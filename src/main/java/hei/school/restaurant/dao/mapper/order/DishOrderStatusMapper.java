package hei.school.restaurant.dao.mapper.order;

import hei.school.restaurant.model.order.DishOrderStatus;
import hei.school.restaurant.model.order.Status;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
public class DishOrderStatusMapper implements Function<ResultSet, DishOrderStatus> {
    @SneakyThrows
    @Override
    public DishOrderStatus apply(ResultSet resultSet) {
        DishOrderStatus dishOrderStatus = new DishOrderStatus();
        dishOrderStatus.setStatus(Status.valueOf(resultSet.getString("status")));
        dishOrderStatus.setCreatedDatetime(resultSet.getTimestamp("created_datetime").toLocalDateTime());
        return dishOrderStatus;
    }
}
