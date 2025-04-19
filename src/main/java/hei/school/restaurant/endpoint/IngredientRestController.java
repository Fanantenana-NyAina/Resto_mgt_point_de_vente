package hei.school.restaurant.endpoint;

import hei.school.restaurant.dao.operations.IngredientCRUDOperations;
import hei.school.restaurant.endpoint.mapper.IngredientRestMapper;
import hei.school.restaurant.endpoint.rest.CreateIngredient;
import hei.school.restaurant.endpoint.rest.CreateIngredientPrice;
import hei.school.restaurant.endpoint.rest.CreateIngredientStockMovement;
import hei.school.restaurant.endpoint.rest.IngredientRest;
import hei.school.restaurant.model.Ingredient;
import hei.school.restaurant.model.Price;
import hei.school.restaurant.model.StockMovement;
import hei.school.restaurant.service.IngredientService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@AllArgsConstructor
public class IngredientRestController {
    private final IngredientService ingredientService;
    private final IngredientRestMapper ingredientRestMapper;
    private final IngredientCRUDOperations ingredientCRUDOperations;

    @GetMapping("/ingredients")
    public ResponseEntity<List<Ingredient>> getAllIngredients(
            @RequestParam(required = false,defaultValue = "1") int page,
            @RequestParam(required = false,defaultValue = "10") int size,
            @RequestParam(required = false)Double priceMinFilter,
            @RequestParam(required = false)Double priceMaxFilter ) {
        return ResponseEntity.ok(ingredientService.findByPrices(priceMinFilter,priceMaxFilter,page,size));
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<Ingredient> findById(@PathVariable int id) {
        Ingredient ingredient = ingredientService.findById(id);
        return ResponseEntity.ok(ingredient);
    }

    @PostMapping("/ingredients")
    public ResponseEntity<List<IngredientRest>> saveIngredients(
            @RequestBody List<CreateIngredient> ingredientsToCreate) {

        // Conversion des DTO en modèles
        List<Ingredient> ingredients = ingredientsToCreate.stream()
                .map(ingredientRestMapper::toModel)
                .toList();

        // Sauvegarde
        List<Ingredient> savedIngredients = ingredientService.saveAll(ingredients);

        // Conversion en DTO de réponse
        List<IngredientRest> response = savedIngredients.stream()
                .map(ingredientRestMapper::toRest)
                .toList();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/ingredients")
    public ResponseEntity<List<IngredientRest>> updateIngredients(@RequestBody List<IngredientRest> ingredientsToUpdate) {
        List<Ingredient> ingredients = ingredientsToUpdate.stream()
                .map(ingredientRestMapper::toModel).toList();

        List<Ingredient> updatedIngredients = ingredientService.update(ingredients);

        List<IngredientRest> response = updatedIngredients.stream()
                .map(ingredientRestMapper::toRest).toList();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/ingredients/{idIngredient}/prices")
    public ResponseEntity<IngredientRest> updateIngredientPrices(@PathVariable int idIngredient, @RequestBody List<CreateIngredientPrice> ingredientPrices){
        List<Price> prices = ingredientPrices.stream()
                .map(price->
                        new Price(price.getId(),price.getAmount(),price.getCreatedDatetime())
                        ).toList();
        Ingredient ingredient = ingredientService.addPrices(idIngredient,prices);
        IngredientRest ingredientRest = ingredientRestMapper.toRest(ingredient);

        return ResponseEntity.status(HttpStatus.OK).body(ingredientRest);
    }

    @PutMapping("/ingredients/{idIngredient}/stockMovements")
    public ResponseEntity<IngredientRest> updateIngredientStockMovements(@PathVariable int idIngredient, @RequestBody List<CreateIngredientStockMovement> ingredientStockMovements){
        List<StockMovement> stockMovements = ingredientStockMovements.stream()
                .map(stockMovement->
                        new StockMovement(stockMovement.getId(),stockMovement.getType(),stockMovement.getQuantity(),stockMovement.getUnit(),stockMovement.getCreationDatetime())).toList();
        Ingredient ingredient = ingredientService.addStockMovements(idIngredient,stockMovements);
        IngredientRest ingredientRest = ingredientRestMapper.toRest(ingredient);

        return ResponseEntity.status(HttpStatus.OK).body(ingredientRest);
    }
}
