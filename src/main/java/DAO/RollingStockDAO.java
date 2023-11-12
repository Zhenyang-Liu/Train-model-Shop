package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

import model.Gauge;
import model.RollingStock;
import model.RollingStock.RollingStockType;
import model.Product;

public class RollingStockDAO extends ProductDAO {

    /**
     * Inserts a new rollingstock record into the database.
     *
     * @param rollingStock The rollingstock object to be inserted.
     * @throws SQLException If a database error occurs.
     */
    public void insertRollingStock(RollingStock rollingStock) throws SQLException {
        int productID = insertProduct(rollingStock);
        String insertSQL = "INSERT INTO RollingStock (productID, Type, Gauge) VALUES (?, ?, ?);";
        
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            
            preparedStatement.setInt(1, productID);
            preparedStatement.setString(2, rollingStock.getRollingStockType());
            preparedStatement.setString(3, rollingStock.getGauge());

            int rowsAffected = preparedStatement.executeUpdate();

            // Check if the insert was successful
            if (rowsAffected > 0) {
                EraDAO.insertEra(productID, rollingStock.getEra());
            } else {
                throw new SQLException("Creating Rollingstock failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error SQL query: " + insertSQL);
            throw e;
        }

    }

    /**
     * Updates an existing rollingstock record in the database.
     *
     * @param rollingStock The rollingstock object with updated information.
     * @throws SQLException If a database error occurs.
     */
    public void updateRollingStock(RollingStock rollingStock) throws SQLException{
        ProductDAO.updateProduct(rollingStock);
        String updateSQL = "UPDATE RollingStock SET Type = ?, Gauge = ? WHERE productID = ?;";
        
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL, Statement.RETURN_GENERATED_KEYS)) {
            
            preparedStatement.setString(1, rollingStock.getRollingStockType());
            preparedStatement.setString(2, rollingStock.getGauge());
            preparedStatement.setInt(3, rollingStock.getProductID());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                EraDAO.deleteEra(rollingStock.getProductID());
                EraDAO.insertEra(rollingStock.getProductID(), rollingStock.getEra());
            } else {
                throw new SQLException("Creating Rollingstock failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Deletes a rollingstock record from the database by product ID.
     *
     * @param productId The ID of the rollingstock product to be deleted.
     * @throws SQLException If a database error occurs.
     */
    public void deleteRollingStock(int productId) throws SQLException{
        String deleteSQL = "DELETE FROM RollingStock WHERE ProductID = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, productId);

            int rowsAffected = preparedStatement.executeUpdate();
            
            // Print to Test
            if (rowsAffected > 0) {
                EraDAO.deleteEra(productId);
                ProductDAO.deleteProduct(productId);
                System.out.println("Locomotive with ID " + productId + " was deleted successfully.");
            } else {
                System.out.println("No Locomotive was found with ID " + productId + " to delete.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }  
    }

    /**
     * Finds a Rollingstock record in the database by product ID.
     *
     * @param productID The ID of the rollingstock product to be retrieved.
     * @return A RollingStock object representing the retrieved rollingstock record | null if can't find.
     * @throws SQLException If a database error occurs.
     */
    public RollingStock findRollingStockByID(int productID) throws SQLException {
        String selectSQL = "SELECT * FROM RollingStock WHERE productID = ?";
        RollingStock rollingStock = new RollingStock();
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, productID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("ProductID");
                Product newProduct = ProductDAO.findProductByID(productId);
                String newGauge = resultSet.getString("Gauge");
                String newType = resultSet.getString("Type");
                int[] newEra = EraDAO.findEraByID(productId);

                rollingStock = new RollingStock(newProduct, newType, newGauge, newEra);
            }
            return rollingStock;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Retrieves a list of rollingstocks from the database that match the specified gauge.
     *
     * @param gauge The gauge to filter rollingstocks by.
     * @return An ArrayList of RollingStock objects that match the specified gauge | null if can't find.
     * @throws SQLException If a database error occurs.
     */
    public ArrayList<RollingStock> findRollingStocksByGauge(Gauge gauge) throws SQLException{
        String selectSQL = "SELECT * FROM RollingStock WHERE Gauge = ?";
        ArrayList<RollingStock> rollingStocks = new ArrayList<RollingStock>();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, gauge.getName());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("ProductID");
                Product newProduct = ProductDAO.findProductByID(productId);
                String newGauge = resultSet.getString("Gauge");
                String newType = resultSet.getString("Type");
                int[] newEra = EraDAO.findEraByID(productId);

                RollingStock rollingStock = new RollingStock(newProduct, newType, newGauge, newEra);
                rollingStocks.add(rollingStock);
            }
            return rollingStocks;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Retrieves a list of rollingstocks from the database that match the specified era(s).
     *
     * @param eraList An array of era IDs to filter rollingstocks by.
     * @return An ArrayList of RollingStock objects that match the specified era(s) | null if can't find.
     * @throws SQLException If a database error occurs.
     */
    public ArrayList<RollingStock> findRollingStocksByEra(int[] eraList) throws SQLException {
        ArrayList<RollingStock> rollingStocks = new ArrayList<RollingStock>();
    
        try (Connection connection = DatabaseConnectionHandler.getConnection()) {
            int[] productIDs = EraDAO.findIDByEra(eraList);
    
            String selectSQL = "SELECT * FROM RollingStock WHERE ProductID IN (" +
                               String.join(",", Collections.nCopies(productIDs.length, "?")) + ")";
    
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
                for (int i = 0; i < productIDs.length; i++) {
                    preparedStatement.setInt(i + 1, productIDs[i]);
                }
    
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int productId = resultSet.getInt("ProductID");
                        Product newProduct = ProductDAO.findProductByID(productId);
                        String newGauge = resultSet.getString("Gauge");
                        String newType = resultSet.getString("Type");
                        int[] newEra = EraDAO.findEraByID(productId);

                        RollingStock rollingStock = new RollingStock(newProduct, newType, newGauge, newEra);
                        rollingStocks.add(rollingStock);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    
        return rollingStocks;
    }
    
    /**
     * Retrieves a list of rollingstocks from the database that match the specified type.
     *
     * @param type The type (Carriage|Wagon) to filter rollingstocks by.
     * @return An ArrayList of RollingStock objects that match the specified type | null if can't find.
     * @throws SQLException If a database error occurs.
     */
    public ArrayList<RollingStock> findRollingStocksByType(RollingStockType type) throws SQLException{
        String selectSQL = "SELECT * FROM RollingStock WHERE Type = ?";
        ArrayList<RollingStock> rollingStocks = new ArrayList<RollingStock>();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, type.getType());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("ProductID");
                Product newProduct = ProductDAO.findProductByID(productId);
                String newGauge = resultSet.getString("Gauge");
                String newType = resultSet.getString("Type");
                int[] newEra = EraDAO.findEraByID(productId);

                RollingStock rollingStock = new RollingStock(newProduct, newType, newGauge, newEra);
                rollingStocks.add(rollingStock);
            }
            return rollingStocks;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
}
