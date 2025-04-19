package hei.school.restaurant.dao.operations;

import hei.school.restaurant.dao.DataSource;
import hei.school.restaurant.dao.mapper.DishMapper;
import hei.school.restaurant.model.Dish;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DishCRUDOperations implements CRUDOperations<Dish> {
    private final DataSource datasource;
    private final DishMapper dishMapper;
    private final DishIngredientCRUDOperations dishIngredientCRUDOperations;

    @Override
    public List<Dish> getAll(Integer page, Integer size) {
        int defaultPage = (page != null) ? page : 1;
        int defaultSize = (size != null) ? size : 10;

        List<Dish> dishes = new ArrayList<>();
        String sql = """
                SELECT d.id,d.name,d.price
                from dish d LIMIT ? OFFSET ?
                """;
        try(Connection conn = datasource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);)
        {
            ps.setInt(1, defaultSize);
            ps.setInt(2, (defaultPage - 1) * defaultSize);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                dishes.add(dishMapper.apply(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dishes;
    }


    @SneakyThrows
    @Override
    public List<Dish> saveAll(List<Dish> items) {
        String sql = """
                insert into dish (id,name,price)
                values (?,?,?)
                on conflict (id)
                do update set name=excluded.name,price=excluded.price
                returning id,name,price
                """;
        List<Dish> dishes = new ArrayList<>();
        try(Connection conn = datasource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql))
        {
            items.forEach(item -> {
                try{
                    ps.setInt(1,item.getId());
                    ps.setString(2,item.getName());
                    ps.setDouble(3,item.getPrice());
                    ps.executeQuery();
                    if(item.getIngredients() != null && !item.getIngredients().isEmpty()) {
                        dishIngredientCRUDOperations.saveAll(item.getIngredients(),item.getId());
                    }
                    ps.executeBatch();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    dishes.add(dishMapper.apply(rs));
                }
            }
            return dishes;
        }
    }

    @SneakyThrows
    public Dish findById(int id) {
        try(Connection conn = datasource.getConnection();
        PreparedStatement ps = conn.prepareStatement("select id,name,price from dish where id=?"))
        {
            ps.setInt(1,id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) {
                    return dishMapper.apply(rs);
                }
                throw new RuntimeException("Dish.id=" + id + " not found");
            }
        }
    }
}
