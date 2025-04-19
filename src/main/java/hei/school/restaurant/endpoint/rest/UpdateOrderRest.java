package hei.school.restaurant.endpoint.rest;

import hei.school.restaurant.model.order.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UpdateOrderRest {
    private Status orderStatus;
    private List<CreateDishOrder> dishes;
}
