package hei.school.restaurant.endpoint.mapper;

import hei.school.restaurant.dao.operations.IngredientCRUDOperations;
import hei.school.restaurant.endpoint.rest.DishIngredientRest;
import hei.school.restaurant.endpoint.rest.IngredientBasicRest;
import hei.school.restaurant.model.DishIngredient;
import hei.school.restaurant.model.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class DishIngredientRestMapper implements Function<DishIngredient, DishIngredientRest> {
    @Autowired private IngredientCRUDOperations ingredientCRUDOperations;
    @Autowired private IngredientBasicRestMapper ingredientBasicRestMapper;

    @Override
    public DishIngredientRest apply(DishIngredient dishIngredient) {
        Ingredient ingredient = ingredientCRUDOperations.findByIdIngredient(dishIngredient.getIngredient().getId());
        IngredientBasicRest ingredientBasicRest = ingredientBasicRestMapper.apply(ingredient);
        return new DishIngredientRest(ingredientBasicRest,
                dishIngredient.getRequiredQuantity(),
                dishIngredient.getUnit());
    }
}
