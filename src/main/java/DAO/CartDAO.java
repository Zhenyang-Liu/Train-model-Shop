package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import model.Cart;
import model.CartItem;

public class CartDAO {

    public static void insertCart(Cart cart) throws SQLException { 
        String insertSQL = "INSERT INTO Cart (user_id, creation_date) VALUES (?,?);";
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, cart.getUserID());
            preparedStatement.setTimestamp(2, cart.getCreationDate());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                int cartID = -1;
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        cartID = generatedKeys.getInt(1);
                    }
                }
                if (cartID > 0) {
                    insertCartItems(cartID,cart.getCartItems());
                }
            } else {
                throw new SQLException("Creating cart failed, no rows affected.");
            }
        }
    }

    public static void insertCartItems(int cartID, ArrayList<CartItem> itemList) throws SQLException { 
        String insertSQL =  "INSERT INTO Cart_Item (cart_id, product_id, quantity) VALUES (?,?,?);";
        
        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
    
            for (CartItem item : itemList) {
                preparedStatement.setInt(1, cartID);
                preparedStatement.setInt(2, item.getItem().getProductID());
                preparedStatement.setInt(3, item.getQuantity());
    
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void updateCartItem(CartItem item) throws SQLException {
        String updateSQL = "UPDATE Cart_Item SET quantity = ? WHERE cart_item_id = ?;";
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setInt(1, item.getQuantity());
            preparedStatement.setInt(2, item.getItemID());

            preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void clearCart(int cartID) throws SQLException {
        ArrayList<CartItem> itemList = findCartItems(cartID);
        for (CartItem item : itemList) {
            deleteCartItem(item.getItemID());
        }
    }

    public static void deleteCartItem(int itemID) throws SQLException {
        String deleteSQL = "DELETE FROM Cart_Item WHERE cart_item_id = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, itemID);

            int rowsAffected = preparedStatement.executeUpdate();
            
            // if (rowsAffected > 0) {
            //     System.out.println("Cart Item with ID " + itemID + " was deleted successfully.");
            // } else {
            //     System.out.println("No cart item was found in set ID " + itemID + " to delete.");
            // }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }  
    }

    public static Cart findCartByUser(int userID) throws SQLException {
        String selectSQL = "SELECT * FROM Cart WHERE user_id = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            Cart cart = new Cart(userID);

            while (resultSet.next()) {
                int cartID = resultSet.getInt("cart_id");
                Timestamp creationDate = resultSet.getTimestamp("creation_date");
                ArrayList<CartItem> itemList = findCartItems(cartID);

                cart = new Cart(cartID, userID, creationDate, itemList);
            }
            return cart;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }
    
    public static CartItem findCartItem(int itemID) throws SQLException {
        String selectSQL = "SELECT * FROM Cart_Item WHERE cart_item_id = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, itemID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                itemID = resultSet.getInt("cart_item_id");
                int productID = resultSet.getInt("product_id");
                int quantity = resultSet.getInt("quantity");

               CartItem item = new CartItem(itemID, ProductDAO.findProductByID(productID), quantity);
               return item;
            }
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public static ArrayList<CartItem> findCartItems(int cartID) throws SQLException {
        String selectSQL = "SELECT * FROM Cart_Item WHERE cart_id = ?;";
        ArrayList<CartItem> itemList = new ArrayList<CartItem>();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, cartID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int itemID = resultSet.getInt("cart_item_id");
                int productID = resultSet.getInt("product_id");
                int quantity = resultSet.getInt("quantity");
                CartItem item = new CartItem(itemID, ProductDAO.findProductByID(productID), quantity);

                itemList.add(item);
            }
            return itemList;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public static int findCartID(int userID) throws SQLException {
        String selectSQL = "SELECT cart_id FROM Cart WHERE user_id = ?;";
        int cartID = -1;

        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                cartID = resultSet.getInt("cart_id");
            }
            return cartID;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
