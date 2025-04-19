package hei.school.restaurant.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DishRest {
    private int id;
    private String name;
    private int availableQuantity;
    private List<DishIngredientRest> ingredients;
}
