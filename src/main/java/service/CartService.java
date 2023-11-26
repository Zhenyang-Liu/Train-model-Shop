package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import DAO.CartDAO;
import DAO.ProductDAO;
import exception.*;
import model.Cart;
import model.CartItem;
import model.Order;
import model.Product;


public class CartService {
    private static PermissionService permission = new PermissionService();

    /**
     * Adds a product to a specified cart.
     *
     * This method first checks if the current user has permission to edit their own cart.
     * It then adds the specified product to the cart, creating a new cart item if it doesn't exist, 
     * or updating the quantity if it does. Returns true if the operation is successful, or false if a DatabaseException occurs.
     *
     * @param cartID The ID of the cart to which the product is to be added.
     * @param productID The ID of the product to add to the cart.
     * @param quantity The quantity of the product to add.
     * @return true if the product is successfully added to the cart; false otherwise.
     */
    public static boolean addToCart(int cartID, int productID, int quantity) {
        try {
            if (!permission.hasPermission(CartDAO.findCartBelongedTo(cartID),"EDIT_OWN_CART")) {
                throw new AuthorizationException("Access denied. Users can only access their own carts.");
            }

            int itemID = CartDAO.checkProductInCart(productID, cartID);
            if (itemID == -1 || itemID == 0){
                CartItem item = new CartItem(ProductDAO.findProductByID(productID), quantity);
                ArrayList<CartItem> itemList = new ArrayList<>();
                itemList.add(item);
                CartDAO.insertCartItems(cartID, itemList);
            } else {
                updateCartItem(itemID, quantity);
            }
            return true;
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return false;
        }
    }

    /**
     * Removes an item from a cart.
     *
     * This method first checks if the current user has permission to edit their own cart.
     * It then removes the specified item from the cart. Returns true if the operation is successful, or false if a DatabaseException occurs.
     *
     * @param itemID The ID of the cart item to be removed.
     * @return true if the cart item is successfully removed; false otherwise.
     */
    public static boolean removeFromCart(int itemID) {
        try {
            int itemHolderID = CartDAO.findCartBelongedTo(CartDAO.findItemBelongedTo(itemID));
            if (!permission.hasPermission(itemHolderID,"EDIT_OWN_CART")){
                throw new AuthorizationException("Access denied. Users can only access their own carts.");
            }
            CartDAO.deleteCartItem(itemID);
            return true;
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return false;
        }
    }

    /**
     * Removes a specific product from a cart.
     *
     * This method first checks if the current user has permission to edit their own cart.
     * It then removes the specified product from the cart. Returns true if the operation is successful, or false if a DatabaseException occurs.
     *
     * @param cartID The ID of the cart from which the product is to be removed.
     * @param productID The ID of the product to be removed from the cart.
     * @return true if the product is successfully removed from the cart; false otherwise.
     */
    public static boolean removeFromCart(int cartID, int productID) {
        try {
            int itemID = CartDAO.checkProductInCart(productID, cartID);
            int itemHolderID = CartDAO.findCartBelongedTo(CartDAO.findItemBelongedTo(itemID));
            if (!permission.hasPermission(itemHolderID,"EDIT_OWN_CART")){
                throw new AuthorizationException("Access denied. Users can only access their own carts.");
            }
            CartDAO.deleteCartItem(itemID);
            return true;
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return false;
        }
    }
  
    /**
     * Retrieves the details of a cart for a specific user.
     *
     * This method first checks if the current user has permission to edit their own cart.
     * It then fetches and returns the details of the user's cart. Returns null if a DatabaseException occurs.
     *
     * @param userID The ID of the user whose cart details are to be retrieved.
     * @return A Cart object containing the details of the user's cart, or null if an error occurs.
     */
    public static Cart getCartDetails(int userID) {
        try {
            if (!permission.hasPermission(userID,"EDIT_OWN_CART")) {
                throw new AuthorizationException("Access denied. Users can only access their own carts.");
            }

            Cart cart = CartDAO.findCartByUser(userID);
            return cart;
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return null; 
        }
    }

