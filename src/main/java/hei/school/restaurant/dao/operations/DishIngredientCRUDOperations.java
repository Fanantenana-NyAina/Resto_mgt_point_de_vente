package hei.school.restaurant.dao.operations;

import hei.school.restaurant.dao.DataSource;
import hei.school.restaurant.dao.mapper.DishIngredientMapper;
import hei.school.restaurant.dao.mapper.IngredientMapper;
import hei.school.restaurant.model.DishIngredient;
import hei.school.restaurant.model.Ingredient;
import hei.school.restaurant.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class DishIngredientCRUDOperations implements CRUDOperations<DishIngredient> {
    private final DataSource dataSource;
    private final DishIngredientMapper dishIngredientMapper;

    @Override
    public List<DishIngredient> getAll(Integer page, Integer size) {
        return List.of();
    }

    @Override
    public List<DishIngredient> saveAll(List<DishIngredient> items) {
        return List.of();
    }

    @SneakyThrows
    public List<DishIngredient> saveAll(List<DishIngredient> items,int idDish) {
        List<DishIngredient> dishIngredients = new ArrayList<>();
        String sql = """
            insert into dish_ingredient (id_dish, id_ingredient, required_quantity, unit)
            values (?,?,?,?::unit)
            on conflict (id_dish,id_ingredient)
            do update set required_quantity=excluded.required_quantity,unit=excluded.unit
            returning id_dish,id_ingredient,required_quantity,unit;
            """;
        try(Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql))
        {
            items.forEach(item-> {
                try{
                    ps.setInt(1,idDish);
                    ps.setInt(2,item.getIngredient().getId());
                    ps.setDouble(3,item.getRequiredQuantity());
                    ps.setString(4,item.getUnit().toString());
                    ps.addBatch();
                }catch (SQLException e){
                    throw new RuntimeException(e);
                }
            });
            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    dishIngredients.add(dishIngredientMapper.apply(rs));
                }
            }
            return dishIngredients;
        }
    }
    @SneakyThrows
    public List<DishIngredient> findbyIdDish(int id){
        List<DishIngredient> dishIngredients = new ArrayList<>();
        String sql = """
            select di.id_ingredient,di.required_quantity,unit
            from dish_ingredient di
            join ingredient i
            on di.id_ingredient = i.id
            where id_dish = ?
            """;
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ){
            ps.setInt(1,id);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    dishIngredients.add(dishIngredientMapper.apply(rs));
                }
                return dishIngredients;
            }
        }
    }
}
