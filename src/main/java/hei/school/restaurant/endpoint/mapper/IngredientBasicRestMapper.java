package hei.school.restaurant.endpoint.mapper;

import hei.school.restaurant.endpoint.rest.IngredientBasicRest;
import hei.school.restaurant.model.Ingredient;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class IngredientBasicRestMapper implements Function<Ingredient, IngredientBasicRest> {

    @Override
    public IngredientBasicRest apply(Ingredient ingredient) {
        return new IngredientBasicRest(ingredient.getId(),ingredient.getName(),ingredient.getActualPrice(),ingredient.getAvailableQuantity());
    }
}
