package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.*;

public class DatabaseOperations {

    // Insert a new book into the database
    public void insertEra(Era newEra, Connection connection) throws SQLException {
        try {
            // Create an SQL INSERT statement
            String insertSQL = "INSERT INTO Era (EraCode, Description) VALUES (?, ?)";

            // Prepare and execute the INSERT statement
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, newEra.getEracode());
            preparedStatement.setString(2, newEra.getDescription());

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception to signal an error.
        }
    }
}
