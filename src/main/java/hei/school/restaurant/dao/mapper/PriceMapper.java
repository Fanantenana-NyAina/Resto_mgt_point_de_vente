package hei.school.restaurant.dao.mapper;

import hei.school.restaurant.model.Price;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
public class PriceMapper implements Function<ResultSet, Price> {
    @SneakyThrows
    @Override
    public Price apply(ResultSet resultSet) {
        Price price = new Price();
        price.setId(resultSet.getInt("id"));
        price.setAmount(resultSet.getDouble("amount"));
        price.setCreatedDateTime(resultSet.getTimestamp("created_datetime").toLocalDateTime());
        return price;
    }
}
