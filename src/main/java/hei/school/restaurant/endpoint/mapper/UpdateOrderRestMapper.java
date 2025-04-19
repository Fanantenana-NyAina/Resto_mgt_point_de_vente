package hei.school.restaurant.endpoint.mapper;

import hei.school.restaurant.dao.operations.order.OrderCRUDOperations;
import hei.school.restaurant.endpoint.rest.UpdateOrderRest;
import hei.school.restaurant.model.order.DishOrder;
import hei.school.restaurant.model.order.DishOrderStatus;
import hei.school.restaurant.model.order.Order;
import hei.school.restaurant.model.order.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdateOrderRestMapper {
    private final CreateDishOrderMapper createDishOrderMapper;
    private final OrderCRUDOperations orderCRUDOperations;

    public Order toModel(UpdateOrderRest updateOrderRest,String reference) {
        OrderStatus orderStatus = new OrderStatus(
                updateOrderRest.getOrderStatus()
        );
        DishOrderStatus dishOrderStatus = new DishOrderStatus(
                updateOrderRest.getOrderStatus()
        );

        List<DishOrder> dishes = updateOrderRest.getDishes().stream()
                .map(createDishOrderMapper)
                .toList();
        dishes.forEach(dishOrder -> {
            dishOrder.setStatusList(List.of(dishOrderStatus));
        });
        Order order = orderCRUDOperations.findByReference(reference);
        if(order.getDishes() == null){
            order.setDishes(dishes);
        }
        else{
            order.getDishes().addAll(dishes);
        }
        order.getStatusList().add(orderStatus);
        return order;
    }
}
