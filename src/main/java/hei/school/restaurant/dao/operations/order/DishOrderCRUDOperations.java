package hei.school.restaurant.dao.operations.order;

import hei.school.restaurant.dao.DataSource;
import hei.school.restaurant.dao.mapper.order.DishOrderMapper;
import hei.school.restaurant.dao.mapper.order.OrderMapper;
import hei.school.restaurant.dao.operations.CRUDOperations;
import hei.school.restaurant.model.order.DishOrder;
import hei.school.restaurant.model.order.DishOrderStatus;
import hei.school.restaurant.model.order.Order;
import hei.school.restaurant.model.order.Status;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Repository
public class DishOrderCRUDOperations implements CRUDOperations<DishOrder> {
    private final DataSource dataSource;
    private final DishOrderMapper dishOrderMapper;
    private final DishOrderStatusCRUDOperations dishOrderStatusCRUDOperations;

    @Override
    public List<DishOrder> getAll(Integer page, Integer size) {
        return List.of();
    }

    @Override
    public List<DishOrder> saveAll(List<DishOrder> items) {
        return List.of();
    }

    @SneakyThrows
    public List<DishOrder> saveAll(List<DishOrder> items,int idOrder) {
        List<DishOrder> dishOrders = new ArrayList<>();
        try(Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("insert into dish_order (id_dish, id_order, quantity) values (?,?,?) on conflict (id_dish,id_order) do update set quantity=excluded.quantity returning id_dish,id_order,quantity");)
        {
            items.forEach(item -> {
                try{
                    ps.setInt(1,item.getDish().getId());
                    ps.setInt(2,idOrder);
                    ps.setDouble(3,item.getQuantity());
                    ps.executeQuery();
                    if (item.getStatusList() != null && !item.getStatusList().isEmpty()) {
                        DishOrderStatus dishOrderStatus = new DishOrderStatus(Status.CREATED);
                        dishOrderStatusCRUDOperations.saveAll(List.of(dishOrderStatus),idOrder,item.getDish().getId());
                        dishOrderStatusCRUDOperations.saveAll(item.getStatusList(),idOrder,item.getDish().getId());
                    }
                    ps.executeBatch();
                }catch (SQLException e){
                    throw new RuntimeException(e);
                }
            });
            try(ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    dishOrders.add(dishOrderMapper.apply(rs));
                }
            }
            return dishOrders;
        }
    }

    public List<DishOrder> findByReference(String reference) {
        List<DishOrder> dishOrders = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT d.id_dish, d.id_order, quantity " +
                            "FROM dish_order d " +
                            "JOIN dish di " +
                            "ON d.id_dish = di.id " +
                            "JOIN \"order\" o " +
                            "ON d.id_order = o.id " +
                            "WHERE reference ILIKE ? "))
        {
            ps.setString(1,"%"+reference+"%");
            try(ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    DishOrder dishOrder = dishOrderMapper.apply(rs);
                    dishOrders.add(dishOrder);
                }
                return dishOrders;
            }

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
