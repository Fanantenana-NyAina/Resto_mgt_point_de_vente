package hei.school.restaurant.endpoint;

import hei.school.restaurant.service.SaleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@AllArgsConstructor
public class SaleRestController {
    private final SaleService saleService;

    @GetMapping("/bestSales")
    public ResponseEntity<Object> getDishSales(@RequestParam String startDate,@RequestParam String endDate,@RequestParam int limit) {
        return ResponseEntity.status(HttpStatus.OK).body(saleService.getDishSales(startDate, endDate, limit));
    }
}
