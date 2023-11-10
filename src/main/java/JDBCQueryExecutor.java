import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JDBCQueryExecutor {
    private static Connection connection;
    private static String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static String JDBC_URL = "jdbc:sqlserver://localhost:1433;database=IJP;TrustServerCertificate=true;";
    private static String USERNAME = "sa";
    private static String PASSWORD = "Tuanvu1993@";

    // Hàm để tạo kết nối đến cơ sở dữ liệu
    public JDBCQueryExecutor() {

    }

    public static void openConnection() {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            //System.out.println("Connected successfully to the database.");
        } catch (SQLException e) {
            System.err.println("Connection error to the database: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // Hàm để đóng kết nối
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
               // System.out.println("Connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    // Hàm để thực hiện truy vấn SELECT
    public static ResultSet executeSelectQuery(String query, Object... params) {
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            // Đặt các tham số cho truy vấn (nếu có)
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.err.println("Lỗi khi thực hiện truy vấn SELECT: " + e.getMessage());
        }
        return resultSet;
    }

    // Hàm để thực hiện truy vấn UPDATE hoặc INSERT
    public static int executeUpdateQuery(String query, Object... params) {
        int rowsAffected = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            // Đặt các tham số cho truy vấn (nếu có)
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi thực hiện truy vấn UPDATE hoặc INSERT: " + e.getMessage());
        }
        return rowsAffected;
    }



}
