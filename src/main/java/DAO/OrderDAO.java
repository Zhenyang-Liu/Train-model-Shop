package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import exception.ConnectionException;
import exception.DatabaseException;
import helper.Logging;
import model.Order;
import model.Product;

public class OrderDAO {

    /**
     * Inserts a new order into the database.
     *
     * This method adds a new order record to the database with details including user ID, delivery address ID, 
     * creation and update times, total cost, status, and bank detail state. 
     * It also inserts associated order line items using insertOrderLine.
     *
     * @param order The Order object containing the details to be inserted.
     * @return The ID of the newly inserted order, or -1 if the insertion fails.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static int insertOrder(Order order) throws DatabaseException { 
        String insertSQL = "INSERT INTO Orders "
            +"(user_id, create_time, update_time, total_cost, status, bank_detail_state) "
            + "VALUES (?,?,?,?,?,?);";
        int orderID = -1;

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, order.getUserID());
            preparedStatement.setTimestamp(2, order.getCreateTime());
            preparedStatement.setTimestamp(3, order.getUpdateTime());
            preparedStatement.setDouble(4, order.getTotalCost());
            preparedStatement.setString(5, order.getStatus().getStatus());
            if (order.getBankDetailState()){
                preparedStatement.setInt(6, 1);
            } else {
                preparedStatement.setInt(6, 0);
            }

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        orderID = generatedKeys.getInt(1);
                    }
                }
                if (orderID > 0) {
                    insertOrderLine(orderID,order.getOrderItems());
                    return orderID;
                }
            } else {
                throw new SQLException("Creating cart failed, no rows affected.");
            }
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
        return orderID;
    }

    /**
     * Inserts order line items associated with a specific order.
     *
     * This method adds each item in the order to the Order_Line table in the database.
     *
     * @param orderID The ID of the order.
     * @param orderList A Map of Product to Integer representing the order items and their quantities.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static void insertOrderLine(int orderID, Map<Product, Integer> orderList) throws DatabaseException { 
        String insertSQL =  "INSERT INTO Order_Line (order_id, product_id, quantity, line_cost) VALUES (?,?,?,?);";
        
        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
    
            for (Map.Entry<Product, Integer> entry : orderList.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                double lineCost = quantity * product.getRetailPrice();
    
                preparedStatement.setInt(1, orderID);
                preparedStatement.setInt(2, product.getProductID());
                preparedStatement.setInt(3, quantity);
                preparedStatement.setDouble(4,lineCost);

                preparedStatement.addBatch();
            }
    
            preparedStatement.executeBatch();

        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

    /**
     * Updates the details of an existing order in the database.
     *
     * This method updates the details of an order including delivery address ID, update time, status, 
     * and bank detail state based on the given Order object.
     *
     * @param order The Order object containing the updated details.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static void updateOrder(Order order) throws DatabaseException {
        String updateSQL = "UPDATE Orders SET "
           + "delivery_address_id = ?, update_time = ?, status = ?, bank_detail_state = ? "
           + "WHERE order_id = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setInt(1,order.getAddressID());
            preparedStatement.setTimestamp(2, order.getUpdateTime());
            preparedStatement.setString(3, order.getStatus().getStatus());
            if (order.getBankDetailState()){
                preparedStatement.setInt(4, 1);
            } else {
                preparedStatement.setInt(4, 0);
            }
            preparedStatement.setInt(5, order.getOrderID());

            preparedStatement.executeUpdate();

        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

    public static void cancelOrder(Order order) throws DatabaseException {
        String updateSQL = "UPDATE Orders SET "
           + "update_time = ?, status = ?, reason = ? "
           + "WHERE order_id = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setTimestamp(1, order.getUpdateTime());
            preparedStatement.setString(2, "Cancelled");
            preparedStatement.setString(3, order.getReason());
            preparedStatement.setInt(4, order.getOrderID());

            preparedStatement.executeUpdate();

        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

    /**
     * Retrieves an order from the database by its ID.
     *
     * This method queries the database for an order matching the provided ID and returns the order details.
     *
     * @param orderID The ID of the order to be retrieved.
     * @return An Order object containing the details of the order, or null if not found.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static Order findOrderByID(int orderID) throws DatabaseException {
        String selectSQL = "SELECT * FROM Orders WHERE order_id = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, orderID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                orderID = resultSet.getInt("order_id");
                int userID = resultSet.getInt("user_id");
                int addressID = resultSet.getInt("delivery_address_id");
                Timestamp createTime = resultSet.getTimestamp("create_time");
                Timestamp updateTime = resultSet.getTimestamp("update_time");
                double total_cost = resultSet.getDouble("total_cost");
                String status = resultSet.getString("status");
                Map<Product,Integer> itemList = findOrderItems(orderID);
                boolean validBankDetail = resultSet.getInt("bank_detail_state") == 1;
                String reason = resultSet.getString("reason");

                return new Order(orderID, userID, addressID, createTime, updateTime, total_cost, status,itemList,validBankDetail,reason);
            }

        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
        return null;
    }

    public static ArrayList<Order> findOrderByStatus(String statusString) throws DatabaseException {
        String selectSQL = "SELECT * FROM Orders WHERE status = ?;";
        ArrayList<Order> orderList = new ArrayList<>();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, statusString);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int orderID = resultSet.getInt("order_id");
                int userID = resultSet.getInt("user_id");
                int addressID = resultSet.getInt("delivery_address_id");
                Timestamp createTime = resultSet.getTimestamp("create_time");
                Timestamp updateTime = resultSet.getTimestamp("update_time");
                double total_cost = resultSet.getDouble("total_cost");
                String status = resultSet.getString("status");
                Map<Product,Integer> itemList = findOrderItems(orderID);
                boolean validBankDetail = resultSet.getInt("bank_detail_state") == 1;
                String reason = resultSet.getString("reason");

                Order order = new Order(orderID, userID, addressID, createTime, updateTime, total_cost, status,itemList,validBankDetail,reason);
                orderList.add(order);
            }

        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
        return orderList;
    }
    
    /**
     * Retrieves the items of a specific order.
     *
     * This method queries the database for items associated with a specific order and returns a map of Product to Integer.
     *
     * @param orderID The ID of the order whose items are to be retrieved.
     * @return A Map of Product to Integer representing the items and their quantities in the order.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static Map<Product,Integer> findOrderItems(int orderID) {
        String selectSQL = "SELECT * FROM Order_Line WHERE order_id = ?;";
        Map<Product,Integer> itemList = new HashMap<>();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, orderID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productID = resultSet.getInt("product_id");
                int quantity = resultSet.getInt("quantity");

                try{
                    itemList.put(ProductDAO.findProductByID(productID),quantity);
                }catch(DatabaseException e){
                    Logging.getLogger().warning("Error when finding order items for order no. " + orderID + 
                        " Could not find product " + productID + "\nStacktrace: " + e.getMessage());
                }
            }
            

        } catch (SQLTimeoutException e){
            Logging.getLogger().warning("Error when finding order items for order no. " + orderID + 
                        " SQL Timed out\nStacktrace: " + e.getMessage());
        } catch (SQLException e) {
            Logging.getLogger().warning("Error when finding order items for order no. " + orderID + 
                        " SQL Excepted\nStacktrace: " + e.getMessage());
        }
        return itemList;
    }

    /**
     * Retrieves all orders from the database.
     *
     * This method queries the database for all orders (excluding "Pending") and returns a list of Order objects containing their details.
     *
     * @return An ArrayList of Order objects representing all orders in the database.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static ArrayList<Order> findAllOrder()  {
        String selectSQL = "SELECT * FROM Orders;";
        ArrayList<Order> orderList = new ArrayList<>();
    
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement checkStatement = connection.prepareStatement(selectSQL);
            ResultSet resultSet = checkStatement.executeQuery()) {

            while (resultSet.next()) {
                int orderID = resultSet.getInt("order_id");
                int userID = resultSet.getInt("user_id");
                int addressID = resultSet.getInt("delivery_address_id");
                Timestamp createTime = resultSet.getTimestamp("create_time");
                Timestamp updateTime = resultSet.getTimestamp("update_time");
                double total_cost = resultSet.getDouble("total_cost");
                String status = resultSet.getString("status");
                Map<Product,Integer> itemList = findOrderItems(orderID);
                boolean validBankDetail = resultSet.getInt("bank_detail_state") == 1;
                String reason = resultSet.getString("reason");

                Order order = new Order(orderID, userID, addressID, createTime, updateTime, total_cost, status,itemList,validBankDetail,reason);
                if (!status.equals("Pending"))
                    orderList.add(order);
            }
        } catch (SQLTimeoutException e){
            Logging.getLogger().warning("Error when finding all orders: SQL Timed out\nStacktrace: " + e.getMessage());
        } catch (SQLException e) {
            Logging.getLogger().warning("Error when finding all orders: SQL Excepted\nStacktrace: " + e.getMessage());
        }
        return orderList;
    }

    public static ArrayList<Order> findUserAllOrder(int userID)  {
        String selectSQL = "SELECT * FROM Orders WHERE user_id = ?;";
        ArrayList<Order> orderList = new ArrayList<>();
    
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement checkStatement = connection.prepareStatement(selectSQL)) {

            checkStatement.setInt(1, userID);
            ResultSet resultSet = checkStatement.executeQuery();

            while (resultSet.next()) {
                int orderID = resultSet.getInt("order_id");
                userID = resultSet.getInt("user_id");
                int addressID = resultSet.getInt("delivery_address_id");
                Timestamp createTime = resultSet.getTimestamp("create_time");
                Timestamp updateTime = resultSet.getTimestamp("update_time");
                double total_cost = resultSet.getDouble("total_cost");
                String status = resultSet.getString("status");
                Map<Product,Integer> itemList = findOrderItems(orderID);
                boolean validBankDetail = resultSet.getInt("bank_detail_state") == 1;
                String reason = resultSet.getString("reason");

                Order order = new Order(orderID, userID, addressID, createTime, updateTime, total_cost, status,itemList,validBankDetail,reason);
                
                orderList.add(order);
            }
        } catch (SQLTimeoutException e){
            Logging.getLogger().warning("Error when finding all orders: SQL Timed out\nStacktrace: " + e.getMessage());
        } catch (SQLException e) {
            Logging.getLogger().warning("Error when finding all orders: SQL Excepted\nStacktrace: " + e.getMessage());
        }
        return orderList;
    }
}