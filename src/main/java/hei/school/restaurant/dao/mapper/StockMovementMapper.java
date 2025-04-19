package hei.school.restaurant.dao.mapper;

import hei.school.restaurant.model.StockMovement;
import hei.school.restaurant.model.StockMovementType;
import hei.school.restaurant.model.Unit;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
public class StockMovementMapper implements Function<ResultSet, StockMovement> {
    @SneakyThrows
    @Override
    public StockMovement apply(ResultSet resultSet) {
        StockMovement stockMovement = new StockMovement();
        stockMovement.setId(resultSet.getInt("id"));
        stockMovement.setType(StockMovementType.valueOf(resultSet.getString("move")));
        stockMovement.setQuantity(resultSet.getDouble("quantity"));
        stockMovement.setUnit(Unit.valueOf(resultSet.getString("unit")));
        stockMovement.setCreationDatetime(resultSet.getTimestamp("created_datetime").toLocalDateTime());
        return stockMovement;
    }
}
