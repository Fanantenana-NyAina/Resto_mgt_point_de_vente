package hei.school.restaurant.endpoint.rest;

import hei.school.restaurant.model.StockMovementType;
import hei.school.restaurant.model.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class StockMovementRest {
    private int id;
    private Double quantity;
    private Unit unit;
    private StockMovementType type;
    private LocalDateTime creationDatetime;
}
