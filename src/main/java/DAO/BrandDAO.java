package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Brand;

public class BrandDAO {

    private Connection connection;

    public BrandDAO(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Inserts a new brand into the Brand table in the database.
     * 
     * @param newBrand The Brand object containing the information to be inserted.
     * @param connection The Connection object to the database.
     * @throws SQLException If there is an error during the database insert operation.
     */
    public void insertBrand(Brand newBrand, Connection connection) throws SQLException {
        try {
            String insertSQL = "INSERT INTO Brand (BrandName, Country) VALUES (?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, newBrand.getBrandName());
            preparedStatement.setString(2, newBrand.getCountry());

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Finds brand information based on the brand code.
     *
     * @param brandID   The ID of the brand to be retrieved.
     * @param connection  The database connection.
     * @return            A Brand object containing the retrieved brand information, or null if not found.
     * @throws SQLException If a SQLException occurs while executing the database operation.
     */
    public static Brand findBrand(int brandID, Connection connection) throws SQLException {
        try {
            String insertSQL = "SELECT * FROM Brand WHERE BrandID = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, brandID);

            ResultSet resultSet = preparedStatement.executeQuery();
            Brand brand = new Brand();
            while (resultSet.next()) {
                    brand.setBrandName(resultSet.getString("BrandName"));
                    brand.setCountry(resultSet.getString("Country"));
                }
            return brand;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Checks if a brand with the specified name already exists in the Brand table.
     * 
     * @param brandName The name of the brand to check in the database.
     * @param connection The Connection object to the database.
     * @return {@code true} if the brand exists, {@code false} otherwise.
     * @throws SQLException If there is an error during the database query operation.
     */
    public boolean brandExists(String brandName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Brand WHERE BrandName = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, brandName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    

}

