package hei.school.restaurant.dao.operations.sale;

import hei.school.restaurant.dao.DataSource;
import hei.school.restaurant.dao.operations.CRUDOperations;
import hei.school.restaurant.endpoint.rest.DishSale;
import hei.school.restaurant.model.Dish;
import hei.school.restaurant.model.sale.Sale;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class SaleCRUDOperations implements CRUDOperations<Sale> {
    private final DataSource dataSource;

    @Override
    public List<Sale> getAll(Integer page, Integer size) {
        return List.of();
    }

    @Override
    public List<Sale> saveAll(List<Sale> items) {
        return List.of();
    }

    @SneakyThrows
    public List<Sale> getBetweenDate(String startDate, String endDate, int limit) {
        List<Sale> sales = new ArrayList<>();
        String sql = """
                SELECT
                    d.id,
                    d.name,
                    SUM(d_o.quantity) AS quantity_sold,
                    SUM(d.price * d_o.quantity) AS total_revenue
                FROM
                    dish_order d_o
                    JOIN dish d ON d_o.id_dish = d.id
                    JOIN "order" o ON d_o.id_order = o.id
                    JOIN order_status os ON o.id = os.id_order
                WHERE
                    os.status = 'FINISHED'
                    AND os.created_datetime BETWEEN ?::date AND ?::date
                GROUP BY
                    d.id,
                    d.name
                ORDER BY
                    quantity_sold DESC
                LIMIT ?;
                """;
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);)
        {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ps.setInt(3, limit);
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Sale sale = new Sale();
                    sale.setDish(new DishSale(rs.getInt("id"), rs.getString("name")));
                    sale.setQuantitySold(rs.getInt("quantity_sold"));
                    sale.setTotalPrice(rs.getDouble("total_revenue"));
                    sales.add(sale);
                }
            }
        }
        return sales;
    }
}
