package hei.school.restaurant.service;

import hei.school.restaurant.dao.operations.IngredientCRUDOperations;
import hei.school.restaurant.dao.operations.PriceCRUDOperations;
import hei.school.restaurant.dao.operations.StockMovementCRUDOperations;
import hei.school.restaurant.model.Ingredient;
import hei.school.restaurant.model.Price;
import hei.school.restaurant.model.StockMovement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientCRUDOperations ingredientCRUDOperations;
    private final StockMovementCRUDOperations stockMovementCRUDOperations;
    private final PriceCRUDOperations priceCRUDOperations;

    public List<Ingredient> findByPrices(Double priceMinFilter, Double priceMaxFilter,int page, int size) {
        if(priceMinFilter != null && priceMaxFilter != null && priceMinFilter > priceMaxFilter) {
            throw new RestClientException("PriceMinFilter must be greater than priceMaxFilter");
        }
        if(priceMinFilter != null  && priceMinFilter < 0) {
            throw new RestClientException("PriceMinFilter : "+ priceMinFilter +" is 0");
        }
        if(priceMaxFilter != null  && priceMaxFilter < 0) {
            throw new RestClientException("PriceMinFilter : "+ priceMaxFilter +" is 0");
        }
        List<Ingredient> ingredients = ingredientCRUDOperations.getAll(page, size);

        return ingredients.stream()
                .filter(ingredient -> {
                    Double actualPrice = ingredient.getActualPrice();
                    if(priceMinFilter == null && priceMaxFilter == null) {
                        return true;
                    }
                    if (priceMinFilter == null){
                        return actualPrice < priceMaxFilter;
                    }
                    if(priceMaxFilter == null){
                        return actualPrice > priceMinFilter;
                    }
                    return actualPrice > priceMinFilter && actualPrice < priceMaxFilter;
                }).toList();
    }

    public List<Ingredient> update(List<Ingredient> ingredients) {
        return ingredientCRUDOperations.saveAll(ingredients);
    }

    public List<Ingredient> getAll(int page,int size) {
        return ingredientCRUDOperations.getAll(page, size);
    }

    public Ingredient findById(int id) {
        return ingredientCRUDOperations.findById(id);
    }

    public List<Ingredient> saveAll(List<Ingredient> ingredients) {
        return ingredientCRUDOperations.saveAll(ingredients);
    }

    public Ingredient addPrices(int ingredientId, List<Price> pricesToAdd) {
        Ingredient ingredient = ingredientCRUDOperations.findById(ingredientId);
        ingredient.addPrices(pricesToAdd);
        priceCRUDOperations.saveAll(pricesToAdd,ingredientId);
        return ingredient;
    }

    public Ingredient addStockMovements(int ingredientId, List<StockMovement> stockMovementsToAdd) {
        Ingredient ingredient = ingredientCRUDOperations.findById(ingredientId);
        ingredient.addStockMovements(stockMovementsToAdd);
        stockMovementCRUDOperations.saveAll(stockMovementsToAdd,ingredientId);
        return ingredient;
    }

}
