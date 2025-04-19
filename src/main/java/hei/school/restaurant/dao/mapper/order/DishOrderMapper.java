package hei.school.restaurant.dao.mapper.order;

import hei.school.restaurant.dao.operations.DishCRUDOperations;
import hei.school.restaurant.dao.operations.order.DishOrderStatusCRUDOperations;
import hei.school.restaurant.model.Dish;
import hei.school.restaurant.model.order.DishOrder;
import hei.school.restaurant.model.order.DishOrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class DishOrderMapper implements Function<ResultSet, DishOrder> {
    private final DishOrderStatusCRUDOperations dishOrderStatusCRUDOperations;
    private final DishCRUDOperations dishCRUDOperations;

    @SneakyThrows
    @Override
    public DishOrder apply(ResultSet resultSet) {
        Dish dish = dishCRUDOperations.findById(resultSet.getInt("id_dish"));
        List<DishOrderStatus> dishOrderStatuses = dishOrderStatusCRUDOperations.findByIdDishOrder(resultSet.getInt("id_dish"),resultSet.getInt("id_order"));

        DishOrder dishOrder = new DishOrder();
        dishOrder.setDish(dish);
        dishOrder.setQuantity(resultSet.getInt("quantity"));
        dishOrder.setStatusList(dishOrderStatuses);
        return dishOrder;
    }
}
