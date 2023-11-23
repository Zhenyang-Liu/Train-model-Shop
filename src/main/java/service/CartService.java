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
     * This method first checks if the current user has authorization to access the given cart.
     * If the product is not already in the cart, it creates a new CartItem and adds it to the cart.
     * If the product is already in the cart, it updates the quantity of the existing item.
     *
     * @param cartID The ID of the cart to which the product is to be added.
     * @param productID The ID of the product to add to the cart.
     * @param quantity The quantity of the product to add.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static void addToCart(int cartID, int productID, int quantity) throws DatabaseException {
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
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            throw e;
        }
    }

    /**
     * Removes an item from a cart.
     *
     * This method checks if the current user is authorized to access the cart containing the item.
     * If authorized, it proceeds to remove the specified item from the cart.
     *
     * @param itemID The ID of the cart item to be removed.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static void removeFromCart(int itemID) throws DatabaseException {
        try {
            int itemHolderID = CartDAO.findCartBelongedTo(CartDAO.findItemBelongedTo(itemID));
            if (!permission.hasPermission(itemHolderID,"EDIT_OWN_CART")){
                throw new AuthorizationException("Access denied. Users can only access their own carts.");
            }
            CartDAO.deleteCartItem(itemID);

        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            throw e;
        }
    }

    /**
     * Removes a product from a shopping cart.
     * <p>
     * This method removes a specified product from a given cart. It first checks if the specified product
     * exists in the cart. If the product is found, the method then verifies that the current user
     * has authorization to modify the cart. If the user is authorized, the product is removed from the cart.
     * </p>
     *
     * @param cartID    The ID of the cart from which the product is to be removed.
     * @param productID The ID of the product to be removed from the cart.
     * @throws DatabaseException       If there is any issue with database connectivity or operations.
     * @throws AuthorizationException  If the current user does not have access to the cart.
     *
     * <p>
     * The method throws an {@code AuthorizationException} if the current user is not the owner of the cart.
     * Any database-related issues encountered during the operation will result in a {@code DatabaseException}.
     * </p>
     */
    public static void removeFromCart(int cartID, int productID) throws DatabaseException {
        try {
            int itemID = CartDAO.checkProductInCart(productID, cartID);
            int itemHolderID = CartDAO.findCartBelongedTo(CartDAO.findItemBelongedTo(itemID));
            if (!permission.hasPermission(itemHolderID,"EDIT_OWN_CART")){
                throw new AuthorizationException("Access denied. Users can only access their own carts.");
            }
            CartDAO.deleteCartItem(itemID);
            
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            throw e;
        }
    }

    /**
     * Retrieves the details of a cart for a specific user.
     * 
     * This method checks if the current user is authorized to access the cart of the specified user.
     * If authorized, it returns the details of the user's cart.
     *
     * @param userID The ID of the user whose cart details are to be retrieved.
     * @return A Cart object containing the details of the user's cart.
     * @throws DatabaseException if there is an issue with database access.
     */  
    public static Cart getCartDetails(int userID) throws DatabaseException {
        try {
            if (!permission.hasPermission(userID,"EDIT_OWN_CART")) {
                throw new AuthorizationException("Access denied. Users can only access their own carts.");
            }

            Cart cart = CartDAO.findCartByUser(userID);
            return cart;
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            throw e;   
        }
    }

    /**
     * Updates the quantity of a product in a shopping cart.
     * <p>
     * This method first checks if the current user has the authorization to access the specified cart.
     * If the user is authorized, it then verifies whether the specified product is in the cart.
     * If the product is found, the method updates the quantity of the product in the cart.
     * </p>
     *
     * @param cartID    The ID of the cart to be updated.
     * @param productID The ID of the product in the cart whose quantity is to be updated.
     * @param quantity  The new quantity to set for the product in the cart.
     * @throws DatabaseException       If there is any issue with database connectivity or operations.
     * @throws AuthorizationException  If the current user does not have access to the cart or the cart item.
     *
     * <p>
     * The method throws an {@code AuthorizationException} if the current user is not the owner of the cart
     * or tries to access an item not in their cart.
     * Any database-related issues during the process result in a {@code DatabaseException}.
     * </p>
     */
    public static void updateCartItem(int cartID, int productID, int quantity) throws DatabaseException {
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
                throw e;
            }
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            throw e;
        }
    }
    
    /**
     * Updates the quantity of an existing cart item.
     * 
     * This method checks if the current user is authorized to access the cart containing the item.
     * If authorized, it updates the quantity of the specified cart item.
     *
     * @param itemID The ID of the cart item whose quantity is to be updated.
     * @param quantity The new quantity for the cart item.
     * @throws DatabaseException if there is an issue with database access.
     */
        public static void updateCartItem(int itemID, int quantity) throws DatabaseException {
        try {
            int itemHolderID = CartDAO.findCartBelongedTo(CartDAO.findItemBelongedTo(itemID));
            System.out.println(itemHolderID);
            if (!permission.hasPermission(itemHolderID,"EDIT_OWN_CART")){
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
    

    /**
     * Clears all items from a specified cart.
     * 
     * This method checks if the current user is authorized to access the specified cart.
     * If authorized, it removes all items from the cart.
     *
     * @param cartID The ID of the cart to be cleared.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static void clearCart(int cartID) throws DatabaseException {
        try {
            int holderID = CartDAO.findCartBelongedTo(cartID);
            if (!permission.hasPermission(holderID,"EDIT_OWN_CART")){
                throw new AuthorizationException("Access denied. Users can only access their own carts.");
            }

            CartDAO.clearCart(cartID);

        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            throw e;
        }
    }
    
    /**
     * Processes the checkout of a specified cart and creates an order.
     *
     * This method retrieves the cart based on the given cart ID, verifies if the current user
     * is authorized to access the cart, and checks if the cart is not empty. It then
     * converts the cart items into a map of products and quantities, creating an Order object.
     * Note that the address ID is set to -1 by default and should be set in the confirmOrder() method.
     *
     * @param cartID The ID of the cart to be checked out.
     * @return An Order object representing the checked-out cart.
     * @throws DatabaseException if there is an issue with database access.
     * @throws CheckoutException if the checkout process encounters any issues, such as an empty cart or authorization failure.
     */
    public static Order checkoutCart(int cartID) throws DatabaseException, CheckoutException {
        try {
            Cart cart = CartDAO.findCartByID(cartID);
            int holderID = cart.getUserID();
            if (!permission.hasPermission(holderID,"EDIT_OWN_CART")){
                throw new AuthorizationException("Access denied. Users can only access their own carts.");
            }
            if (cart == null || cart.getUserID() ==-1 ) {
                throw new CheckoutException("Cart is empty or not exist.");
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
            throw e;
        }
    }

    public static int findItemID(int cartID, int productID) throws DatabaseException {
        try {
            return CartDAO.checkProductInCart(productID, cartID);
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            throw e;
        }
    }

    public static int findItemNUM(int itemID) throws DatabaseException {
        try {
            return Objects.requireNonNull(CartDAO.findCartItem(itemID)).getQuantity();
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            throw e;
        }
    }
}

