package hei.school.restaurant.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class Price {
    private int id;
    private Ingredient ingredient;
    private Double amount;
    private LocalDateTime createdDateTime;

    public Price(int id,Double amount, LocalDateTime createdDateTime) {
        this.id = id;
        this.amount = amount;
        this.createdDateTime = createdDateTime;
    }
}
