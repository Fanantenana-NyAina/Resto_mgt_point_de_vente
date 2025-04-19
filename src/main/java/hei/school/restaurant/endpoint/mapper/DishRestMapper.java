package hei.school.restaurant.endpoint.mapper;

import hei.school.restaurant.dao.operations.DishIngredientCRUDOperations;
import hei.school.restaurant.endpoint.rest.DishRest;
import hei.school.restaurant.model.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DishRestMapper {
    @Autowired private DishIngredientCRUDOperations dishIngredientCRUDOperations;
    @Autowired private DishIngredientRestMapper dishIngredientMapper;

    public DishRest toRest(Dish dish) {
        DishRest dishRest = new DishRest();
        dishRest.setId(dish.getId());
        dishRest.setName(dish.getName());
        dishRest.setAvailableQuantity(dish.getAvailableQuantity());
        dishRest.setIngredients(dish.getIngredients().stream()
                .map(dishIngredientMapper::apply)
                .toList());
        return dishRest;
    }
}
