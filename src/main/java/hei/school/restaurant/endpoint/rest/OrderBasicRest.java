package hei.school.restaurant.endpoint.rest;

import hei.school.restaurant.model.order.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class OrderBasicRest {
    private int id;
    private String reference;
    private Status ActualStatus;
    private Double totalAmount;
    private List<DishOrderRest> dishes;
}
