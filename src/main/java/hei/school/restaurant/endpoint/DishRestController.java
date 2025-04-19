package hei.school.restaurant.endpoint;

import hei.school.restaurant.dao.operations.DishCRUDOperations;
import hei.school.restaurant.endpoint.mapper.DishIngredientRestMapper;
import hei.school.restaurant.endpoint.mapper.DishRestMapper;
import hei.school.restaurant.endpoint.rest.CreateDishIngredient;
import hei.school.restaurant.endpoint.rest.DishIngredientRest;
import hei.school.restaurant.endpoint.rest.DishRest;
import hei.school.restaurant.endpoint.rest.ProcessingTimeResponse;
import hei.school.restaurant.model.*;
import hei.school.restaurant.service.DishService;
import hei.school.restaurant.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class DishRestController {
    private final DishService dishService;
    private final DishRestMapper dishRestMapper;
    private final DishCRUDOperations dishCRUDOperations;
    private final OrderService orderService;

    @GetMapping("/dishes")
    public ResponseEntity<Object> getDishes(
            @RequestParam(required = false,defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        List<Dish> dishes = dishService.getAll(page, size);
        List<DishRest> dishRests = dishes.stream().map(
                dishRestMapper::toRest
        ).toList();
        return ResponseEntity.status(HttpStatus.OK).body(dishRests);
    }

    @PutMapping("/dishes/{id}/ingredients")
    public ResponseEntity<Object> updateIngredients(@PathVariable int id, @RequestBody List<CreateDishIngredient> dishIngredients){
        List<DishIngredient> ingredients = dishIngredients.stream()
                .map(ingredient->
                        new DishIngredient(
                                new Ingredient(ingredient.getId(), ingredient.getName()),
                                ingredient.getRequiredQuantity(),
                                ingredient.getUnit()
                        )
                ).toList();
        dishService.addDishes(id,ingredients);
        Dish dish = dishCRUDOperations.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(dishRestMapper.toRest(dish));
    }

    @GetMapping("/dishes/{id}/processingTime")
    public ResponseEntity<?> getProcessingTime(
            @PathVariable int id,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(required = false, defaultValue = "SECONDS") TimeUnit unit,
            @RequestParam(required = false, defaultValue = "AVERAGE") CalculationType calculationType
    ) {
        long duration = (long) orderService.calculateProcessingTime(id, startDate, endDate, unit, calculationType);
        return ResponseEntity.ok().body(new ProcessingTimeResponse(id, duration, unit, calculationType));
    }
}
