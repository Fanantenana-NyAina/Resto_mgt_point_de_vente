package hei.school.restaurant.dao.mapper.order;


import hei.school.restaurant.dao.operations.order.DishOrderCRUDOperations;
import hei.school.restaurant.dao.operations.order.OrderStatusCRUDOperations;
import hei.school.restaurant.model.order.DishOrder;
import hei.school.restaurant.model.order.Order;
import hei.school.restaurant.model.order.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class OrderMapper implements Function<ResultSet, Order> {
    private final DishOrderCRUDOperations dishOrderCRUDOperations;
    private final OrderStatusCRUDOperations orderStatusCRUDOperations;

    @SneakyThrows
    @Override
    public Order apply(ResultSet resultSet) {
        int idOrder = resultSet.getInt("id");
        String reference = resultSet.getString("reference");
        List<DishOrder> dishOrders = dishOrderCRUDOperations.findByReference(reference);
        List<OrderStatus> orderStatuses = orderStatusCRUDOperations.findByIdOrder(idOrder);


        Order order = new Order();
        order.setId(idOrder);
        order.setReference(reference);
        if(!dishOrders.isEmpty()){
            order.setDishes(dishOrders);
        }
        if(!orderStatuses.isEmpty()){
            order.setStatusList(orderStatuses);
        }
        return order;
    }
}
