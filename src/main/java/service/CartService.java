package service;

import DAO.CartDAO;
import exception.*;
import helper.*;
import model.Cart;
import model.CartItem;

public class CartService {

    public static void addToCart(int cartID, int productID, int quantity) throws DatabaseException {
        try {
            if (!Validation.isCurrentUser(CartDAO.findCartBelongedTo(cartID))){
                throw new AuthorizationException("Access denied. Users can only access their own carts.");
            }
            
            if (CartDAO.checkProductInCart(productID, cartID) == -1){

            } else ()
            

        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            throw e;
        }
    }

    public static void removeFromCart(int itemID, int quantity) throws DatabaseException {
        try {
            int itemHolderID = CartDAO.findCartBelongedTo(CartDAO.findItemBelongedTo(itemID));
            if (!Validation.isCurrentUser(CartDAO.findCartBelongedTo(itemHolderID))){
                throw new AuthorizationException("Access denied. Users can only access their own carts.");
            }
            // TODO add your code here
            

        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            throw e;
        }
    }

    public static void removeFromCart(int itemID) throws DatabaseException {
        try {
            int itemHolderID = CartDAO.findCartBelongedTo(CartDAO.findItemBelongedTo(itemID));
            if (!Validation.isCurrentUser(itemHolderID)){
                throw new AuthorizationException("Access denied. Users can only access their own carts.");
            }
            // TODO add your code here
            

        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            throw e;
        }
    }

    public static Cart getCartDetails(int userID) throws DatabaseException {
        try {
            // TODO modify logic
            Cart cart = CartDAO.findCartByUser(userID);
    
            if (cart == null || !Validation.isCurrentUser(userID) || !Validation.isCurrentUser(cart.getUserID())) {
                throw new AuthorizationException("Access denied. Users can only access their own carts.");
            }
    
            return cart;
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            throw e;   
        }
    }
    

    public static void updateCartItem(int itemID, int quantity) throws DatabaseException {
        try {
            int itemHolderID = CartDAO.findCartBelongedTo(CartDAO.findItemBelongedTo(itemID));
            if (!Validation.isCurrentUser(itemHolderID)){
                throw new AuthorizationException("Access denied. User cannot access item " + itemID + ".");
            }
            CartItem item = CartDAO.findCartItem(itemID);
            item.setQuantity(quantity);
            CartDAO.updateCartItem(item);

        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            throw e;
        }
    }
    

    public static void clearCart(int userID) throws DatabaseException {
        try {
            if (!Validation.isCurrentUser(userID)){
                throw new AuthorizationException("Access denied. Users can only access their own carts.");
            }

            int cartID = CartDAO.findCartID(userID);
            if (cartID > 0) {
                CartDAO.clearCart(cartID);
            } else {
                throw new NotFoundException("Cart clear failed. Cannot find user's cart in the database.");
            }
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            throw e;
        }
    }

    public static void checkoutCart(int userID) throws DatabaseException {
        try {
            if (!Validation.isCurrentUser(userID)){
                throw new AuthorizationException("Access denied. Users can only access their own carts.");
            }
            // TODO add your code here
            

        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            throw e;
        }
    }

}

