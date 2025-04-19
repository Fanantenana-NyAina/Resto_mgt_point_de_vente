package hei.school.restaurant.endpoint.mapper;

import hei.school.restaurant.dao.operations.IngredientCRUDOperations;
import hei.school.restaurant.dao.operations.PriceCRUDOperations;
import hei.school.restaurant.endpoint.rest.CreateIngredient;
import hei.school.restaurant.endpoint.rest.IngredientRest;
import hei.school.restaurant.endpoint.rest.PriceRest;
import hei.school.restaurant.endpoint.rest.StockMovementRest;
import hei.school.restaurant.model.Ingredient;
import hei.school.restaurant.model.Price;
import hei.school.restaurant.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class IngredientRestMapper {
    @Autowired private PriceRestMapper priceRestMapper;
    @Autowired private StockMovementRestMapper stockMovementRestMapper;
    @Autowired private IngredientCRUDOperations ingredientCRUDOperations;

    public IngredientRest toRest(Ingredient ingredient) {
        List<PriceRest> prices = ingredient.getPrices() != null ?
                ingredient.getPrices().stream()
                        .map(priceRestMapper::apply)
                        .toList() :
                Collections.emptyList();
        List<StockMovementRest> stockMovements = ingredient.getStockMovements() != null ?
                ingredient.getStockMovements().stream()
                        .map(stockMovementRestMapper::apply)
                .toList() : Collections.emptyList();

        return new IngredientRest(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getActualPrice(),
                ingredient.getAvailableQuantity(),
                prices,
                stockMovements
        );
    }

    public Ingredient toModel(CreateIngredient newIngredient) {
        Price price = new Price();
        price.setAmount(newIngredient.getPrice());
        price.setCreatedDateTime(newIngredient.getCreatedDateTime());

        return new Ingredient(
                newIngredient.getId(),
                newIngredient.getName(),
                Collections.singletonList(price)
        );
    }

    public Ingredient toModel(IngredientRest ingredientRest) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientRest.getId());
        ingredient.setName(ingredientRest.getName());
        ingredient.setPrices(new ArrayList<>());
        ingredient.setStockMovements(new ArrayList<>());
        try{
            Ingredient existingIngredient = ingredientCRUDOperations.findById(ingredientRest.getId());
            ingredient.addPrices(existingIngredient.getPrices());
            ingredient.addStockMovements(existingIngredient.getStockMovements());
        }catch (NotFoundException e){
            ingredient.addPrices(new ArrayList<>());
            ingredient.addStockMovements(new ArrayList<>());
        }
        return ingredient;
    }
}
