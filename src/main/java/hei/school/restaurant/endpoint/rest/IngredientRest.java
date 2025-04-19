package hei.school.restaurant.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public class IngredientRest {
    private int id;
    private String name;
    private Double actualPrice;
    private Double availableQuantity;
    private List<PriceRest> prices;
    private List<StockMovementRest> stockMovements;

}
