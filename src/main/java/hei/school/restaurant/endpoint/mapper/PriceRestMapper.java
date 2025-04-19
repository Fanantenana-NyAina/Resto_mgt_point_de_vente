package hei.school.restaurant.endpoint.mapper;

import hei.school.restaurant.endpoint.rest.PriceRest;
import hei.school.restaurant.model.Price;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class
PriceRestMapper implements Function<Price, PriceRest> {
    @Override
    public PriceRest apply(Price price) {
        return new PriceRest(price.getId(),price.getAmount(),price.getCreatedDateTime());
    }
}
