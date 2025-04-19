package hei.school.restaurant.dao.mapper;

import hei.school.restaurant.dao.operations.PriceCRUDOperations;
import hei.school.restaurant.dao.operations.StockMovementCRUDOperations;
import hei.school.restaurant.model.Ingredient;
import hei.school.restaurant.model.Price;
import hei.school.restaurant.model.StockMovement;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class IngredientMapper implements Function<ResultSet, Ingredient> {
    private final PriceCRUDOperations priceCRUDOperations;
    private final StockMovementCRUDOperations stockMovementCRUDOperations;

    @SneakyThrows
    @Override
    public Ingredient apply(ResultSet resultSet) {
        int idIngredient = resultSet.getInt("id");
        List<Price> prices = priceCRUDOperations.findById(idIngredient);
        List<StockMovement> stockMovements = stockMovementCRUDOperations.findById(idIngredient);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(idIngredient);
        ingredient.setName(resultSet.getString("name"));
        ingredient.setPrices(prices);
        ingredient.setStockMovements(stockMovements);
        return ingredient;
    }
}
