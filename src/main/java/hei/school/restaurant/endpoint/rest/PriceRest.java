package hei.school.restaurant.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class PriceRest {
    private int id;
    private Double amount;
    private LocalDateTime createdDateTime;
}
