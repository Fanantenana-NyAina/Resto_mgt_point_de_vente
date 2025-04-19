package hei.school.restaurant.dao.operations.order;

import hei.school.restaurant.dao.DataSource;
import hei.school.restaurant.dao.mapper.order.DishOrderStatusMapper;
import hei.school.restaurant.dao.operations.CRUDOperations;
import hei.school.restaurant.model.Price;
import hei.school.restaurant.model.order.DishOrderStatus;
import hei.school.restaurant.model.order.Status;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class DishOrderStatusCRUDOperations implements CRUDOperations<DishOrderStatus> {
    private final DataSource dataSource;
    private final DishOrderStatusMapper dishOrderStatusMapper;

    @Override
    public List<DishOrderStatus> getAll(Integer page, Integer size) {
        return List.of();
    }

    @Override
    public List<DishOrderStatus> saveAll(List<DishOrderStatus> items) {
        return List.of();
    }

    @SneakyThrows
    public List<DishOrderStatus> saveAll(List<DishOrderStatus> items,int idOrder,int idDish) {
        List<DishOrderStatus> newPrices = new ArrayList<>();
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("insert into dish_order_status (id_order,id_dish,status) " +
                    "values (?,?,?::status) on conflict (status) do nothing " +
                    "returning id_order,id_dish,status,created_datetime");)
        {
            items.forEach(item -> {
                try{
                    ps.setInt(1, idOrder);
                    ps.setInt(2,idDish);
                    ps.setString(3, item.getStatus().toString());
                    ps.addBatch();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    newPrices.add(dishOrderStatusMapper.apply(rs));
                }
            }
            return newPrices;
        }
    }

    public List<DishOrderStatus> findByIdDishOrder(int idDish,int idOrder) {
        List<DishOrderStatus> statusList = new ArrayList<>();
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT dos.id_dish, dos.id_order, status, created_datetime " +
                            "FROM dish_order_status dos " +
                            "JOIN dish_order d " +
                            "ON dos.id_dish = d.id_dish " +
                            "AND dos.id_order = d.id_order " +
                            "WHERE dos.id_dish = ? AND dos.id_order = ?");)
        {
            ps.setInt(1, idDish);
            ps.setInt(2, idOrder);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DishOrderStatus status = dishOrderStatusMapper.apply(rs);
                    statusList.add(status);
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return statusList;
    }

    @SneakyThrows
    public DishOrderStatus saveDishOrderStatus(DishOrderStatus dishOrderStatus,int idOrder,int idDish) {
        String sql = """
                insert into dish_order_status (id_order, id_dish, status)
                values (?,?,?::status) on conflict (status) do nothing returning id_order,id_dish,status,created_datetime
                """;
        try(Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);)
        {
            ps.setInt(1, idOrder);
            ps.setInt(2, idDish);
            ps.setString(3, dishOrderStatus.getStatus().name());
            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    return dishOrderStatusMapper.apply(rs);
                }
                throw new RuntimeException("Not saved");
            }
        }
    }

    public List<DishOrderStatus> findByDishIdAndStatusAndDateRange(
            int dishId, Status status, String startDate, String endDate
    ) {
        String query = "SELECT * FROM dish_order_status " +
                "WHERE id_dish = ? AND status = ?::status " +
                "AND created_datetime BETWEEN ?::timestamp AND ?::timestamp";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, dishId);
            ps.setString(2, status.name());
            ps.setString(3, startDate);
            ps.setString(4, endDate);
            ResultSet rs = ps.executeQuery();
            List<DishOrderStatus> statuses = new ArrayList<>();
            while (rs.next()) {
                statuses.add(dishOrderStatusMapper.apply(rs));
            }
            return statuses;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
