package Connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class To_Database {
    public static Connection connection;
    public static void Database()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/user", "root", "8702402г");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