    /**
     * Updates the quantity of a specific cart item.
     *
     * This method checks if the current user has permission to edit their own cart and then updates the quantity of the specified cart item.
     * Returns true if the operation is successful, or false if a DatabaseException occurs.
     *
     * @param cartID The ID of the cart containing the item to be updated.
     * @param productID The ID of the product in the cart to be updated.
     * @param quantity The new quantity to set for the cart item.
     * @return true if the cart item is successfully updated; false otherwise.
     */
    public static boolean updateCartItem(int cartID, int productID, int quantity) {
        try {
            if (!permission.hasPermission(CartDAO.findCartBelongedTo(cartID),"EDIT_OWN_CART")){
                throw new AuthorizationException("Access denied. Users can only access their own carts.");
            }

            int itemID = CartDAO.checkProductInCart(productID, cartID);
            try {
                int itemHolderID = CartDAO.findCartBelongedTo(CartDAO.findItemBelongedTo(itemID));
                if (!permission.hasPermission(itemHolderID,"EDIT_OWN_CART")){
                    throw new AuthorizationException("Access denied. User cannot access item " + itemID + ".");
                }
                CartItem item = CartDAO.findCartItem(itemID);
                item.setQuantity(quantity);
                CartDAO.updateCartItem(item);

            } catch (DatabaseException e) {
                ExceptionHandler.printErrorMessage(e);
                return false;
            }
            return true;
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return false;
        }
    }

    /**
     * Updates the quantity of a specific cart item.
     *
     * This method checks if the current user has permission to edit their own cart and then updates the quantity of the specified cart item.
     * Returns true if the operation is successful, or false if a DatabaseException occurs.
     *
     * @param itemID The ID of the cart item to be updated.
     * @param quantity The new quantity to set for the cart item.
     * @return true if the cart item is successfully updated; false otherwise.
     */
    public static boolean updateCartItem(int itemID, int quantity) {
        try {
            int itemHolderID = CartDAO.findCartBelongedTo(CartDAO.findItemBelongedTo(itemID));
            System.out.println(itemHolderID);
            if (!permission.hasPermission(itemHolderID,"EDIT_OWN_CART")){
                throw new AuthorizationException("Access denied. User cannot access item " + itemID + ".");
            }
            CartItem item = CartDAO.findCartItem(itemID);
            item.setQuantity(quantity);
            CartDAO.updateCartItem(item);
            return true;
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return false;
        }
    }
    
    /**
     * Clears all items from a specified cart.
     *
     * This method checks if the current user has permission to edit their own cart and then clears all items from the specified cart.
     * Returns true if the operation is successful, or false if a DatabaseException occurs.
     *
     * @param cartID The ID of the cart to be cleared.
     * @return true if the cart is successfully cleared; false otherwise.
     */
    public static boolean clearCart(int cartID) {
        try {
            int holderID = CartDAO.findCartBelongedTo(cartID);
            if (!permission.hasPermission(holderID,"EDIT_OWN_CART")){
                throw new AuthorizationException("Access denied. Users can only access their own carts.");
            }
            CartDAO.clearCart(cartID);
            return true;
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return false;
        }
    }

    /**
     * Proceeds to checkout for a specified cart.
     *
     * This method checks if the current user has permission to access their own cart and then creates an order based on the cart items.
     * Returns the created Order object, or null if a DatabaseException occurs or the cart is empty or non-existent.
     *
     * @param cartID The ID of the cart to checkout.
     * @return An Order object representing the checked-out cart, or null if an error occurs or the cart is invalid.
     */
    public static Order checkoutCart(int cartID) {
        try {
            // TODO: unfinished
            Cart cart = CartDAO.findCartByID(cartID);
            int holderID = cart.getUserID();
            if (!permission.hasPermission(holderID,"EDIT_OWN_CART")){
                throw new AuthorizationException("Access denied. Users can only access their own carts.");
            }
            if (cart == null || cart.getUserID() ==-1 ) {
                throw new DatabaseException("Cart is empty or not exist.");
            }
            
            Map<Product,Integer> itemList = new HashMap<>();
            for (CartItem item : cart.getCartItems()) {
                itemList.put(item.getItem(), item.getQuantity());
            }
            // Address ID is default to -1. This should be setted in confirmOrder(); 
            Order order = new Order(holderID, -1, itemList);
            return order;
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return null;
        }
    }

    /**
     * Finds the item ID of a product in a cart.
     *
     * This method searches for an item in a cart based on the given product ID and returns the item ID.
     * Returns the item ID if found, or -1 if a DatabaseException occurs.
     *
     * @param cartID The ID of the cart containing the product.
     * @param productID The ID of the product to find in the cart.
     * @return The ID of the item if found; -1 otherwise.
     */
    public static int findItemID(int cartID, int productID) {
        try {
            return CartDAO.checkProductInCart(productID, cartID);
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return -1;
        }
    }

    /**
     * Finds the quantity of a specific cart item.
     *
     * This method retrieves the quantity of an item in a cart based on the given item ID.
     * Returns the quantity of the item, or 0 if a DatabaseException occurs.
     *
     * @param itemID The ID of the cart item whose quantity is to be found.
     * @return The quantity of the item; 0 if an error occurs.
     */
    public static int findItemNUM(int itemID) {
        try {
            return Objects.requireNonNull(CartDAO.findCartItem(itemID)).getQuantity();
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return 0;
        }
    }
}

