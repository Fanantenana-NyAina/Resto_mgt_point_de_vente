package hei.school.restaurant.model.sale;

import hei.school.restaurant.endpoint.rest.DishSale;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sale {
    private DishSale dish;
    private int quantitySold;
    private double totalPrice;
}
