package hei.school.restaurant.dao.mapper;

import hei.school.restaurant.dao.operations.DishIngredientCRUDOperations;
import hei.school.restaurant.dao.operations.IngredientCRUDOperations;
import hei.school.restaurant.model.DishIngredient;
import hei.school.restaurant.model.Ingredient;
import hei.school.restaurant.model.Unit;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DishIngredientMapper implements Function<ResultSet, DishIngredient> {
    private final IngredientCRUDOperations ingredientCRUDOperations;

    @SneakyThrows
    @Override
    public DishIngredient apply(ResultSet resultSet) {
        DishIngredient dishIngredient = new DishIngredient();
        int idIngredient = resultSet.getInt("id_ingredient");
        Ingredient ingredient = ingredientCRUDOperations.findByIdIngredient(idIngredient);

        dishIngredient.setIngredient(ingredient);
        dishIngredient.setRequiredQuantity(resultSet.getDouble("required_quantity"));
        dishIngredient.setUnit(Unit.valueOf(resultSet.getString("unit")));
        return dishIngredient;
    }
}
