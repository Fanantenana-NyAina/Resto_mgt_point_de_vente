package hei.school.restaurant.endpoint.rest;

import hei.school.restaurant.model.order.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderRest {
    private int id;
    private String reference;
    private Double price;
    private List<DishOrderRest> dishOrders;
    private Status actualStatus;

}
