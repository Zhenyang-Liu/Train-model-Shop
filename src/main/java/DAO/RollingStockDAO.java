package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.ArrayList;
import java.util.Collections;

import exception.ConnectionException;
import exception.DatabaseException;
import helper.Logging;
import model.Gauge;
import model.RollingStock;
import model.RollingStock.RollingStockType;
import model.Product;

public class RollingStockDAO extends ProductDAO {

    /**
     * Inserts a new rollingstock record into the database.
     *
     * @param rollingStock The rollingstock object to be inserted.
     * @throws DatabaseException If a database error occurs.
     */
    public static void insertRollingStock(RollingStock rollingStock) throws DatabaseException {
        int productID = insertProduct(rollingStock);
        String insertSQL = "INSERT INTO RollingStock (product_id, type, gauge) VALUES (?, ?, ?);";
        
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            
            preparedStatement.setInt(1, productID);
            preparedStatement.setString(2, rollingStock.getRollingStockType());
            preparedStatement.setString(3, rollingStock.getGauge());

            int rowsAffected = preparedStatement.executeUpdate();

            // Check if the insert was successful
            if (rowsAffected > 0) {
                EraDAO.insertEra(productID, rollingStock.getEra());
            } else {
                throw new DatabaseException("Creating Rollingstock failed, no rows affected.");
            }
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }

    }

    /**
     * Updates an existing rollingstock record in the database.
     *
     * @param rollingStock The rollingstock object with updated information.
     * @throws DatabaseException If a database error occurs.
     */
    public static void updateRollingStock(RollingStock rollingStock) throws DatabaseException{
        ProductDAO.updateProduct(rollingStock);
        String updateSQL = "UPDATE RollingStock SET type = ?, gauge = ? WHERE product_id = ?;";
        
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            
            preparedStatement.setString(1, rollingStock.getRollingStockType());
            preparedStatement.setString(2, rollingStock.getGauge());
            preparedStatement.setInt(3, rollingStock.getProductID());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                EraDAO.deleteEra(rollingStock.getProductID());
                EraDAO.insertEra(rollingStock.getProductID(), rollingStock.getEra());
            } else {
                throw new DatabaseException("Creating Rollingstock failed, no rows affected.");
            }
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

    /**
     * Deletes a rollingstock record from the database by product ID.
     *
     * @param productId The ID of the rollingstock product to be deleted.
     * @throws DatabaseException If a database error occurs.
     */
    public static void deleteRollingStock(int productId) throws DatabaseException{
        String deleteSQL = "DELETE FROM RollingStock WHERE product_id = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, productId);

            int rowsAffected = preparedStatement.executeUpdate();
            
            // Print to Test
            if (rowsAffected > 0) {
                EraDAO.deleteEra(productId);
                ProductDAO.deleteProduct(productId);
            }
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

    /**
     * Finds a Rollingstock record in the database by product ID.
     *
     * @param productID The ID of the rollingstock product to be retrieved.
     * @return A RollingStock object representing the retrieved rollingstock record | null if can't find.
     * @throws DatabaseException If a database error occurs.
     */
    public static RollingStock findRollingStockByID(int productID) throws DatabaseException {
        String selectSQL = "SELECT * FROM RollingStock WHERE product_id = ?;";
        RollingStock rollingStock = new RollingStock();
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, productID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                Product newProduct = ProductDAO.findProductByID(productId);
                String newGauge = resultSet.getString("gauge");
                String newType = resultSet.getString("type");
                int[] newEra = EraDAO.findEraByID(productId);

                rollingStock = new RollingStock(newProduct, newType, newGauge, newEra);
            }
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
        return rollingStock;
    }

    /**
     * Retrieves a list of rollingstocks from the database that match the specified gauge.
     *
     * @param gauge The gauge to filter rollingstocks by.
     * @return An ArrayList of RollingStock objects that match the specified gauge | null if can't find.
     * @throws DatabaseException If a database error occurs.
     */
    public static ArrayList<RollingStock> findRollingStocksByGauge(Gauge gauge){
        String selectSQL = "SELECT * FROM RollingStock WHERE gauge = ?;";
        ArrayList<RollingStock> rollingStocks = new ArrayList<RollingStock>();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, gauge.getName());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                Product newProduct = null;
                try{
                    newProduct = ProductDAO.findProductByID(productId);
                }catch(DatabaseException e){
                    Logging.getLogger().warning("Error when finding all rolling stocks for gauge " + gauge + ": could not find product " 
                            + productId + "\n Stacktrace: " + e.getMessage());
                    continue;
                }
                String newGauge = resultSet.getString("gauge");
                String newType = resultSet.getString("type");
                int[] newEra = EraDAO.findEraByID(productId);

                RollingStock rollingStock = new RollingStock(newProduct, newType, newGauge, newEra);
                rollingStocks.add(rollingStock);
            }
        } catch (SQLTimeoutException e){
            Logging.getLogger().warning("Error when finding all rolling stocks: SQL Timeout\nStacktrace: " + e.getMessage());;
        } catch (SQLException e) {
            Logging.getLogger().warning("Error when finding all rolling stocks: SQL Exception\nStacktrace: " + e.getMessage());;
        }
        return rollingStocks;
    }
    
    /**
     * Retrieves a list of rollingstocks from the database that match the specified era(s).
     *
     * @param eraList An array of era IDs to filter rollingstocks by.
     * @return An ArrayList of RollingStock objects that match the specified era(s) | null if can't find.
     * @throws DatabaseException If a database error occurs.
     */
    public static ArrayList<RollingStock> findRollingStocksByEra(int[] eraList) {
        ArrayList<RollingStock> rollingStocks = new ArrayList<RollingStock>();
    
        try (Connection connection = DatabaseConnectionHandler.getConnection()) {
            int[] productIDs = EraDAO.findIDByEra(eraList);
    
            String selectSQL = "SELECT * FROM RollingStock WHERE product_id IN (" +
                               String.join(",", Collections.nCopies(productIDs.length, "?")) + ");";
    
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
                for (int i = 0; i < productIDs.length; i++) {
                    preparedStatement.setInt(i + 1, productIDs[i]);
                }
    
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int productId = resultSet.getInt("product_id");
                        Product newProduct = null;
                        try{
                            newProduct = ProductDAO.findProductByID(productId);
                        }catch(DatabaseException e){
                            Logging.getLogger().warning("Error when finding all rolling stocks for era " + eraList + ": could not find product " 
                                    + productId + "\n Stacktrace: " + e.getMessage());
                            continue;
                        }
                        String newGauge = resultSet.getString("gauge");
                        String newType = resultSet.getString("type");
                        int[] newEra = EraDAO.findEraByID(productId);

                        RollingStock rollingStock = new RollingStock(newProduct, newType, newGauge, newEra);
                        rollingStocks.add(rollingStock);
                    }
                }
            }
        } catch (SQLTimeoutException e){
            Logging.getLogger().warning("Error when finding all rolling stocks: SQL Timeout\nStacktrace: " + e.getMessage());;
        } catch (SQLException e) {
            Logging.getLogger().warning("Error when finding all rolling stocks: SQL Exception\nStacktrace: " + e.getMessage());;
        }
        return rollingStocks;
    }
    
    /**
     * Retrieves a list of rollingstocks from the database that match the specified type.
     *
     * @param type The type (Carriage|Wagon) to filter rollingstocks by.
     * @return An ArrayList of RollingStock objects that match the specified type | null if can't find.
     * @throws DatabaseException If a database error occurs.
     */
    public static ArrayList<RollingStock> findRollingStocksByType(RollingStockType type){
        String selectSQL = "SELECT * FROM RollingStock WHERE type = ?;";
        ArrayList<RollingStock> rollingStocks = new ArrayList<RollingStock>();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, type.getType());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                Product newProduct = null;
                try{
                    newProduct = ProductDAO.findProductByID(productId);
                }catch(DatabaseException e){
                    Logging.getLogger().warning("Error when finding all rolling stocks for stock type " + type + ": could not find product " 
                            + productId + "\n Stacktrace: " + e.getMessage());
                    continue;
                }
                String newGauge = resultSet.getString("gauge");
                String newType = resultSet.getString("type");
                int[] newEra = EraDAO.findEraByID(productId);

                RollingStock rollingStock = new RollingStock(newProduct, newType, newGauge, newEra);
                rollingStocks.add(rollingStock);
            }
        } catch (SQLTimeoutException e){
            Logging.getLogger().warning("Error when finding all rolling stocks: SQL Timeout\nStacktrace: " + e.getMessage());;
        } catch (SQLException e) {
            Logging.getLogger().warning("Error when finding all rolling stocks: SQL Exception\nStacktrace: " + e.getMessage());;
        }
        return rollingStocks;
    }

    /**
     * Retrieves a list of all rollingstocks from the database.
     *
     * @return An ArrayList of all RollingStock objects in the database | null if can't find.
     * @throws DatabaseException If a database error occurs.
     */
    public static ArrayList<RollingStock> findAllRollingStocks() {
        String selectSQL = "SELECT * FROM RollingStock;";
        ArrayList<RollingStock> rollingStocks = new ArrayList<RollingStock>();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                Product newProduct = null;
                try{
                    newProduct = ProductDAO.findProductByID(productId);
                }catch(DatabaseException e){
                    Logging.getLogger().warning("Error when finding all rolling stocks\n Stacktrace: " + e.getMessage());
                    continue;
                }
                String newGauge = resultSet.getString("gauge");
                String newType = resultSet.getString("type");
                int[] newEra = EraDAO.findEraByID(productId);

                RollingStock rollingStock = new RollingStock(newProduct, newType, newGauge, newEra);
                rollingStocks.add(rollingStock);
            }
        } catch (SQLTimeoutException e){
            Logging.getLogger().warning("Error when finding all rolling stocks: SQL Timeout\nStacktrace: " + e.getMessage());;
        } catch (SQLException e) {
            Logging.getLogger().warning("Error when finding all rolling stocks: SQL Exception\nStacktrace: " + e.getMessage());;
        }
        return rollingStocks;
    }
    
}
