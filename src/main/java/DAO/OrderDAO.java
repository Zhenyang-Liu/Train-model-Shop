package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import exception.ConnectionException;
import exception.DatabaseException;
import model.Order;
import model.Product;

public class OrderDAO {

    public static void insertOrder(Order order) throws DatabaseException { 
        String insertSQL = "INSERT INTO Orders (user_id, delivery_address_id, create_time, update_time, total_cost, status) "
            + "VALUES (?,?,?,?,?,?);";
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, order.getUserID());
            preparedStatement.setInt(2, order.getAddressID());
            preparedStatement.setTimestamp(3, order.getCreateTime());
            preparedStatement.setTimestamp(4, order.getUpdateTime());
            preparedStatement.setDouble(5, order.getTotalCost());
            preparedStatement.setString(6, order.getStatus().getStatus());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                int orderID = -1;
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        orderID = generatedKeys.getInt(1);
                    }
                }
                if (orderID > 0) {
                    insertOrderLine(orderID,order.getOrderItems());
                }
            } else {
                throw new SQLException("Creating cart failed, no rows affected.");
            }
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

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

    public static void updateOrderStatus(int orderId, String newStatus) throws DatabaseException {
        String updateSQL = "UPDATE Orders SET status = ? WHERE order_id = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, orderId);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating order failed, no rows affected.");
            }
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }


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

                return new Order(orderID, userID, addressID, createTime, updateTime, total_cost, status,itemList);
            }

        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
        return null;
    }
    
    public static Map<Product,Integer> findOrderItems(int orderID) throws DatabaseException {
        String selectSQL = "SELECT * FROM Order_Line WHERE order_id = ?;";
        Map<Product,Integer> itemList = new HashMap<>();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, orderID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productID = resultSet.getInt("product_id");
                int quantity = resultSet.getInt("quantity");

                itemList.put(ProductDAO.findProductByID(productID),quantity);
            }
            

        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
        return itemList;
    }
}