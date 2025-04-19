package hei.school.restaurant.model.order;

import hei.school.restaurant.dao.DataSource;
import hei.school.restaurant.dao.mapper.order.OrderStatusMapper;
import hei.school.restaurant.dao.operations.order.OrderStatusCRUDOperations;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Order {
    private int id;
    private String reference;
    private List<DishOrder> dishes;
    private List<OrderStatus> statusList;

    public Order(String reference){
        this.reference = reference;
    }

    public Status getActualStatus() {
        OrderStatusCRUDOperations orderStatusCRUDOperations = new OrderStatusCRUDOperations(new DataSource(),new OrderStatusMapper());
        if(statusList == null){
            OrderStatus orderStatus = orderStatusCRUDOperations.saveOrderStatus(Status.CREATED,this.id);
            return orderStatus.getStatus();
    }
        return statusList.stream()
                .max(Comparator.comparing(OrderStatus::getCreatedDatetime))
                .map(OrderStatus::getStatus)
                .orElse(Status.CREATED);
    }

    public double getTotalAmount(){
        if(dishes == null){
            return 0;
        }
        return dishes.stream()
                .map(d->d.getQuantity() * d.getDish().getPrice())
                .reduce(0.0, Double::sum);
    }
}
