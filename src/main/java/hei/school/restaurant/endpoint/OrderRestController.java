package hei.school.restaurant.endpoint;


import hei.school.restaurant.dao.operations.order.OrderStatusCRUDOperations;
import hei.school.restaurant.endpoint.mapper.OrderRestMapper;
import hei.school.restaurant.endpoint.mapper.UpdateOrderRestMapper;
import hei.school.restaurant.endpoint.rest.UpdateOrderRest;
import hei.school.restaurant.model.order.DishOrderStatus;
import hei.school.restaurant.model.order.Order;
import hei.school.restaurant.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
public class OrderRestController {
    private final OrderService orderService;
    private final OrderRestMapper orderRestMapper;
    private final OrderStatusCRUDOperations orderStatusCRUDOperations;
    private final UpdateOrderRestMapper updateOrderRestMapper;

    @GetMapping("/orders/{reference}")
    public ResponseEntity<Object> getOrder(@PathVariable String reference){
        Order order = orderService.getDishByReference(reference);
        if(order == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order :"+reference+" not found");
        }
        if(order.getDishes() == null){
            return ResponseEntity.status(HttpStatus.OK).body(orderRestMapper.toBasicRest(order));
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body(orderRestMapper.toRest(order));
        }
    }

    @PutMapping("/orders/{reference}/dishes")
    public ResponseEntity<Object> updateOrder(@PathVariable String reference, @RequestBody UpdateOrderRest orderToUpdate){
        Order order = updateOrderRestMapper.toModel(orderToUpdate,reference);
        orderStatusCRUDOperations.saveAll(order.getStatusList(),order.getId());
        Order updatedOrder = orderService.updateDishOrder(reference,order.getDishes());
        return ResponseEntity.status(HttpStatus.OK).body(orderRestMapper.toRest(updatedOrder));
    }

    @PutMapping("/orders/{reference}/dishes/{dishId}")
    public ResponseEntity<Object> updateDishOrderStatus(@PathVariable String reference, @PathVariable int dishId, @RequestBody DishOrderStatus dishOrderStatus){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.updateStatus(reference,dishId,dishOrderStatus));
    }

    @PostMapping("/orders/{reference}")
    public ResponseEntity<Object> createOrder(@PathVariable String reference){
        Order order = orderService.saveOrder(reference);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderRestMapper.toBasicRest(order));
    }
}
