package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.*;

public class ProductDAO {

    /**
     * Inserts a new product into the database.
     *
     * @param product The product to insert.
     * @return The productID of this product in database
     * @throws SQLException If there is a problem executing the insert.
     */  
    public static int insertProduct(Product product) throws SQLException {
        int productId = 0;
        String insertSQL = "INSERT INTO Product (brand_id, product_name, product_code, "
                + "retail_price, description, stock_quantity) "
                + "VALUES (?,?,?,?,?,?)";
        
        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            // wait for change
            if (productCodeExist(product.getProductCode())) {
                System.out.println("Check the product code since this code is duplicated in the database");
                return -1;
            } else {
                System.out.println("Successful pass the productCodeExist");
            }
            
            // Set the parameters for the product
            preparedStatement.setInt(1, product.getBrand().getBrandID());
            preparedStatement.setString(2, product.getProductName());
            preparedStatement.setString(3, product.getProductCode());
            preparedStatement.setDouble(4, product.getRetailPrice());
            preparedStatement.setString(5, product.getDescription());
            preparedStatement.setInt(6, product.getStockQuantity());
    
            // Execute the insert
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
    
    /**
     * Update a product information into the database.
     *
     * @param product The product to update.
     * @throws SQLException If there is a problem executing the update.
     */ 
    public static void updateProduct(Product product) throws SQLException {
        String updateSQL = "UPDATE Product SET brand_id = ?, product_name = ?, product_code = ?, retail_price = ?, "
            + "description = ?, stock_quantity = ? WHERE product_id = ?;"; 

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL, Statement.RETURN_GENERATED_KEYS)) {
            // Set the parameters for the product
            preparedStatement.setInt(1, product.getBrand().getBrandID());
            preparedStatement.setString(2, product.getProductName());
            preparedStatement.setString(3, product.getProductCode());
            preparedStatement.setDouble(4, product.getRetailPrice());
            preparedStatement.setString(5, product.getDescription());
            preparedStatement.setInt(6, product.getStockQuantity());
            preparedStatement.setInt(7,product.getProductID());

            preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Deletes a product from the database by its ID.
     *
     * @param productId The ID of the product to be deleted.
     * @throws SQLException If a database access error occurs or this method is called on a closed connection.
     */
    public static void deleteProduct(int productId) throws SQLException {
        String deleteSQL = "DELETE FROM Product WHERE product_id = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, productId);

            int rowsAffected = preparedStatement.executeUpdate();
            
            // Print to Test
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
     * Retrieve the product in the database by its productID.
     * 
     * @param productID the product ID of the product to be found.
     * @return The selected Product object in database.
     * @throws SQLException If there is a problem executing the select.
     */
    public static Product findProductByID(int productID) throws SQLException {
        String selectSQL = "SELECT * FROM Product WHERE product_id = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, productID);
            ResultSet resultSet = preparedStatement.executeQuery();
            Product product = new Product();

            while (resultSet.next()) {
                product.setProductID(resultSet.getInt("product_id"));
                Brand brand = BrandDAO.findBrand(resultSet.getInt("brand_id"));
                product.setBrand(brand);
                product.setProductName(resultSet.getString("product_name"));
                product.setProductCode(resultSet.getString("product_code"));
                product.setDescription(resultSet.getString("description"));
                product.setRetailPrice(resultSet.getFloat("retail_price"));
                product.setStockQuantity(resultSet.getInt("stock_quantity"));
            }

            // //Print for test
            // System.out.println("<=================== GET SPECIFIC PRODUCTS By ID====================>");
            // System.out.println(product.toString());
            // System.out.println("<======================================================>");

            return product;
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        
    }

    /**
     * Retrieve the product in the database by its productCode.
     * 
     * @param productCode the product code of the product to be found.
     * @return The selected Product object in database.
     * @throws SQLException If there is a problem executing the select.
     */
    public static Product findProductByCode(String productCode) throws SQLException {
        String selectSQL = "SELECT * FROM Product WHERE product_code = ?;";
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, productCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            Product product = new Product();

            while (resultSet.next()) {
                product.setProductID(resultSet.getInt("product_id"));
                Brand brand = BrandDAO.findBrand(resultSet.getInt("brand_id"));
                product.setBrand(brand);
                product.setProductName(resultSet.getString("product_name"));
                product.setProductCode(resultSet.getString("product_code"));
                product.setDescription(resultSet.getString("description"));
                product.setRetailPrice(resultSet.getFloat("retail_price"));
                product.setStockQuantity(resultSet.getInt("stock_quantity"));
            }

            // Print for test
            // System.out.println("<=================== GET SPECIFIC PRODUCTS By Code====================>");
            // System.out.println(product.toString());
            // System.out.println("<======================================================>");

            return product;
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        
    }

    /**
     * Retrieve all products in the database.
     * 
     * @return An ArrayList<Product> with all Product in database.
     * @throws SQLException If there is a problem executing the select.
     */
    public static ArrayList<Product> getAllProduct() throws SQLException {
        String selectSQL = "SELECT * FROM Product";
        ArrayList<Product> productList = new ArrayList<>();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Product product = new Product();
                product.setProductID(resultSet.getInt("product_id"));
                Brand brand = BrandDAO.findBrand(resultSet.getInt("brand_id"));
                product.setBrand(brand);
                product.setProductName(resultSet.getString("product_name"));
                product.setProductCode(resultSet.getString("product_code"));
                product.setDescription(resultSet.getString("description"));
                product.setRetailPrice(resultSet.getFloat("retail_price"));
                product.setStockQuantity(resultSet.getInt("stock_quantity"));
                productList.add(product);
            }

            
            //  //Print for test
            //  System.out.println("<=================== GET ALL PRODUCTS ====================>");
            //  for (Product obj : productList) {
            //      System.out.println(obj.toString());
            //  }
            //  System.out.println("<======================================================>");
             
            return productList;
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Retrieves the product ID from the database for a given product name.
     *
     * @param productName The name of the product for which to retrieve the ID.
     * @return The product ID corresponding to the given product name, or 0 if not found.
     * @throws SQLException If a database error occurs.
     */
    public static int findIDByName(String productName) throws SQLException {
        String selectSQL = "SELECT product_id FROM Product WHERE product_name = ?";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, productName);
            ResultSet resultSet = preparedStatement.executeQuery();

            int productId = 0;

            while (resultSet.next()) {
                productId = resultSet.getInt("product_id");
            }
            return productId;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Checks if a product with the specified code already exists in the database.
     * 
     * @param productCode The code of the product to check in the database.
     * @return {@code True} if the code exists, {@code False} otherwise.
     * @throws SQLException If there is an error during the database query operation.
     */
    public static Boolean productCodeExist(String productCode) throws SQLException {
        String selectSQL = "SELECT COUNT(*) FROM Product WHERE product_code = ?";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, productCode);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return false;
    }
}
