package hei.school.restaurant.service;

import hei.school.restaurant.dao.operations.sale.SaleCRUDOperations;
import hei.school.restaurant.model.sale.Sale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleService {
    private final SaleCRUDOperations saleCRUDOperations;

    public List<Sale> getDishSales(String startDate, String endDate, int limit) {
        return saleCRUDOperations.getBetweenDate(startDate, endDate, limit);
    }
}
