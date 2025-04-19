package hei.school.restaurant.endpoint.rest;

import hei.school.restaurant.model.CalculationType;
import hei.school.restaurant.model.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProcessingTimeResponse {
    private int dishId;
    private long duration;
    private TimeUnit unit;
    private CalculationType calculationType;
}
