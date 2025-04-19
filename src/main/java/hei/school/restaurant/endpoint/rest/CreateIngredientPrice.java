package hei.school.restaurant.endpoint.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CreateIngredientPrice {
    private int id;

    @JsonProperty("price")
    private Double amount;

    @JsonProperty("dateValue")
    private LocalDateTime createdDatetime;
}
