package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.*;

public class ProductDAO {
    private Connection connection;

    public ProductDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inserts a new product into the database.
     *
     * @param product The product to insert.
     * @return The productID of this product in database
     * @throws SQLException If there is a problem executing the insert.
     */  
    public int insertProduct(Product product) throws SQLException {
        int productId = 0;
        String insertSQL = "INSERT INTO Product (BrandID, ProductName, ProductCode, "
                + "RetailPrice, Description, StockQuantity) "
                + "VALUES (?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            // Set the parameters for the product
            preparedStatement.setInt(1, product.getBrand().getBrandID());
            preparedStatement.setString(2, product.getProductName());
            preparedStatement.setString(3, product.getProductCode());
            preparedStatement.setDouble(4, product.getRetailPrice());
            preparedStatement.setString(5, product.getDescription());
            preparedStatement.setInt(6, product.getStockQuantity());

            // Execute the update
            int rowsAffected = preparedStatement.executeUpdate();

            // Check if the insert was successful
            if (rowsAffected > 0) {
                // Retrieve the generated keys (if any)
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        productId = generatedKeys.getInt(1);
                    }
                }
            } else {
                throw new SQLException("Creating product failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return productId;
    }

    public void updateProduct() {

    }

    /**
     * Deletes a product from the database by its ID.
     *
     * @param productId The ID of the product to be deleted.
     * @throws SQLException If a database access error occurs or this method is called on a closed connection.
     */
    public void deleteProduct(int productId) throws SQLException {
        String deleteSQL = "DELETE FROM Product WHERE ProductID = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            // Set the parameters for the prepared statement
            preparedStatement.setInt(1, productId);

            // Execute the delete statement
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Product with ID " + productId + " was deleted successfully.");
            } else {
                System.out.println("No product was found with ID " + productId + " to delete.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }        
    }


    /**
     * Retrieve all products in the database.
     * @return A arrayList<Product> with all Product in database.
     * @throws SQLException If there is a problem executing the insert.
     */
    public ArrayList<Product> getAllProduct() throws SQLException {
        String selectSQL = "SELECT * FROM Product";
        ArrayList<Product> productList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product();
                product.setProductID(resultSet.getInt("ProductID"));
                product.setBrand(BrandDAO.findBrand(resultSet.getInt("BrandID"),connection));
                product.setProductName(resultSet.getString("ProductName"));
                product.setProductCode(resultSet.getString("ProductCode"));
                product.setDescription(resultSet.getString("Description"));
                product.setRetailPrice(resultSet.getFloat("RetailPrice"));
                product.setStockQuantity(resultSet.getInt("StockQuantity"));
                productList.add(product);
            }

            // Print for test
            System.out.println("<=================== GET ALL PRODUCTS ====================>");
            for (Product obj : productList) {
                System.out.println(obj.toString());
            }
            System.out.println("<======================================================>");

            return productList;
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
