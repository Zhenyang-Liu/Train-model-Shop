package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
     * @throws SQLException If there is an error during the database insert operation.
     */
    public void insertBrand(Brand newBrand) throws SQLException {
        String insertSQL = "INSERT INTO Brand (BrandName, Country) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);) {
            preparedStatement.setString(1, newBrand.getBrandName());
            preparedStatement.setString(2, newBrand.getCountry());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * Update a brand information into the database.
     *
     * @param brand The brand to update.
     * @throws SQLException If there is a problem executing the update.
     */ 
    public void updateBrand(Brand brand) throws SQLException {
        String updateSQL = "UPDATE Brand SET BrandName = ?, Country = ? WHERE BrandID = ?;"; 
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, brand.getBrandName());
            preparedStatement.setString(2, brand.getCountry());
            preparedStatement.setInt(3,brand.getBrandID());

            preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    /**
     * Deletes a brand from the database by its ID.
     *
     * @param brandId The ID of the brand to be deleted.
     * @throws SQLException If a database access error occurs or this method is called on a closed connection.
     */
    public void deleteBrand(int brandId) throws SQLException {
        String deleteSQL = "DELETE FROM Brand WHERE BrandID = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            // Set the parameters for the prepared statement
            preparedStatement.setInt(1, brandId);

            // Execute the delete statement
            int rowsAffected = preparedStatement.executeUpdate();

            // Print to test
            if (rowsAffected > 0) {
                System.out.println("Brand with ID " + brandId + " was deleted successfully.");
            } else {
                System.out.println("No brand was found with ID " + brandId + " to delete.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }        
    }

    /**
     * Finds brand information based on the brand ID.
     *
     * @param brandID   The ID of the brand to be retrieved.
     * @return            A Brand object containing the retrieved brand information, or null if not found.
     * @throws SQLException If a SQLException occurs while executing the database operation.
     */
    public Brand findBrand(int brandID) throws SQLException {
        String selectSQL = "SELECT * FROM Brand WHERE BrandID = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, brandID);

            ResultSet resultSet = preparedStatement.executeQuery();
            Brand brand = new Brand();
            while (resultSet.next()) {
                    brand.setBrandID(brandID);
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
     * Finds brand information based on the brand ID.
     *
     * @return An ArrayList<Brand> contains all brands in the database.
     * @throws SQLException If a SQLException occurs while executing the database operation.
     */
    public ArrayList<Brand> findAllBrand() throws SQLException {
        String selectSQL = "SELECT * FROM Brand;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Brand> brandList = new ArrayList<Brand>();
            
            while (resultSet.next()) {
                Brand brand = new Brand();
                brand.setBrandID(resultSet.getInt("BrandID"));
                brand.setBrandName(resultSet.getString("BrandName"));
                brand.setCountry(resultSet.getString("Country"));
                brandList.add(brand);
            }

            // Print for test
            System.out.println("<=================== GET ALL BRANDS ====================>");
            for (Brand obj : brandList) {
                System.out.println(obj.toString());
            }
            System.out.println("<======================================================>");

            return brandList;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    
    /**
     * Checks if a brand with the specified name already exists in the Brand table.
     * 
     * @param brandName The name of the brand to check in the database.
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

