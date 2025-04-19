package hei.school.restaurant.service;

import hei.school.restaurant.dao.operations.DishCRUDOperations;
import hei.school.restaurant.model.Dish;
import hei.school.restaurant.model.DishIngredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DishService {
    private final DishCRUDOperations dishCRUDOperations;

    public List<Dish> getAll(int page, int size) {
        return dishCRUDOperations.getAll(page,size);
    }

    public Dish addDishes(int idDish, List<DishIngredient> dishesToAdd) {
        Dish dish = dishCRUDOperations.findById(idDish);
        dish.addIngredients(dishesToAdd);
        List<Dish> dishesSaved = dishCRUDOperations.saveAll(List.of(dish));
        return dishesSaved.getFirst();
    }
}
