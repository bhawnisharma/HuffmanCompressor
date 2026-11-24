package util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    public static Connection getConnection() throws SQLException {
        try {
            Properties p = new Properties();
            InputStream in = DBConnection.class.getClassLoader()
                    .getResourceAsStream("db.properties");
            if (in == null) {
                in = new FileInputStream("src/db.properties");
            }
            p.load(in);
            in.close();
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                    p.getProperty("url"),
                    p.getProperty("user"),
                    p.getProperty("password"));
        } catch (Exception e) {
            throw new SQLException("DB connection failed: " + e.getMessage(), e);
        }
    }
}