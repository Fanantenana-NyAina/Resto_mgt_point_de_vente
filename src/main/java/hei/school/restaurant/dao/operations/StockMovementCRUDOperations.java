package hei.school.restaurant.dao.operations;

import hei.school.restaurant.dao.DataSource;
import hei.school.restaurant.dao.mapper.StockMovementMapper;
import hei.school.restaurant.model.StockMovement;
import hei.school.restaurant.service.exception.ServerException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Repository
public class StockMovementCRUDOperations implements CRUDOperations<StockMovement> {
    private final DataSource dataSource;
    private final StockMovementMapper stockMovementMapper;

    @Override
    public List<StockMovement> getAll(Integer page, Integer size) {
        return List.of();
    }

    @Override
    public List<StockMovement> saveAll(List<StockMovement> items) {
        return List.of();
    }

    public List<StockMovement> saveAll(List<StockMovement> entities,int id) {

        List<StockMovement> stockMovements = new ArrayList<>();
        String sql = """
                insert into stock_movement (id,id_ingredient, move, quantity, unit, created_datetime)
                values (?, ?, ?::stock_movement_type, ?, ?::unit, ?)
                returning id, id_ingredient, move,quantity, unit, created_datetime""";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {
            entities.forEach(entityToSave -> {
                try {
                    statement.setInt(1, entityToSave.getId());
                    statement.setInt(2,id);
                    statement.setString(3, entityToSave.getType().name());
                    statement.setDouble(4, entityToSave.getQuantity());
                    statement.setString(5, entityToSave.getUnit().name());
                    statement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                    statement.addBatch(); // group by batch so executed as one query in database
                } catch (SQLException e) {
                    throw new ServerException(e);
                }
            });
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    stockMovements.add(stockMovementMapper.apply(resultSet));
                }
            }
            return stockMovements;
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    public List<StockMovement> findById(int idIngredient) {
        List<StockMovement> stockMovements = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select s.id, id_ingredient, s.move::stock_movement_type, s.quantity, s.unit::unit, s.created_datetime from stock_movement s"
                             + " join ingredient i on s.id_ingredient = i.id"
                             + " where s.id_ingredient = ?")) {
            statement.setLong(1, idIngredient);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    stockMovements.add(stockMovementMapper.apply(resultSet));
                }
                return stockMovements;
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }
}
