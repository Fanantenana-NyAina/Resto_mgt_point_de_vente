package hei.school.restaurant.dao.mapper.order;

import hei.school.restaurant.model.order.OrderStatus;
import hei.school.restaurant.model.order.Status;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
public class OrderStatusMapper implements Function<ResultSet, OrderStatus> {
    @SneakyThrows
    @Override
    public OrderStatus apply(ResultSet resultSet) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setStatus(Status.valueOf(resultSet.getString("status")));
        orderStatus.setCreatedDatetime(resultSet.getTimestamp("created_datetime").toLocalDateTime());
        return orderStatus;
    }
}
