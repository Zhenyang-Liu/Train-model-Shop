package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import java.util.HashMap;
import java.util.ArrayList;

import model.BoxedSet;
import model.Product;
import model.BoxedSet.BoxedType;

public class BoxedSetDAO extends ProductDAO {

    public static void insertBoxedSet(BoxedSet set) throws SQLException {
        String insertSQL = "INSERT INTO BoxedSet (product_id, pack_type) VALUES (?, ?);";
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
                throw new SQLException("Creating Boxed Set failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error SQL query: " + insertSQL);
            throw e;
        }
    }

    public static void insertBoxedSetItems(BoxedSet boxedSet) throws SQLException {
        String insertSQL = "INSERT INTO BoxedSet_Item (boxed_set_id, item_id, quantity) VALUES (?, ?, ?);";
    
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

    public static void updateBoxedSetItems(BoxedSet boxedSet) throws SQLException {
        ProductDAO.updateProduct(boxedSet);
        String updateSQL = "UPDATE BoxedSet SET pack_type = ? WHERE product_id = ?;";
        
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            
            preparedStatement.setString(1, boxedSet.getBoxedType());
            preparedStatement.setInt(3, boxedSet.getProductID());
            preparedStatement.executeUpdate();
            
            deleteItem(boxedSet.getProductID());
            insertBoxedSetItems(boxedSet);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void deleteBoxedSet(int productId) throws SQLException{
        String deleteSQL = "DELETE FROM BoxedSet WHERE product_id = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, productId);
            deleteItem(productId);

            int rowsAffected = preparedStatement.executeUpdate();
            
            if (rowsAffected > 0) {
                ProductDAO.deleteProduct(productId);
                System.out.println("BoxedSet with ID " + productId + " was deleted successfully.");
            } else {
                System.out.println("No set was found with ID " + productId + " to delete.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }  
    }

    public static void deleteItem(int productId) throws SQLException{
        String deleteSQL = "DELETE FROM BoxedSet_Item WHERE boxed_set_id = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, productId);

            int rowsAffected = preparedStatement.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Item of set with ID " + productId + " was deleted successfully.");
            } else {
                System.out.println("No item was found in set ID " + productId + " to delete.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }  
    }

    public static BoxedSet findBoxedSetByID(int setID) throws SQLException {
        String selectSQL = "SELECT * FROM BoxedSet WHERE product_id = ?;";
        BoxedSet set = new BoxedSet();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, setID);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                Product newProduct = ProductDAO.findProductByID(productId);
                String newType = resultSet.getString("pack_type");
                set = new BoxedSet(newProduct,newType,findBoxedSetItem(productId));
            }
            return set;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static Map<Product, Integer> findBoxedSetItem(int setID) throws SQLException {
        String selectSQL = "SELECT * FROM BoxedSet_Item WHERE boxed_set_id = ?;";
        Map<Product, Integer> contain = new HashMap<>();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, setID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int itemID = resultSet.getInt("item_id");
                int quantity = resultSet.getInt("quantity");

                Product item = ProductDAO.findProductByID(itemID);

                switch (item.getProductType()) {
                    case "Track":
                        contain.put(TrackDAO.findTrackByID(itemID),quantity);
                    case "Controller":
                        contain.put(ControllerDAO.findControllerByID(itemID),quantity);
                    case "Locomotive":
                        contain.put(LocomotiveDAO.findLocomotiveByID(itemID),quantity);
                    case "Rolling Stock":
                        contain.put(RollingStockDAO.findProductByID(itemID),quantity);
                }
            }

            return contain;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public static ArrayList<BoxedSet> findAllBoxedSet() throws SQLException {
        String selectSQL = "SELECT * FROM BoxedSet;";
        ArrayList<BoxedSet> setList = new ArrayList<>();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                Product newProduct = ProductDAO.findProductByID(productId);
                String newType = resultSet.getString("pack_type");
                
                BoxedSet set = new BoxedSet(newProduct,newType,findBoxedSetItem(productId));
                setList.add(set);
            }
            return setList;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    public static ArrayList<BoxedSet> findBoxedSetByType(BoxedType type) throws SQLException {
        String selectSQL = "SELECT * FROM BoxedSet WHERE pack_type = ?;";
        ArrayList<BoxedSet> setList = new ArrayList<>();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, type.getType());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                Product newProduct = ProductDAO.findProductByID(productId);
                String newType = resultSet.getString("pack_type");
                
                BoxedSet set = new BoxedSet(newProduct,newType,findBoxedSetItem(productId));
                setList.add(set);
            }
            return setList;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


}
