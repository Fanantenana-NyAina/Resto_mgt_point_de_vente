package hei.school.restaurant.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DishIngredient {
    private Ingredient ingredient;
    private Double requiredQuantity;
    private Unit unit;
}
