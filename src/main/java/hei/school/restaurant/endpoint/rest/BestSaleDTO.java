package hei.school.restaurant.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BestSaleDTO {
    private Long dishId;
    private String dishName;
    private Integer quantitySold;
    private Double totalAmount;
}
