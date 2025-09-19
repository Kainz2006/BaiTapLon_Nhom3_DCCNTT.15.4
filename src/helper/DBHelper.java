package helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
    // Thay đổi thông tin kết nối dưới đây để phù hợp với cài đặt MySQL của bạn
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/qlbanhang";
    private static final String USERNAME = "root"; // Tên người dùng MySQL của bạn
    private static final String PASSWORD = "Dinhdiepvu98";     // Mật khẩu MySQL của bạn

    public static Connection getConnection() throws SQLException {
        try {
            // Đăng ký JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL JDBC Driver not found.");
        }
    }
}
