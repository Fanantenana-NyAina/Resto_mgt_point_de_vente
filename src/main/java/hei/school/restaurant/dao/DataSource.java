package hei.school.restaurant.dao;

import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DataSource {
    private static final int port = Integer.parseInt(System.getenv("PORT"));
    private static final String host = System.getenv("HOST");
    private static final String username = System.getenv("USERNAME");
    private static final String password = System.getenv("PASSWORD");
    private static final String database = System.getenv("DATABASE");
    private final String url;

    public DataSource() {
        url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
    }
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
