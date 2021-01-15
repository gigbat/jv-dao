package internet.shop.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can't find mySQL driver", e);
        }
    }

    public static Connection getConnection() {
        Properties properties = new Properties();
        properties.put("user", "root");
        properties.put("password", "root");
        String url = "jdbc:mysql://localhost:3320/internet_shop?serverTimezone=UTC";

        try {
            Connection connection = DriverManager.getConnection(url, properties);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Can't establish the connection to DB", e);
        }
    }
}
