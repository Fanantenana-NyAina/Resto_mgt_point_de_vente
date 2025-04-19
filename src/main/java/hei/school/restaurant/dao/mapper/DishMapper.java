package hei.school.restaurant.dao.mapper;

import hei.school.restaurant.dao.operations.DishIngredientCRUDOperations;
import hei.school.restaurant.dao.operations.IngredientCRUDOperations;
import hei.school.restaurant.model.Dish;
import hei.school.restaurant.model.DishIngredient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DishMapper implements Function<ResultSet, Dish> {
    private final DishIngredientCRUDOperations dishIngredientCRUDOperations;

    @SneakyThrows
    @Override
    public Dish apply(ResultSet resultSet) {
        Dish dish = new Dish();
        int idDish = resultSet.getInt("id");
        List<DishIngredient> ingredients = dishIngredientCRUDOperations.findbyIdDish(idDish);

        dish.setId(idDish);
        dish.setName(resultSet.getString("name"));
        dish.setPrice(resultSet.getInt("price"));
        dish.setIngredients(ingredients);

        return dish;
    }
}
