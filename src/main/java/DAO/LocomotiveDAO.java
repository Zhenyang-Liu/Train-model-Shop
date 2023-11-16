package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

import model.Gauge;
import model.Locomotive;
import model.Locomotive.*;
import model.Product;

public class LocomotiveDAO extends ProductDAO {

    /**
     * Inserts a new locomotive record into the database.
     *
     * @param locomotive The locomotive object to be inserted.
     * @throws SQLException If a database error occurs.
     */
    public static void insertLocomotive(Locomotive locomotive) throws SQLException {
        int productID = insertProduct(locomotive);
        String insertSQL = "INSERT INTO Locomotive (product_id, gauge, dcc_type) VALUES (?, ?, ?);";
        
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            
            preparedStatement.setInt(1, productID);
            preparedStatement.setString(2, locomotive.getGauge());
            preparedStatement.setString(3, locomotive.getDCCType().getName());

            int rowsAffected = preparedStatement.executeUpdate();

            // Check if the insert was successful
            if (rowsAffected > 0) {
                EraDAO.insertEra(productID, locomotive.getEra());
            } else {
                throw new SQLException("Creating Locomotive failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error SQL query: " + insertSQL);
            throw e;
        }

       
    }

    /**
     * Updates an existing locomotive record in the database.
     *
     * @param locomotive The locomotive object with updated information.
     * @throws SQLException If a database error occurs.
     */
    public static void updateLocomotive(Locomotive locomotive) throws SQLException{
        ProductDAO.updateProduct(locomotive);
        String updateSQL = "UPDATE Locomotive SET gauge = ?, dcc_type = ? WHERE product_id = ?;";
        
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            
            preparedStatement.setString(1, locomotive.getGauge());
            preparedStatement.setString(2, locomotive.getDCCType().toString());
            preparedStatement.setInt(3, locomotive.getProductID());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        
        EraDAO.deleteEra(locomotive.getProductID());
        EraDAO.insertEra(locomotive.getProductID(), locomotive.getEra());
    }

    /**
     * Deletes a locomotive record from the database by product ID.
     *
     * @param productId The ID of the locomotive product to be deleted.
     * @throws SQLException If a database error occurs.
     */
    public static void deleteLocomotive(int productId) throws SQLException{
        String deleteSQL = "DELETE FROM Locomotive WHERE product_id = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, productId);

            int rowsAffected = preparedStatement.executeUpdate();
            
            // Print to Test
            if (rowsAffected > 0) {
                System.out.println("Locomotive with ID " + productId + " was deleted successfully.");
            } else {
                System.out.println("No Locomotive was found with ID " + productId + " to delete.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }   
        
        EraDAO.deleteEra(productId);
        ProductDAO.deleteProduct(productId);

    }

    /**
     * Finds a locomotive record in the database by product ID.
     *
     * @param productID The ID of the locomotive product to be retrieved.
     * @return A Locomotive object representing the retrieved locomotive record | null if can't find.
     * @throws SQLException If a database error occurs.
     */
    public static Locomotive findLocomotiveByID(int productID) throws SQLException {
        String selectSQL = "SELECT * FROM Locomotive WHERE product_id = ?;";
        Locomotive locomotive = new Locomotive();
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, productID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                Product newProduct = ProductDAO.findProductByID(productId);
                String newGauge = resultSet.getString("gauge");
                String newDCCType = resultSet.getString("dcc_type");
                int[] newEra = EraDAO.findEraByID(productId);

                locomotive = new Locomotive(newProduct, newGauge, newDCCType, newEra);
            }
            return locomotive;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Retrieves a list of locomotives from the database that match the specified gauge.
     *
     * @param gauge The gauge to filter locomotives by.
     * @return An ArrayList of Locomotive objects that match the specified gauge | null if can't find.
     * @throws SQLException If a database error occurs.
     */
    public static ArrayList<Locomotive> findLocomotivesByGauge(Gauge gauge) throws SQLException{
        String selectSQL = "SELECT * FROM Locomotive WHERE gauge = ?;";
        ArrayList<Locomotive> locomotives = new ArrayList<Locomotive>();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, gauge.getName());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                Product newProduct = ProductDAO.findProductByID(productId);
                String newGauge = resultSet.getString("gauge");
                String newDCCType = resultSet.getString("dcc_type");
                int[] newEra = EraDAO.findEraByID(productId);

                Locomotive locomotive = new Locomotive(newProduct, newGauge, newDCCType, newEra);
                locomotives.add(locomotive);
            }
            return locomotives;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Retrieves a list of locomotives from the database that match the specified era(s).
     *
     * @param eraList An array of era IDs to filter locomotives by.
     * @return An ArrayList of Locomotive objects that match the specified era(s) | null if can't find.
     * @throws SQLException If a database error occurs.
     */
    public static ArrayList<Locomotive> findLocomotivesByEra(int[] eraList) throws SQLException {
        ArrayList<Locomotive> locomotives = new ArrayList<Locomotive>();
    
        try (Connection connection = DatabaseConnectionHandler.getConnection()) {
            int[] productIDs = EraDAO.findIDByEra(eraList);
    
            String selectSQL = "SELECT * FROM Locomotive WHERE product_id IN (" +
                               String.join(",", Collections.nCopies(productIDs.length, "?")) + ");";
    
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
                for (int i = 0; i < productIDs.length; i++) {
                    preparedStatement.setInt(i + 1, productIDs[i]);
                }
    
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int productId = resultSet.getInt("product_id");
                        Product newProduct = ProductDAO.findProductByID(productId);
                        String newGauge = resultSet.getString("gauge");
                        String newDCCType = resultSet.getString("dcc_type");
                        int[] newEra = EraDAO.findEraByID(productId);
    
                        Locomotive locomotive = new Locomotive(newProduct, newGauge, newDCCType, newEra);
                        locomotives.add(locomotive);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    
        return locomotives;
    }
    
    /**
     * Retrieves a list of locomotives from the database that match the specified DCC type.
     *
     * @param dccType The DCC type to filter locomotives by.
     * @return An ArrayList of Locomotive objects that match the specified DCC type | null if can't find.
     * @throws SQLException If a database error occurs.
     */
    public static ArrayList<Locomotive> findLocomotivesByDCCType(DCCType dccType) throws SQLException{
        String selectSQL = "SELECT * FROM Locomotive WHERE dcc_type = ?;";
        ArrayList<Locomotive> locomotives = new ArrayList<Locomotive>();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, dccType.getName());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                Product newProduct = ProductDAO.findProductByID(productId);
                String newGauge = resultSet.getString("gauge");
                String newDCCType = resultSet.getString("dcc_type");
                int[] newEra = EraDAO.findEraByID(productId);

                Locomotive locomotive = new Locomotive(newProduct, newGauge, newDCCType, newEra);
                locomotives.add(locomotive);
            }
            return locomotives;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Retrieves a list of all locomotives from the database.
     *
     * @return An ArrayList of all Locomotive objects in the database | null if can't find.
     * @throws SQLException If a database error occurs.
     */
    public static ArrayList<Locomotive> findAllLocomotives() throws SQLException{
        String selectSQL = "SELECT * FROM Locomotive;";
        ArrayList<Locomotive> locomotives = new ArrayList<Locomotive>();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                Product newProduct = ProductDAO.findProductByID(productId);
                String newGauge = resultSet.getString("gauge");
                String newDCCType = resultSet.getString("dcc_type");
                int[] newEra = EraDAO.findEraByID(productId);

                Locomotive locomotive = new Locomotive(newProduct, newGauge, newDCCType, newEra);
                locomotives.add(locomotive);
            }
            return locomotives;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
}
