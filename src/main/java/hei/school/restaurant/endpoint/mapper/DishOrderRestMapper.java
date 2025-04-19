package hei.school.restaurant.endpoint.mapper;

import hei.school.restaurant.endpoint.rest.DishOrderRest;
import hei.school.restaurant.model.order.DishOrder;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class DishOrderRestMapper implements Function<DishOrder, DishOrderRest> {
    @Override
    public DishOrderRest apply(DishOrder dishOrder) {
        DishOrderRest dishOrderRest = new DishOrderRest();
        dishOrderRest.setName(dishOrder.getDish().getName());
        dishOrderRest.setQuantity(dishOrder.getQuantity());
        dishOrderRest.setActualStatus(dishOrder.getActualStatus());
        return dishOrderRest;
    }
}
