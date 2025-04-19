package hei.school.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StockMovement {
    private int id;
    private Ingredient ingredient;
    private StockMovementType type;
    private Double quantity;
    private Unit unit;
    private LocalDateTime creationDatetime;

    public StockMovement(int id,StockMovementType type, Double quantity, Unit unit, LocalDateTime creationDatetime) {
        this.id = id;
        this.type = type;
        this.quantity = quantity;
        this.unit = unit;
        this.creationDatetime = creationDatetime;
    }
}
