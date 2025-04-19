package hei.school.restaurant.model.order;

import hei.school.restaurant.model.Dish;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishOrder {
    private Order order;
    private Dish dish;
    private int quantity;
    private List<DishOrderStatus> statusList;

    public Status getActualStatus() {
        return statusList.stream()
                .max(Comparator.comparing(DishOrderStatus::getCreatedDatetime))
                .map(DishOrderStatus::getStatus).orElse(Status.CREATED);
    }
}
