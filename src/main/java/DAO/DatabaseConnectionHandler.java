package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionHandler {
    // Connect to Remote Server
    private static final String DB_URL = "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team004";
    private static final String DB_USER = "team004";
    private static final String DB_PASSWORD = "ofo4Et0uu";
    private Connection connection = null;

    public void openConnection() throws SQLException {
        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish the connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL driver not found", e);
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // Log error
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

}