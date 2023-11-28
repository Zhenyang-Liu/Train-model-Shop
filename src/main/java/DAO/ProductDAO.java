package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.ArrayList;

import exception.ConnectionException;
import exception.DatabaseException;
import helper.Logging;
import model.*;

public class ProductDAO {

    /**
     * Inserts a new product into the database.
     *
     * @param product The product to insert.
     * @return The productID of this product in database
     * @throws DatabaseException If there is a problem executing the insert.
     */  
    public static int insertProduct(Product product) throws DatabaseException {
        int productId = 0;
        String insertSQL = "INSERT INTO Product (brand_name, product_name, product_code, "
                + "retail_price, description, stock_quantity, product_image) "
                + "VALUES (?,?,?,?,?,?, ?)";
        
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
            preparedStatement.setString(1, product.getBrand());
            preparedStatement.setString(2, product.getProductName());
            preparedStatement.setString(3, product.getProductCode());
            preparedStatement.setDouble(4, product.getRetailPrice());
            preparedStatement.setString(5, product.getDescription());
            preparedStatement.setInt(6, product.getStockQuantity());
            preparedStatement.setString(7, product.getImageBase64());
    
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
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    
        return productId;
    }
    
    /**
     * Update a product information into the database.
     *
     * @param product The product to update.
     * @throws DatabaseException If there is a problem executing the update.
     */ 
    public static void updateProduct(Product product) throws DatabaseException {
        String updateSQL = "UPDATE Product SET brand_name = ?, product_name = ?, product_code = ?, retail_price = ?, "
            + "description = ?, stock_quantity = ?, product_image = ? WHERE product_id = ?;"; 

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            // Set the parameters for the product
            preparedStatement.setString(1, product.getBrand());
            preparedStatement.setString(2, product.getProductName());
            preparedStatement.setString(3, product.getProductCode());
            preparedStatement.setDouble(4, product.getRetailPrice());
            preparedStatement.setString(5, product.getDescription());
            preparedStatement.setInt(6, product.getStockQuantity());
            preparedStatement.setString(7, product.getImageBase64());
            preparedStatement.setInt(8,product.getProductID());

            preparedStatement.executeUpdate();
            
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

    public static void updateStock(int productID, int quantity) throws DatabaseException {
        String updateSQL = "UPDATE Product SET stock_quantity = ? WHERE product_id = ?;"; 

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            // Set the parameters for the product
            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, productID);

            preparedStatement.executeUpdate();
            
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

    /**
     * Deletes a product from the database by its ID.
     *
     * @param productId The ID of the product to be deleted.
     * @throws DatabaseException If a database access error occurs or this method is called on a closed connection.
     */
    public static void deleteProduct(int productId) throws DatabaseException {
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
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }        
    }

