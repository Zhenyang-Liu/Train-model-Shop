package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import exception.ConnectionException;
import exception.DatabaseException;
import helper.Logging;

public class EraDAO {
    
    /**
     * Inserts era codes associated with a product into the ProductEra table.
     *
     * @param productID The ID of the product for which era codes are being inserted.
     * @param eraList   An array of era codes to insert.
     * @throws DatabaseException If a database error occurs.
     */
    public static void insertEra(int productID, int[] eraList) throws DatabaseException {
        String insertSQL = "INSERT INTO ProductEra (era_code, product_id) VALUES (?, ?);";
        try (Connection connection = DatabaseConnectionHandler.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            connection.setAutoCommit(false);

            for (int era: eraList) {
                preparedStatement.setInt(1, era);
                preparedStatement.setInt(2, productID);
                preparedStatement.addBatch();
            }

            int[] batchResults = preparedStatement.executeBatch();
            
            for (int i = 0; i < batchResults.length; i++) {
                if (batchResults[i] == PreparedStatement.EXECUTE_FAILED) {
                    Logging.getLogger().warning("Insert operation failed for record at index " + i);
                } else {
                    Logging.getLogger().info("Insert operation succeeded for record at index " + i);
                }
            }

            connection.commit();
            preparedStatement.close();        
            connection.setAutoCommit(true);
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

    /**
     * Deletes era codes associated with a product from the ProductEra table.
     *
     * @param productID The ID of the product for which era codes are being deleted.
     * @throws DatabaseException If a database error occurs.
     */
    public static void deleteEra(int productID) throws DatabaseException {
        String deleteSQL = "DELETE FROM ProductEra WHERE product_id = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, productID);
            preparedStatement.executeUpdate();
            
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }     
    }

    /**
     * Retrieves era codes associated with a product by its ID.
     *
     * @param productID The ID of the product for which era codes are being retrieved.
     * @return An array of era codes associated with the product.
     * @throws DatabaseException If a database error occurs.
     */
    public static int[] findEraByID(int productID) {
        String selectSQL = "SELECT era_code FROM ProductEra WHERE product_id = ?;";
        List<Integer> eraList = new ArrayList<>();
        
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, productID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int eraCode = resultSet.getInt("era_code");
                    eraList.add(eraCode);
                }
            }
        } catch (SQLTimeoutException e) {
            Logging.getLogger().warning("Error when finding eras for product: " + productID + 
                "SQL Timed out\n Stacktrace: " + e.getMessage());
        } catch (SQLException e) {
            Logging.getLogger().warning("Error when finding eras for product: " + productID + 
                "SQL Excepted :0\nStacktrace: " + e.getMessage());
        }

        int[] era = eraList.stream().mapToInt(Integer::intValue).toArray();
        return era;
    }

    /**
     * Retrieves product IDs associated with one or more era codes.
     *
     * @param eraList An array of era codes for which to retrieve associated product IDs.
     * @return An array of product IDs associated with the era codes.
     * @throws DatabaseException If a database error occurs.
     */
    public static int[] findIDByEra(int[] eraList) {
        String selectSQL = "SELECT DISTINCT product_id FROM ProductEra WHERE era_code IN (" + 
                                      String.join(",", Collections.nCopies(eraList.length, "?")) + ")";
        List<Integer> productIDs = new ArrayList<>();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);) {

            for (int i = 0; i < eraList.length; i++) {
                preparedStatement.setInt(i + 1, eraList[i]);
            }
    
            try (ResultSet productIDsResult = preparedStatement.executeQuery()) {
                while (productIDsResult.next()) {
                    int productID = productIDsResult.getInt("product_id");
                    productIDs.add(productID);
                }
            }
        } catch (SQLTimeoutException e) {
            Logging.getLogger().warning("Error when finding products for eras:" + 
                "SQL Timed out\n Stacktrace: " + e.getMessage());
        } catch (SQLException e) {
            Logging.getLogger().warning("Error when finding products for eras:" + 
                "SQL Excepted :0\nStacktrace: " + e.getMessage());
        }

        int[] idList = productIDs.stream().mapToInt(Integer::intValue).toArray();
        return idList;
    }

    /**
     * Retrieves a list of all era descriptions in the database, sorted by era code.
     *
     * This method queries the Era table and extracts the description of each era,
     * returning a list of these descriptions ordered by era code.
     * It logs a warning and throws an exception in case of SQL timeouts or other SQL exceptions.
     *
     * @return An ArrayList of Strings containing descriptions of each era.
     * @throws DatabaseException If a database error occurs.
     */
    public static ArrayList<String> findAllEraDescription() throws DatabaseException {
        String selectSQL = "SELECT description FROM Era ORDER BY era_code ASC;";
        ArrayList<String> eraDescriptions = new ArrayList<>();
    
        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String description = resultSet.getString("description");
                    eraDescriptions.add(description);
                }
            }
        } catch (SQLTimeoutException e) {
            Logging.getLogger().warning("Error when finding era descriptions: SQL Timed out\n Stacktrace: " + e.getMessage());
            throw new DatabaseException("SQL Timeout Exception occurred", e);
        } catch (SQLException e) {
            Logging.getLogger().warning("Error when finding era descriptions: SQL Exception\nStacktrace: " + e.getMessage());
            throw new DatabaseException("SQL Exception occurred", e);
        }
    
        return eraDescriptions;
    }

    /**
     * Retrieves the description of a specific era based on its era code.
     *
     * This method queries the Era table for the description corresponding to the provided era code.
     * It logs a warning and throws an exception in case of SQL timeouts or other SQL exceptions.
     *
     * @param eraCode The era code for which the description is being retrieved.
     * @return A String containing the description of the specified era, or an empty string if not found.
     * @throws DatabaseException If a database error occurs.
     */
    public static String findDescriptionByCode(int eraCode) throws DatabaseException {
        String selectSQL = "SELECT description FROM Era WHERE era_code = ?;";
        String eraDescription = "";
    
        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
    
            preparedStatement.setInt(1, eraCode);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    eraDescription = resultSet.getString("description");
                }
            }
        } catch (SQLTimeoutException e) {
            Logging.getLogger().warning("Error when finding era description: SQL Timed out\n Stacktrace: " + e.getMessage());
            throw new DatabaseException("SQL Timeout Exception occurred", e);
        } catch (SQLException e) {
            Logging.getLogger().warning("Error when finding era description: SQL Exception\nStacktrace: " + e.getMessage());
            throw new DatabaseException("SQL Exception occurred", e);
        }
        return eraDescription;
    }
    
}
