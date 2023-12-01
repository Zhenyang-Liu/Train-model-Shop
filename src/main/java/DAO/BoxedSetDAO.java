package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import exception.ConnectionException;
import exception.DatabaseException;
import helper.Logging;
import model.BoxedSet;
import model.BoxedSet.BoxedType;
import model.Product;

public class BoxedSetDAO extends ProductDAO {

    /**
     * Inserts a new boxed set into the database.
     *
     * @param set The BoxedSet object to insert.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static void insertBoxedSet(BoxedSet set) throws DatabaseException {
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
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

    /**
     * Inserts items of a boxed set into the database.
     *
     * @param boxedSet The BoxedSet object whose items are to be inserted.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static void insertBoxedSetItems(BoxedSet boxedSet) throws DatabaseException{
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
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

    /**
     * Updates the details of an existing boxed set in the database.
     *
     * @param boxedSet The BoxedSet object to update.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static void updateBoxedSetItems(BoxedSet boxedSet) throws DatabaseException {
        ProductDAO.updateProduct(boxedSet);
        String updateSQL = "UPDATE BoxedSet SET pack_type = ? WHERE product_id = ?;";
        
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            
            preparedStatement.setString(1, boxedSet.getBoxedType());
            preparedStatement.setInt(3, boxedSet.getProductID());
            preparedStatement.executeUpdate();
            
            deleteItem(boxedSet.getProductID());
            insertBoxedSetItems(boxedSet);
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

    /**
     * Deletes a boxed set from the database.
     *
     * @param productId The product ID of the boxed set to delete.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static void deleteBoxedSet(int productId) throws DatabaseException {
        String deleteSQL = "DELETE FROM BoxedSet WHERE product_id = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, productId);
            deleteItem(productId);

            int rowsAffected = preparedStatement.executeUpdate();
            
            if (rowsAffected > 0) {
                ProductDAO.deleteProduct(productId);
                Logging.getLogger().info("BoxedSet with ID " + productId + " was deleted successfully.");
            } else {
                Logging.getLogger().warning("No set was found with ID " + productId + " to delete.");
            }
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        } 
    }

    /**
     * Deletes items of a specific boxed set from the database.
     *
     * @param productId The product ID of the items to delete.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static void deleteItem(int productId) throws DatabaseException{
        String deleteSQL = "DELETE FROM BoxedSet_Item WHERE item_id = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, productId);

            preparedStatement.executeUpdate();
            
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        } 
    }

    /**
     * Deletes records of a boxed set from the database.
     *
     * @param productId The product ID of the boxed set whose records are to be deleted.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static void deleteRecord(int productId) throws DatabaseException {
        String deleteSQL = "DELETE FROM BoxedSet_Item WHERE boxed_set_id = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, productId);

            int rowsAffected = preparedStatement.executeUpdate();
            
            if (rowsAffected > 0) {
                Logging.getLogger().info("Item of set with ID " + productId + " was deleted successfully.");
            } else {
                Logging.getLogger().warning("No item was found in set ID " + productId + " to delete.");
            }
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        } 
    }

    /**
     * Finds a boxed set by its ID.
     *
     * @param setID The ID of the boxed set to find.
     * @return The BoxedSet object found, or a new BoxedSet object if not found.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static BoxedSet findBoxedSetByID(int setID) throws DatabaseException {
        String selectSQL = "SELECT * FROM BoxedSet WHERE product_id = ?;";
        BoxedSet set = new BoxedSet();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, setID);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int productID = resultSet.getInt("product_id");
                Product newProduct = ProductDAO.findProductByID(productID);
                String newType = resultSet.getString("pack_type");
                set = new BoxedSet(newProduct,newType,findBoxedSetItem(productID));
            }
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
        return set;
    }

    /**
     * Finds the items of a specific boxed set.
     *
     * @param setID The ID of the boxed set whose items are to be found.
     * @return A Map of Product to Integer representing the items and their quantities in the boxed set.
     */
    public static Map<Product, Integer> findBoxedSetItem(int setID) {
        String selectSQL = "SELECT * FROM BoxedSet_Item WHERE boxed_set_id = ?;";
        Map<Product, Integer> contain = new HashMap<>();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, setID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int itemID = resultSet.getInt("item_id");
                int quantity = resultSet.getInt("quantity");
                Product item = null;
                try{
                    item = ProductDAO.findProductByID(itemID);
                }catch(DatabaseException e){
                    Logging.getLogger().warning("Error finding item" + itemID + " when getting items for box set " +
                        setID + "\nStacktrace: " + e.getMessage());
                    continue;
                }
                try{
                    switch (item.getProductType()) {
                    case "Track":
                        contain.put(TrackDAO.findTrackByID(itemID), quantity);
                        break;
                    case "Controller":
                        contain.put(ControllerDAO.findControllerByID(itemID), quantity);
                        break; 
                    case "Locomotive":
                        contain.put(LocomotiveDAO.findLocomotiveByID(itemID), quantity);
                        break;
                    case "Rolling Stock":
                        contain.put(RollingStockDAO.findProductByID(itemID), quantity);
                        break;
                    case "Track Pack":
                        contain.put(BoxedSetDAO.findBoxedSetByID(itemID),quantity);
                        break;
                } 
                }catch(DatabaseException e){
                    Logging.getLogger().warning("Could not find " + item.getProductType() + " for itemID " + itemID + 
                        "\nStacktrace: " + e.getMessage());
                }
                             
            }
        } catch (SQLTimeoutException e) {
            Logging.getLogger().warning("Error when finding items for box set " + setID + " SQL Timeout\nStacktrace: " + e.getMessage());
        } catch (SQLException e) {
            Logging.getLogger().warning("Error when finding items for box set " + setID + " SQL Excepted\nStacktrace: " + e.getMessage());
        }
        return contain;
    }

    /**
     * Retrieves a list of box set IDs that include a specific item.
     *
     * This method performs a database query to find all unique box set IDs (`box_set_id`)
     * where the given item ID (`item_id`) is a part of the box set. It returns an ArrayList of Integer,
     * each representing a box set ID. The method handles SQLTimeoutException and SQLException,
     * logging warnings if either occurs.
     *
     * @param itemID The item ID for which the box set IDs are to be found.
     * @return An ArrayList of Integer containing the IDs of box sets that include the given item.
     *         Returns an empty list if no box sets are found or in case of an SQL error.
     */
    public static ArrayList<Integer> findProductBelongTo(int itemID) {
        String selectSQL = "SELECT DISTINCT box_set_id FROM BoxedSet_Item WHERE item_id = ?;";
        ArrayList<Integer> idList = new ArrayList<>();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, itemID);
            
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int setId = resultSet.getInt("box_set_id");
                idList.add(setId);

            }
        } catch (SQLTimeoutException e) {
            Logging.getLogger().warning("Error getting all boxed sets: SQL Timed out\n Stacktrace: " + e.getMessage());
        } catch (SQLException e) {
            Logging.getLogger().warning("Error getting all boxed sets: SQL Exception\nStacktrace: " + e.getMessage());
        }
        return idList; 
    }

    /**
     * Retrieves all boxed sets from the database.
     *
     * @return An ArrayList of BoxedSet objects.
     */
    public static ArrayList<BoxedSet> findAllBoxedSet() {
        String selectSQL = "SELECT * FROM BoxedSet;";
        ArrayList<BoxedSet> setList = new ArrayList<>();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                Product newProduct = null;
                try{
                    newProduct = ProductDAO.findProductByID(productId);
                }catch(DatabaseException e){
                    Logging.getLogger().warning("Error when finding all boxed sets: could not find product " + productId + "\n Stacktrace: " + e.getMessage());
                    continue;
                }
                
                String newType = resultSet.getString("pack_type");
                
                BoxedSet set = new BoxedSet(newProduct,newType,findBoxedSetItem(productId));
                setList.add(set);
            }
        } catch (SQLTimeoutException e) {
            Logging.getLogger().warning("Error getting all boxed sets: SQL Timed out\n Stacktrace: " + e.getMessage());
        } catch (SQLException e) {
            Logging.getLogger().warning("Error getting all boxed sets: SQL Exception\nStacktrace: " + e.getMessage());
        }
        return setList;
    }
    
    /**
     * Retrieves boxed sets of a specific type from the database.
     *
     * @param type The type of boxed sets to retrieve.
     * @return An ArrayList of BoxedSet objects of the specified type.
     */
    public static ArrayList<BoxedSet> findBoxedSetByType(BoxedType type) {
        String selectSQL = "SELECT * FROM BoxedSet WHERE pack_type = ?;";
        ArrayList<BoxedSet> setList = new ArrayList<>();

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
                    Logging.getLogger().warning("Error when finding all boxed sets with type " + type + ": could not find product " + productId + "\n Stacktrace: " + e.getMessage());
                    continue;
                }
                String newType = resultSet.getString("pack_type");
                
                BoxedSet set = new BoxedSet(newProduct,newType,findBoxedSetItem(productId));
                setList.add(set);
            }
        } catch (SQLTimeoutException e) {
            Logging.getLogger().warning("Error getting boxed sets with type" + type + ": SQL Timed out\n Stacktrace: " + e.getMessage());
        } catch (SQLException e) {
            Logging.getLogger().warning("Error getting boxed sets with type" + type + ": SQL Exception\nStacktrace: " + e.getMessage());
        }
        return setList;
    }

}
