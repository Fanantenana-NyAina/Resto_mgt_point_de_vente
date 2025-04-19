package hei.school.restaurant.dao.operations.order;

import hei.school.restaurant.dao.DataSource;
import hei.school.restaurant.dao.mapper.order.OrderStatusMapper;
import hei.school.restaurant.dao.operations.CRUDOperations;
import hei.school.restaurant.model.order.DishOrderStatus;
import hei.school.restaurant.model.order.OrderStatus;
import hei.school.restaurant.model.order.Status;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Repository
public class OrderStatusCRUDOperations implements CRUDOperations<OrderStatus> {
    private final DataSource dataSource;
    private final OrderStatusMapper orderStatusMapper;

    @Override
    public List<OrderStatus> getAll(Integer page, Integer size) {
        return List.of();
    }

    @Override
    public List<OrderStatus> saveAll(List<OrderStatus> items) {
        return List.of();
    }

    public List<OrderStatus> findByIdOrder(int idOrder) {
        List<OrderStatus> orderStatusList = new ArrayList<>();
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT os.id_order, status, created_datetime " +
                            "FROM order_status os " +
                            "JOIN \"order\" o " +
                            "ON os.id_order = o.id "+
                            "WHERE os.id_order=?");)
        {
            ps.setInt(1, idOrder);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    OrderStatus orderStatus = orderStatusMapper.apply(rs);
                    orderStatusList.add(orderStatus);
                }
                return orderStatusList;
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public OrderStatus saveOrderStatus(Status status, int idOrder) {
        String sql = """
                insert into order_status (id_order, status)
                values (?,?::status) on conflict (status) do nothing returning id_order,status,created_datetime
                """;
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);)
        {
            ps.setInt(1, idOrder);
            ps.setString(2, status.name());
            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    return orderStatusMapper.apply(rs);
                }
                throw new RuntimeException("Not saved");
            }
        }
    }

    @SneakyThrows
    public List<OrderStatus> saveAll(List<OrderStatus> items,int idOrder) {
        List<OrderStatus> newPrices = new ArrayList<>();
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("insert into order_status (id_order,status,created_datetime) " +
                    "values (?,?::status,?) on conflict (status) do nothing " +
                    "returning id_order,status,created_datetime");)
        {
            items.forEach(item -> {
                try{
                    ps.setInt(1, idOrder);
                    ps.setString(2, item.getStatus().toString());
                    ps.setTimestamp(3, Timestamp.valueOf(item.getCreatedDatetime()));
                    ps.addBatch();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    newPrices.add(orderStatusMapper.apply(rs));
                }
            }
            return newPrices;
        }
    }
}
