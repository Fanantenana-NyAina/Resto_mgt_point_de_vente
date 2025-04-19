package hei.school.restaurant.endpoint.rest;

import hei.school.restaurant.model.order.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DishOrderRest {
    private String name;
    private int quantity;
    private Status actualStatus;

}
