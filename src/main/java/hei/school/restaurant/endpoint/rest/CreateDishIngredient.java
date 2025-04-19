package hei.school.restaurant.endpoint.rest;

import hei.school.restaurant.model.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateDishIngredient {
    private Double requiredQuantity;
    private int id;
    private String name;
    private Unit unit;
}
