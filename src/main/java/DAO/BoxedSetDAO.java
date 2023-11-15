package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import model.BoxedSet;
import model.Product;

public class BoxedSetDAO extends ProductDAO {

    public static void insertBoxedSet(BoxedSet set) throws SQLException {
        String insertSQL = "INSERT INTO BoxedSet (ProductID, PackType) VALUES (?, ?);";
        int productID = insertProduct(set);
        
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            
            preparedStatement.setInt(1, productID);
            preparedStatement.setString(2, set.getBoxedType());

            int rowsAffected = preparedStatement.executeUpdate();
            
            if (rowsAffected > 0) {
                set.setProductID(productID);
                insertBoxedSetItems(set);
            } else {
                throw new SQLException("Creating Locomotive failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error SQL query: " + insertSQL);
            throw e;
        }
    }

    public static void insertBoxedSetItems(BoxedSet boxedSet) throws SQLException {
        String insertSQL = "INSERT INTO BoxedSet_Item (BoxedSetID, ItemID, Quantity) VALUES (?, ?, ?)";
    
        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
    
            for (Map.Entry<Product, Integer> entry : boxedSet.getContain().entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
    
                preparedStatement.setInt(1, boxedSet.getProductID());
                preparedStatement.setInt(2, product.getProductID());
                preparedStatement.setInt(3, quantity);
    
                preparedStatement.addBatch();
            }
    
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // public static BoxedSet findBoxedSetByID(int setID) throws SQLException {
    //     String selectSQL = "SELECT * FROM BoxedSet WHERE productID = ?;";
    //     try (Connection connection = DatabaseConnectionHandler.getConnection();
    //          PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
    
    //         for (Map.Entry<Product, Integer> entry : boxedSet.getContain().entrySet()) {
    //             Product product = entry.getKey();
    //             int quantity = entry.getValue();
    
    //             preparedStatement.setInt(1, boxedSet.getProductID());
    //             preparedStatement.setInt(2, product.getProductID());
    //             preparedStatement.setInt(3, quantity);
    
    //             preparedStatement.addBatch();
    //         }
    
    //         preparedStatement.executeBatch();
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //         throw e;
    //     }
    // }

    // public static Map<Product, Integer> findBoxedSetItem(int setID) throws SQLException {
    //     String selectSQL = "SELECT * FROM BoxedSet_Item WHERE BoxedSetID = ?;";

    //     try (Connection connection = DatabaseConnectionHandler.getConnection();
    //          PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //         throw e;
    //     }

    // }
    


}
