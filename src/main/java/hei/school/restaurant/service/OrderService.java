package hei.school.restaurant.service;

import hei.school.restaurant.dao.operations.order.DishOrderCRUDOperations;
import hei.school.restaurant.dao.operations.order.DishOrderStatusCRUDOperations;
import hei.school.restaurant.dao.operations.order.OrderCRUDOperations;
import hei.school.restaurant.dao.operations.order.OrderStatusCRUDOperations;
import hei.school.restaurant.model.CalculationType;
import hei.school.restaurant.model.TimeUnit;
import hei.school.restaurant.model.order.DishOrder;
import hei.school.restaurant.model.order.DishOrderStatus;
import hei.school.restaurant.model.order.Order;
import hei.school.restaurant.model.order.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    @Autowired private OrderCRUDOperations orderCRUDOperations;
    @Autowired private DishOrderCRUDOperations dishOrderCRUDOperations;
    @Autowired private DishOrderStatusCRUDOperations dishOrderStatusCRUDOperations;
    @Autowired private OrderStatusCRUDOperations orderStatusCRUDOperations;

    public Order getDishByReference(String reference) {
        return orderCRUDOperations.findByReference(reference);
    }

    public Order updateDishOrder(String reference, List<DishOrder> dishOrdersToUpdate) {
        return orderCRUDOperations.saveDishes(dishOrdersToUpdate,reference);
    }

    public DishOrderStatus updateStatus(String reference,int idDish, DishOrderStatus dishOrderStatus) {
        Order order = orderCRUDOperations.findByReference(reference);
        List<DishOrder> dishOrders = dishOrderCRUDOperations.findByReference(reference);
        DishOrder dishOrder = dishOrders.stream()
                .filter(di-> di.getDish().getId() == idDish)
                .toList().getFirst();
        dishOrder.getStatusList().add(dishOrderStatus);
        return dishOrderStatusCRUDOperations.saveDishOrderStatus(dishOrderStatus,order.getId(),idDish);
    }

    public Order saveOrder(String reference) {
        System.out.println(orderCRUDOperations.findByReference(reference));
        return orderCRUDOperations.save(reference);
    }

    private long convertDuration(long durationSeconds, TimeUnit unit) {
        return switch (unit) {
            case SECONDS -> durationSeconds;
            case MINUTES -> durationSeconds / 60;
            case HOURS -> durationSeconds / 3600;
        };
    }

    public double calculateProcessingTime(
            int dishId,
            String startDate,
            String endDate,
            TimeUnit unit,
            CalculationType calculationType
    ) {
        List<DishOrderStatus> inProgressList = dishOrderStatusCRUDOperations.findByDishIdAndStatusAndDateRange(
                dishId, Status.IN_PROGRESS, startDate, endDate
        );
        List<DishOrderStatus> finishedList = dishOrderStatusCRUDOperations.findByDishIdAndStatusAndDateRange(
                dishId, Status.FINISHED, startDate, endDate
        );

        // Calcul des durées en secondes
        List<Double> durations = inProgressList.stream()
                .filter(inProgress -> finishedList.stream()
                        .anyMatch(finished -> isMatchingPair(inProgress, finished)))
                .map(inProgress -> {
                    DishOrderStatus finished = finishedList.stream()
                            .filter(f -> f.getCreatedDatetime().isAfter(inProgress.getCreatedDatetime()))
                            .findFirst()
                            .orElseThrow();
                    return (double) Duration.between(
                            inProgress.getCreatedDatetime(),
                            finished.getCreatedDatetime()
                    ).getSeconds();
                })
                .toList();

        // Appliquer le type de calcul (MOYEN, MIN, MAX)
        double result = switch (calculationType) {
            case AVERAGE -> durations.stream().mapToDouble(Double::doubleValue).average().orElse(0);
            case MINIMUM -> durations.stream().mapToDouble(Double::doubleValue).min().orElse(0);
            case MAXIMUM -> durations.stream().mapToDouble(Double::doubleValue).max().orElse(0);
        };

        // Convertir selon l'unité demandée
        return convertToTimeUnit(result, unit);
    }

    private double convertToTimeUnit(double durationInSeconds, TimeUnit unit) {
        return switch (unit) {
            case SECONDS -> durationInSeconds;
            case MINUTES -> durationInSeconds / 60;
            case HOURS -> durationInSeconds / 3600;
        };
    }

    private boolean isMatchingPair(DishOrderStatus inProgress, DishOrderStatus finished) {
        return finished.getCreatedDatetime().isAfter(inProgress.getCreatedDatetime());
    }
}
