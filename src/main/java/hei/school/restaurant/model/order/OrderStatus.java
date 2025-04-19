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
public class OrderStatus {
    private Order order;
    private Status status;
    private LocalDateTime createdDatetime;

    public OrderStatus(Status status) {
        this.status = status;
        this.createdDatetime =LocalDateTime.now();
    }
}
