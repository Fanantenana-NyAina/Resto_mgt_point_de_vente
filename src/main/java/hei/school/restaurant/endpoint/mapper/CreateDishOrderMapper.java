package hei.school.restaurant.endpoint.mapper;

import hei.school.restaurant.dao.operations.DishCRUDOperations;
import hei.school.restaurant.endpoint.rest.CreateDishOrder;
import hei.school.restaurant.model.Dish;
import hei.school.restaurant.model.order.DishOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class CreateDishOrderMapper implements Function<CreateDishOrder, DishOrder> {
    private final DishCRUDOperations dishCRUDOperations;

    @Override
    public DishOrder apply(CreateDishOrder dishOrder) {
        Dish dish = dishCRUDOperations.findById(dishOrder.getDishIdentifier());
        DishOrder dishOrderModel = new DishOrder();
        dishOrderModel.setQuantity(dishOrder.getQuantityOrdered());
        dishOrderModel.setDish(dish);
        return dishOrderModel;
    }

}
