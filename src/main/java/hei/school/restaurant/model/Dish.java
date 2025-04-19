package hei.school.restaurant.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class Dish {
    private int id;
    private String name;
    private double price;
    private List<DishIngredient> ingredients;

    public Double getIngredientCost() {
        return ingredients.stream()
            .mapToDouble(i-> i.getRequiredQuantity() * i.getIngredient().getActualPrice()).sum();
    }

    public Double getIngredientCost(LocalDateTime date){
        return ingredients.stream()
                .mapToDouble(i-> i.getRequiredQuantity() * i.getIngredient().getPriceAt(date)).sum();
    }

    public Double getGrossMargin(){
        return price - getIngredientCost();
    }

    public Double getGrossMargin(LocalDateTime date){
        return price - getIngredientCost(date);
    }

    public int getAvailableQuantity(){
        return ingredients.stream()
                .mapToInt(i-> (int) (i.getIngredient().getAvailableQuantity() / i.getRequiredQuantity() )).min().orElse(0);
    }

    public List<DishIngredient> addIngredients(List<DishIngredient> ingredientList) {
        List<DishIngredient> dishIngredients = this.ingredients;
        dishIngredients.addAll(ingredientList);
        return dishIngredients;
    }

}
