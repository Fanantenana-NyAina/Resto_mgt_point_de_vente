package hei.school.restaurant.endpoint.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CreateIngredient {
    private int id;
    private String name;

    @JsonProperty("unitPrice")
    private Double price;

    @JsonProperty("updatedAt")
    private LocalDateTime createdDateTime;
}
