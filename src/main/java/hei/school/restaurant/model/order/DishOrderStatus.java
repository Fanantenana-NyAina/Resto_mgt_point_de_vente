package hei.school.restaurant.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DishOrderStatus {
    private DishOrder dishOrder;
    private Status status;
    private LocalDateTime CreatedDatetime;

    public DishOrderStatus(Status status){
        this.status = status;
        this.CreatedDatetime = LocalDateTime.now();
    }
}
