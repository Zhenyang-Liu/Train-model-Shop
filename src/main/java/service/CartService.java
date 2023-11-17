package service;

import java.sql.SQLException;


import DAO.CartDAO;
import exception.*;
import helper.Validation;
import model.Cart;
import model.CartItem;

public class CartService {

    public static void addToCart(int cartID, int productId, int quantity) {
        
    }

    public static void removeFromCart(int itemID, int quantity) {
        
    }

    public static void removeFromCart(int itemID) {
        
        removeFromCart(itemID);
    }

    public static Cart getCartDetails(int userID) throws AuthorizationException, SQLException {
        Cart cart = CartDAO.findCartByUser(userID);

        if (cart == null || !Validation.isCurrentUser(userID) || !Validation.isCurrentUser(cart.getUserID())) {
            throw new AuthorizationException("Access denied. Users can only access their own carts.");
        }

        return cart;
    }

    public static void updateCartItem(int userID, int itemID, int quantity) throws AuthorizationException {
        if (!Validation.isCurrentUser(userID)){
            throw new AuthorizationException("Access denied. Users can only access their own carts.");
        }
        try {
            CartItem item = CartDAO.findCartItem(itemID);
            item.setQuantity(quantity);
            CartDAO.updateCartItem(item);
        } catch (SQLException e) {
            e.printStackTrace();;
        }
    }

    public static void clearCart(int userID) throws AuthorizationException, NotFoundException {
        if (!Validation.isCurrentUser(userID)){
            throw new AuthorizationException("Access denied. Users can only access their own carts.");
        }
        
        try {
            int cartID = CartDAO.findCartID(userID);
            if (cartID > 0) {
                CartDAO.clearCart(cartID);
            } else {
                throw new NotFoundException("Cart clear failed. Cannot find user's cart in the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void checkoutCart(int userID) throws AuthorizationException {
        if (!Validation.isCurrentUser(userID)){
            throw new AuthorizationException("Access denied. Users can only access their own carts.");
        }
    }

}

