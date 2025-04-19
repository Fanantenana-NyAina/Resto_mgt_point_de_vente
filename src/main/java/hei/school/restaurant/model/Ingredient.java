package hei.school.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Ingredient {
    private int id;
    private String name;
    private List<Price> prices;
    private List<StockMovement> stockMovements;

    public Ingredient(int id,String name,List<Price> prices) {
        this.id = id;
        this.name = name;
        this.prices = prices;
    }

    public Ingredient(int id,String name){
        this.id = id;
        this.name = name;
    }

    public Double getActualPrice(){
        return prices.stream()
                .max(Comparator.comparing(Price::getCreatedDateTime))
                .map(Price::getAmount)
                .orElse(null);
    }
    public List<Price> addPrices(List<Price> priceList) {
        if (getPrices() == null || getPrices().isEmpty()){
            return prices;
        }
        priceList.forEach(price -> price.setIngredient(this));
        getPrices().addAll(priceList);
        return getPrices();
    }
    public List<StockMovement> addStockMovements(List<StockMovement> stockMovementList) {
        stockMovementList.forEach(stockMovement -> stockMovement.setIngredient(this));
        getStockMovements().addAll(stockMovementList);
        return getStockMovements();
    }

    public Double getPriceAt(LocalDateTime date){
        return prices.stream()
                .filter(p-> !p.getCreatedDateTime().isAfter(date))
                .max(Comparator.comparing(Price::getCreatedDateTime))
                .map(Price::getAmount)
                .orElse(getActualPrice());
    }

    public Double getAvailableQuantity(){
        Double in = stockMovements.stream()
                .filter((m)->m.getType().equals(StockMovementType.IN))
                .map(StockMovement::getQuantity)
                .reduce(0.0, Double::sum);
        Double out = stockMovements.stream()
                .filter((m)->m.getType().equals(StockMovementType.OUT))
                .map(StockMovement::getQuantity)
                .reduce(0.0, Double::sum);

        return in - out;
    }

    public double getAvailableQuantity(LocalDateTime date){
        double in = stockMovements.stream()
                .filter(m->!m.getCreationDatetime().isAfter(date))
                .filter((m)->m.getType().equals(StockMovementType.IN))
                .map(StockMovement::getQuantity)
                .reduce(0.0, Double::sum);
        double out = stockMovements.stream()
                .filter(m->m.getCreationDatetime().isAfter(date))
                .filter((m)->m.getType().equals(StockMovementType.OUT))
                .map(StockMovement::getQuantity)
                .reduce(0.0, Double::sum);
        return in - out;
    }
}
