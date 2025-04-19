package hei.school.restaurant.dao.operations;

import hei.school.restaurant.dao.DataSource;
import hei.school.restaurant.dao.mapper.PriceMapper;
import hei.school.restaurant.model.Price;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PriceCRUDOperations implements CRUDOperations<Price> {
    private final DataSource dataSource;
    private final PriceMapper priceMapper;

    @Override
    public List<Price> getAll(Integer page, Integer size) {
        List<Price> prices = new ArrayList<>();
        int defaultPage = page == null ? 1 : page;
        int defaultSize = size == null ? 10 : size;
        try(Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("select id,id_ingredient,amount,created_datetime from price limit ? offset ?"))
        {
            ps.setInt(1, defaultSize);
            ps.setInt(2, defaultSize * (defaultPage - 1));
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    prices.add(priceMapper.apply(rs));
                }
            }
            return prices;
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Price> saveAll(List<Price> prices){
        return List.of();
    }

    @SneakyThrows
    public List<Price> saveAll(List<Price> prices,int id) {
        List<Price> newPrices = new ArrayList<>();
        try(Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("insert into price (id_ingredient, amount,created_datetime) " +
                "values (?,?,?) " +
                "returning id,id_ingredient,amount,created_datetime");)
        {
            prices.forEach(price -> {
                try{
                    ps.setInt(1, id);
                    ps.setDouble(2, price.getAmount());
                    ps.setTimestamp(3, Timestamp.valueOf(price.getCreatedDateTime()));
                    ps.addBatch();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    newPrices.add(priceMapper.apply(rs));
                }
            }
            return newPrices;
        }
    }


    @SneakyThrows
    public List<Price> updateAll(List<Price> prices,int id) {
        List<Price> newPrices = new ArrayList<>();
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("update price set amount=?,created_datetime=? where id_ingredient=? and id=? returning id,id_ingredient,amount,created_datetime"))
        {
            prices.forEach(price -> {
                try{
                    ps.setDouble(1, price.getAmount());
                    ps.setTimestamp(2, Timestamp.valueOf(price.getCreatedDateTime()));
                    ps.setInt(3, id);
                    ps.setInt(4,price.getId());
                    try(ResultSet rs = ps.executeQuery()){
                        while (rs.next()) {
                            newPrices.add(priceMapper.apply(rs));
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            return newPrices;
        }
    }

    @SneakyThrows
    public List<Price> findById(int idIngredient){
        List<Price> prices = new ArrayList<>();
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT p.id,id_ingredient, amount, created_datetime " +
                    "FROM price p " +
                    "join ingredient i " +
                    "on p.id_ingredient = i.id " +
                    "where p.id_ingredient = ?"))
        {
            ps.setInt(1, idIngredient);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    prices.add(priceMapper.apply(rs));
                }
            }
        }
        return prices;
    }
}
