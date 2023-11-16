package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class EraDAO {
    
    /**
     * Inserts era codes associated with a product into the ProductEra table.
     *
     * @param productID The ID of the product for which era codes are being inserted.
     * @param eraList   An array of era codes to insert.
     * @throws SQLException If a database error occurs.
     */
    public static void insertEra(int productID, int[] eraList) throws SQLException {
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
            
            // print to Test
            for (int i = 0; i < batchResults.length; i++) {
                if (batchResults[i] == PreparedStatement.EXECUTE_FAILED) {
                    System.err.println("Insert operation failed for record at index " + i);
                } else {
                    System.out.println("Insert operation succeeded for record at index " + i);
                }
            }

            connection.commit();
            preparedStatement.close();        
            connection.setAutoCommit(true);
        }catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } 
    }

    /**
     * Deletes era codes associated with a product from the ProductEra table.
     *
     * @param productID The ID of the product for which era codes are being deleted.
     * @throws SQLException If a database error occurs.
     */
    public static void deleteEra(int productID) throws SQLException {
        String deleteSQL = "DELETE FROM ProductEra WHERE product_id = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, productID);
            preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }       
    }

    /**
     * Retrieves era codes associated with a product by its ID.
     *
     * @param productID The ID of the product for which era codes are being retrieved.
     * @return An array of era codes associated with the product.
     * @throws SQLException If a database error occurs.
     */
    public static int[] findEraByID(int productID) throws SQLException {
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
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        int[] era = eraList.stream().mapToInt(Integer::intValue).toArray();

        return era;
    }

    /**
     * Retrieves product IDs associated with one or more era codes.
     *
     * @param eraList An array of era codes for which to retrieve associated product IDs.
     * @return An array of product IDs associated with the era codes.
     * @throws SQLException If a database error occurs.
     */
    public static int[] findIDByEra(int[] eraList) throws SQLException {
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
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        int[] idList = productIDs.stream().mapToInt(Integer::intValue).toArray();

        return idList;
    }
}
