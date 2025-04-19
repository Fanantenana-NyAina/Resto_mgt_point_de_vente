package hei.school.restaurant.endpoint.rest;

import hei.school.restaurant.model.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DishIngredientRest {
    private IngredientBasicRest ingredient;
    private Double requiredQuantity;
    private Unit unit;
}
