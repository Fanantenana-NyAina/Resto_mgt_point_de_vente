package hei.school.restaurant.dao.operations;

import hei.school.restaurant.dao.DataSource;
import hei.school.restaurant.dao.mapper.DishIngredientMapper;
import hei.school.restaurant.dao.mapper.IngredientMapper;
import hei.school.restaurant.model.DishIngredient;
import hei.school.restaurant.model.Ingredient;
import hei.school.restaurant.model.Price;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class IngredientCRUDOperations implements CRUDOperations<Ingredient> {
    private final DataSource dataSource;
    private final IngredientMapper ingredientMapper;
    private final PriceCRUDOperations priceCRUDOperations;
    private final StockMovementCRUDOperations stockMovementCRUDOperations;


    @Override
    public List<Ingredient> getAll(Integer page, Integer size) {
         int defaultPage = (page != null) ? page : 1;
         int defaultSize = (size != null) ? size : 10;
        List<Ingredient> ingredients = new ArrayList<>();
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT i.id,i.name FROM ingredient i ORDER BY i.id LIMIT ? OFFSET ?");)
        {
            ps.setInt(1, defaultSize);
            ps.setInt(2, (defaultPage-1)*defaultSize);
            try(ResultSet rs = ps.executeQuery();){
                while(rs.next()) {
                    Ingredient ingredient = ingredientMapper.apply(rs);
                    ingredients.add(ingredient);
                }
                return ingredients;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Ingredient findById(int id) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT id, name FROM ingredient WHERE id = ?"))
        {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return ingredientMapper.apply(rs);
                }
                throw new RuntimeException("Ingredient.id=" + id + " not found");
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Ingredient findByIdIngredient(int idIngredient) {
        String sql = """
                SELECT id, name
                FROM ingredient
                JOIN dish_ingredient di
                ON ingredient.id = di.id_ingredient
                WHERE di.id_ingredient=?
                ORDER BY id
            """;
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1,idIngredient);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return ingredientMapper.apply(rs);
                }
                throw new RuntimeException("Ingredient.id=" + idIngredient + " not found");
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    @Override
    public List<Ingredient> saveAll(List<Ingredient> ingredientList) {
        List<Ingredient> ingredients = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();) {
            try (PreparedStatement ps = conn.prepareStatement("insert into ingredient (id, name) " +
                    "values (?, ?) " +
                    "on conflict (id) " +
                    "do update set name = excluded.name returning id,name")) {
                ingredientList.forEach(ingredient -> {
                   try{
                       ps.setInt(1, ingredient.getId());
                       ps.setString(2, ingredient.getName());
                       ps.executeQuery();
                       if(ingredient.getPrices() != null && !ingredient.getPrices().isEmpty() ) {
                           priceCRUDOperations.saveAll(ingredient.getPrices(),ingredient.getId());
                       }
                       if(ingredient.getStockMovements() != null && !ingredient.getStockMovements().isEmpty() ) {
                           stockMovementCRUDOperations.saveAll(ingredient.getStockMovements(),ingredient.getId());
                       }
                       ps.executeBatch();
                   }catch (SQLException e){
                       throw new RuntimeException(e);
                   }
                });
                try(ResultSet rs = ps.executeQuery()) {
                    while(rs.next()) {
                        ingredients.add(ingredientMapper.apply(rs));
                    }
                }
                return ingredients;
            }
        }
    }
}