    /**
     * Retrieve the product in the database by its productID.
     * 
     * @param productID the product ID of the product to be found.
     * @return The selected Product object in database.
     * @throws DatabaseException If there is a problem executing the select.
     */
    public static Product findProductByID(int productID) throws DatabaseException {
        String selectSQL = "SELECT * FROM Product WHERE product_id = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, productID);
            ResultSet resultSet = preparedStatement.executeQuery();
            Product product = new Product();

            while (resultSet.next()) {
                product.setProductID(resultSet.getInt("product_id"));
                product.setBrand(resultSet.getString("brand_name"));
                product.setProductName(resultSet.getString("product_name"));
                product.setProductCode(resultSet.getString("product_code"));
                product.setDescription(resultSet.getString("description"));
                product.setRetailPrice(resultSet.getFloat("retail_price"));
                product.setStockQuantity(resultSet.getInt("stock_quantity"));
                product.setImageBase64(resultSet.getString("product_image"));
            }
            return product;
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

    /**
     * Retrieve the product in the database by its productCode.
     * 
     * @param productCode the product code of the product to be found.
     * @return The selected Product object in database.
     * @throws DatabaseException If there is a problem executing the select.
     */
    public static Product findProductByCode(String productCode) throws DatabaseException {
        String selectSQL = "SELECT * FROM Product WHERE product_code = ?;";
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, productCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            Product product = new Product();

            while (resultSet.next()) {
                product.setProductID(resultSet.getInt("product_id"));
                product.setBrand(resultSet.getString("brand_name"));
                product.setProductName(resultSet.getString("product_name"));
                product.setProductCode(resultSet.getString("product_code"));
                product.setDescription(resultSet.getString("description"));
                product.setRetailPrice(resultSet.getFloat("retail_price"));
                product.setStockQuantity(resultSet.getInt("stock_quantity"));
                product.setImageBase64(resultSet.getString("product_image"));
            }
            return product;
            
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
        
    }

     /**
     * Takes a resultSet generated from execuing a SELECT command on the product database table and turns it into
     * an arraylist of product objects
     *
     * @param selectQueryResults the resultset generated from calling executeQuery with a SELECT statement
     * @return an arraylist containing all the products within the result set
     * @throws SQLException
     */
    private static ArrayList<Product> arrayFromResultSet(ResultSet selectQueryResults) throws SQLException{
        ArrayList<Product> rVal = new ArrayList<>();
        while (selectQueryResults.next()) {
            Product product = new Product();
            product.setProductID(selectQueryResults.getInt("product_id"));
            product.setBrand(selectQueryResults.getString("brand_name"));
            product.setProductName(selectQueryResults.getString("product_name"));
            product.setProductCode(selectQueryResults.getString("product_code"));
            product.setDescription(selectQueryResults.getString("description"));
            product.setRetailPrice(selectQueryResults.getFloat("retail_price"));
            product.setStockQuantity(selectQueryResults.getInt("stock_quantity"));
            product.setImageBase64(selectQueryResults.getString("product_image"));
            rVal.add(product);
        }
        return rVal;
    }

    private static String constructSQLQuery(String searchQuery, float minPrice, float maxPrice, String brand, String sortBy, boolean asc, String type){
        String sqlString = "SELECT * FROM Product ";
        if(type != "")
            sqlString += " INNER JOIN " + type + " ON Product.product_id = " + type + ".product_id";
        sqlString += " WHERE product_name LIKE ? AND ? <= retail_price AND retail_price <= ?";
        if(brand != null && !brand.isEmpty() && !brand.equals("All"))
            sqlString += " AND brand_name = ?";
        if(sortBy != "")
            sqlString += " ORDER BY ? ?";
        return sqlString;
    }
 
    /**
     * Constructs an SQL PreparedStatement based on all the filters given and the search query used by the user
     *
     * @param connection database connection
     * @param searchQuery the input of the search bar by the user (default "")
     * @param minPrice the min price the user has selected (default 0)
     * @param maxPrice the max price the user has selected (default 1e10)
     * @return
     * @throws SQLException
     */
    private static PreparedStatement constructPreparedStatement(Connection connection, String searchQuery, float minPrice, float maxPrice, String brand, String sortBy, boolean asc, String type) throws SQLException{
        String sqlString = constructSQLQuery(searchQuery, minPrice, maxPrice, brand, sortBy, asc, type);
        Integer cExtraIndex = 1;

        PreparedStatement pStatement = connection.prepareStatement(sqlString);
        pStatement.setString(1, "%" + searchQuery + "%");
        pStatement.setFloat(2, minPrice);
        pStatement.setFloat(3, maxPrice == -1 ? 1e10f : maxPrice);
        if(brand != null && !brand.isEmpty() && !brand.equals("All")){
            pStatement.setString(3 + cExtraIndex, brand);
            cExtraIndex++;
        }
        if(sortBy != ""){
            pStatement.setString(3 + cExtraIndex, sortBy);
            pStatement.setString(4 + cExtraIndex, asc ? "ASC" : "DESC");
            cExtraIndex += 2;
        }
        return pStatement;
    }

    /**
     * Filters products based solely on a given search query
     *
     * @param searchQuery the search query entered by the user
     * @return an array list of the products that match the search query
     */
    public static ArrayList<Product> filterProducts(String searchQuery){
        return filterProducts(searchQuery, 0, -1, "", "", true, "");
    }

    /**
     * Filters products based off of all filters given by the user
     * Java does not allow default args so unlucky, all must be provided!! :D Shit language!! :D
     *
     * @param searchQuery
     * @param minPrice
     * @param maxPrice
     * @return
     */
    public static ArrayList<Product> filterProducts(String searchQuery, float minPrice, float maxPrice, String brand, String sortBy, boolean asc, String type){
        try(Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = constructPreparedStatement(connection, searchQuery, minPrice, maxPrice, brand, sortBy, asc, type)) {
            // Return the array of products from the result set
            return arrayFromResultSet(preparedStatement.executeQuery());
        }catch(SQLException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Retrieve all products in the database.
     * 
     * @return An ArrayList<Product> with all Product in database.
     * @throws DatabaseException If there is a problem executing the select.
     */
    public static ArrayList<Product> getAllProduct() {
        String selectSQL = "SELECT * FROM Product";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            
            return arrayFromResultSet(resultSet);       
        } catch (SQLTimeoutException e){
            Logging.getLogger().warning("Error when finding all products: SQL Timed out\nStacktrace: " + e.getMessage());
        } catch (SQLException e) {
            Logging.getLogger().warning("Error when finding all products: SQL Excepted\nStacktrace: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * Retrieves the product ID from the database for a given product name.
     *
     * @param productName The name of the product for which to retrieve the ID.
     * @return The product ID corresponding to the given product name, or 0 if not found.
     * @throws DatabaseException If a database error occurs.
     */
    public static int findIDByName(String productName) throws DatabaseException {
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
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

    /**
     * Checks if a product with the specified code already exists in the database.
     * 
     * @param productCode The code of the product to check in the database.
     * @return {@code True} if the code exists, {@code False} otherwise.
     * @throws DatabaseException If there is an error during the database query operation.
     */
    public static Boolean productCodeExist(String productCode) throws DatabaseException {
        String selectSQL = "SELECT COUNT(*) FROM Product WHERE product_code = ?";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, productCode);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
        return false;
    }

    public static ArrayList<String> findAllBrand() {
        String selectSQL = "SELECT DISTINCT brand_name FROM Product;";
        ArrayList<String> brandList = new ArrayList<>();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    brandList.add(resultSet.getString("brand_name"));
                }
            }
        } catch (SQLException e) {
            Logging.getLogger().warning("Error finding all brands: SQL Excepted\nStacktrace: " + e.getMessage());
            return new ArrayList<>();
        }
        return brandList;
    }  

    public static boolean checkProductStock (int productID, int quantity) throws DatabaseException {
        String selectSQL = "SELECT stock_quantity FROM Product WHERE product_id = ?;";
        int stock = -1;
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, productID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    stock = resultSet.getInt("stock_quantity");
                }
            }
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
        return stock >= quantity;
    }

}
