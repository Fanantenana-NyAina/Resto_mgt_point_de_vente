package hei.school.restaurant.endpoint.mapper;

import hei.school.restaurant.endpoint.rest.StockMovementRest;
import hei.school.restaurant.model.StockMovement;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class StockMovementRestMapper implements Function<StockMovement, StockMovementRest> {
    @Override
    public StockMovementRest apply(StockMovement stockMovement) {
        return new StockMovementRest(
                stockMovement.getId(),
                stockMovement.getQuantity(),
                stockMovement.getUnit(),
                stockMovement.getType(),
                stockMovement.getCreationDatetime()
        );
    }
}
