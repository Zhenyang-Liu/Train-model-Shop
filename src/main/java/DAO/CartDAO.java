package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import exception.*;
import model.Cart;
import model.CartItem;

/**
 * This class provides Data Access Object (DAO) methods for handling database operations
 * related to the Cart entity. It includes functionalities such as inserting, updating,
 * and retrieving cart and cart item information from the database.
 */
public class CartDAO {

    /**
     * Inserts a new cart into the database.
     *
     * @param cart The Cart object to be inserted.
     * @throws DatabaseException if there is a problem executing the query.
     * @throws ConnectionException if there is a problem to connecting the database.
     */
    public static void insertCart(Cart cart) throws DatabaseException { 
        String insertSQL = "INSERT INTO Cart (user_id, creation_date) VALUES (?,?);";
        
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, cart.getUserID());
            preparedStatement.setTimestamp(2, cart.getCreationDate());
    
            int rowsAffected = preparedStatement.executeUpdate();
    
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int cartID = generatedKeys.getInt(1);
                        insertCartItems(cartID, cart.getCartItems());
                    }
                }
            } else {
                throw new ActionFailedException("Creating cart failed, no rows affected.");
            }
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

    /**
     * Inserts a list of cart items associated with a specific cart into the database.
     *
     * @param cartID   The ID of the cart to which the items belong.
     * @param itemList The list of CartItem objects to be inserted.
     * @throws DatabaseException if there is a problem executing the query.
     * @throws ConnectionException if there is a problem to connecting the database.
     */
    public static void insertCartItems(int cartID, ArrayList<CartItem> itemList) throws DatabaseException { 
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

        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

    /**
     * Updates the quantity of a specific cart item in the database.
     *
     * @param item The CartItem object to be updated.
     * @throws DatabaseException if there is a problem executing the query.
     * @throws ConnectionException if there is a problem to connecting the database.
     */
    public static void updateCartItem(CartItem item) throws DatabaseException {
        String updateSQL = "UPDATE Cart_Item SET quantity = ? WHERE cart_item_id = ?;";
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setInt(1, item.getQuantity());
            preparedStatement.setInt(2, item.getItemID());

            preparedStatement.executeUpdate();
            
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

    /**
     * Clears all items from a specific cart in the database.
     *
     * @param cartID The ID of the cart to be cleared.
     * @throws DatabaseException if there is a problem executing the query.
     * @throws ConnectionException if there is a problem to connecting the database.
     */
    public static void clearCart(int cartID) throws DatabaseException {
        ArrayList<CartItem> itemList = findCartItems(cartID);
        for (CartItem item : itemList) {
            deleteCartItem(item.getItemID());
        }
    }
    
    /**
     * Deletes a specific cart item from the database.
     *
     * @param itemID The ID of the cart item to be deleted.
     * @throws DatabaseException if there is a problem executing the query.
     * @throws ConnectionException if there is a problem to connecting the database.
     */
    public static void deleteCartItem(int itemID) throws DatabaseException {
        String deleteSQL = "DELETE FROM Cart_Item WHERE cart_item_id = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, itemID);

            preparedStatement.executeUpdate();
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

    /**
     * Retrieves a Cart object based on its ID.
     *
     * @param cartID The ID of the cart to be retrieved.
     * @return A Cart object representing the cart found in the database, or a Cart object
     *         with a user ID of -1 if no cart is found.
     * @throws DatabaseException if there is a problem executing the query.
     * @throws ConnectionException if there is a problem to connecting the database.
     */
    public static Cart findCartByID(int cartID) throws DatabaseException {
        String selectSQL = "SELECT * FROM Cart WHERE cart_id = ?;";
        Cart cart = new Cart(-1);
        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, cartID);
            ResultSet resultSet = preparedStatement.executeQuery();
            

            while (resultSet.next()) {
                cartID = resultSet.getInt("cart_id");
                int userID = resultSet.getInt("user_id");
                Timestamp creationDate = resultSet.getTimestamp("creation_date");
                ArrayList<CartItem> itemList = findCartItems(cartID);
                cart = new Cart(cartID, userID, creationDate, itemList);
            }
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
        return cart;
    }

    /**
     * Retrieves a Cart object for a specific user.
     *
     * @param userID The ID of the user whose cart is to be retrieved.
     * @return A Cart object containing the cart details.
     * @throws DatabaseException if there is a problem executing the query.
     * @throws ConnectionException if there is a problem to connecting the database.
     */
    public static Cart findCartByUser(int userID) throws DatabaseException {
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

        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }

    }
    
    /**
     * Retrieves a CartItem object by its ID.
     *
     * @param itemID The ID of the cart item to be retrieved.
     * @return A CartItem object containing the cart item details.
     * @throws DatabaseException if there is a problem executing the query.
     * @throws ConnectionException if there is a problem to connecting the database.
     */
    public static CartItem findCartItem(int itemID) throws DatabaseException {
        String selectSQL = "SELECT * FROM Cart_Item WHERE cart_item_id = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, itemID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    itemID = resultSet.getInt("cart_item_id");
                    int productID = resultSet.getInt("product_id");
                    int quantity = resultSet.getInt("quantity");

                    CartItem item = new CartItem(itemID, ProductDAO.findProductByID(productID), quantity);
                    return item;
                }
            } 
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
        return null;
    }

    /**
     * Retrieves all cart items associated with a specific cart.
     *
     * @param cartID The ID of the cart.
     * @return An ArrayList of CartItem objects.
     * @throws DatabaseException if there is a problem executing the query.
     * @throws ConnectionException if there is a problem to connecting the database.
     */
    public static ArrayList<CartItem> findCartItems(int cartID) throws DatabaseException {
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

        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
        return itemList;
    }

    /**
     * Retrieves the ID of the cart associated with a specific user.
     *
     * @param userID The ID of the user whose cart ID is to be retrieved.
     * @return The cart ID associated with the given user ID, or {@code -1} if no cart is found.
     * @throws DatabaseException if there is a problem executing the query.
     * @throws ConnectionException if there is a problem to connecting the database.
     */
    public static int findCartID(int userID) throws DatabaseException {
        String selectSQL = "SELECT cart_id FROM Cart WHERE user_id = ?;";
        int cartID = -1;

        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, userID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    cartID = resultSet.getInt("cart_id");
                }
            } 
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
        return cartID;
    }
    
    /**
     * Retrieves the user ID associated with a specific cart.
     *
     * @param cartID The ID of the cart whose user ID is to be retrieved.
     * @return The user ID associated with the given cart ID, or {@code -1} if no user is found.
     * @throws DatabaseException if there is a problem executing the query.
     * @throws ConnectionException if there is a problem to connecting the database.
     */
    public static int findCartBelongedTo(int cartID) throws DatabaseException {
        String selectSQL = "SELECT user_id FROM Cart WHERE cart_id = ?;";
        int userID = -1;

        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, cartID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    userID = resultSet.getInt("user_id");
                }
            } 
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
        return userID;
    }

    /**
     * Retrieves the cart ID to which a specific cart item belongs.
     *
     * @param itemID The ID of the cart item whose cart ID is to be retrieved.
     * @return The {@code cartID} associated with the given cart item ID, or {@code -1} if no cart is found.
     * @throws DatabaseException if there is a problem executing the query.
     * @throws ConnectionException if there is a problem to connecting the database.
     */
    public static int findItemBelongedTo(int itemID) throws DatabaseException {
        String selectSQL = "SELECT cart_id FROM Cart_Item WHERE cart_item_id = ?;";
        int cartID = -1;

        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, itemID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    cartID = resultSet.getInt("cart_id");
                }
            } 
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
        return cartID;
    }

    /**
     * Checks if a product is already in a specific cart.
     *
     * @param productID The ID of the product to check in the cart.
     * @param cartID    The ID of the cart in which to check for the product.
     * @return The {@code cartItemID} if the product is found in the cart, or{@code -1} if it is not found.
     * @throws DatabaseException if there is a problem executing the query.
     * @throws ConnectionException if there is a problem to connecting the database.
     */
    public static int checkProductInCart(int productID, int cartID) throws DatabaseException {
        String selectSQL = "SELECT cart_item_id FROM Cart_Item WHERE product_id = ? AND cart_id = ?";
        int cartItemID = -1; 

        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, productID);
            preparedStatement.setInt(2, cartID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    cartItemID = resultSet.getInt("cart_item_id");
                }
            }
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
        return cartItemID;
    }
}
