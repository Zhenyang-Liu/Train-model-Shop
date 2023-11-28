package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import DAO.BoxedSetDAO;
import DAO.CartDAO;
import DAO.OrderDAO;
import DAO.ProductDAO;
import exception.*;
import helper.Logging;
import helper.UserSession;
import model.BoxedSet;
import model.Cart;
import model.CartItem;
import model.Order;
import model.Product;
import model.User;


public class CartService {

    public static boolean createCart() {
        try {
            int userID = UserSession.getInstance().getCurrentUser().getUserID();
            if (!PermissionService.hasPermission(userID,"EDIT_OWN_CART")) {
                throw new AuthorizationException("Access denied. Users can only access their own carts.");
            }
            CartDAO.insertCart(new Cart(userID));
            return true;

        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return false;
        }
    }
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
            if (!PermissionService.hasPermission(CartDAO.findCartBelongedTo(cartID),"EDIT_OWN_CART")) {
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
            if (!PermissionService.hasPermission(itemHolderID,"EDIT_OWN_CART")){
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
            if (!PermissionService.hasPermission(itemHolderID,"EDIT_OWN_CART")){
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
            if (!PermissionService.hasPermission(userID,"VIEW_OWN_ORDERS")) {
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
            if (!PermissionService.hasPermission(CartDAO.findCartBelongedTo(cartID),"EDIT_OWN_CART")){
                throw new AuthorizationException("Access denied. Users can only access their own carts.");
            }

            int itemID = CartDAO.checkProductInCart(productID, cartID);
            try {
                int itemHolderID = CartDAO.findCartBelongedTo(CartDAO.findItemBelongedTo(itemID));
                if (!PermissionService.hasPermission(itemHolderID,"EDIT_OWN_CART")){
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
            if (!PermissionService.hasPermission(itemHolderID,"EDIT_OWN_CART")){
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
            if (!PermissionService.hasPermission(holderID,"EDIT_OWN_CART")){
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
     * Processes the checkout of a cart and creates an order.
     *
     * This method checks if the user has permission to edit their own cart and validates the cart's existence.
     * It then checks the stock for each product in the cart and creates an order if the stock is sufficient.
     * Returns the ID of the created order, or a negative number indicating specific error conditions:
     * -1 -- Stock issue
     * -2 -- Create Order issue
     * -3 -- Other Exception
     *
     * @param cartID The ID of the cart to be checked out.
     * @return The ID of the created order, or a negative number in case of errors.
     */
    public static int checkoutCart(int cartID) {
        try {
            Cart cart = CartDAO.findCartByID(cartID);
            int holderID = cart.getUserID();
            if (!PermissionService.hasPermission(holderID,"EDIT_OWN_CART")){
                throw new AuthorizationException("Access denied. Users can only access their own carts.");
            }
            if (cart == null || cart.getUserID() ==-1 ) {
                throw new DatabaseException("Cart is empty or not exist.");
            }
            
            Map<Product, Integer> itemList = new HashMap<>();
            Map<Product, Integer> checkList = new HashMap<>();
            Set<Integer> boxedSetList = new HashSet<>();

            for (CartItem item : cart.getCartItems()) {
                String itemType = item.getItem().getProductType();
                if ("Train Set".equals(itemType) || "Track Pack".equals(itemType)) {
                    BoxedSet set = BoxedSetDAO.findBoxedSetByID(item.getItem().getProductID());
                    for (Map.Entry<Product,Integer> setItem : set.getContain().entrySet()){
                        addProductToMap(checkList, setItem.getKey(), setItem.getValue()*item.getQuantity());
                        boxedSetList.add(setItem.getKey().getProductID());
                    }
                } else {
                    addProductToMap(checkList, item.getItem(), item.getQuantity());
                }
                itemList.put(item.getItem(), item.getQuantity());
            }

            // Check the Stock
            if (!checkStock(checkList)){
                return -1; // stock not enough
            } 
            
            Map<Product, Integer> successList = new HashMap<>();

            for (Map.Entry<Product,Integer> entry : checkList.entrySet()){
                int productID = entry.getKey().getProductID();
                int quantity = entry.getValue();
                if (reduceStock(productID, quantity)){
                    successList.put(entry.getKey(), entry.getValue());
                } else {
                    restoreStock(successList);
                }
            }

            Iterator<Integer> iterator = boxedSetList.iterator();
            while (iterator.hasNext()) {
                int setID = iterator.next();
                ProductService.updateBoxedSetQuantity(setID);
            }
            
            // Address ID is default to -1. This should be setted in confirmOrder(); 
            Order order = new Order(holderID, -1, itemList, false);
            int orderID = OrderDAO.insertOrder(order);
            if (orderID > 0) {
                for (CartItem item : cart.getCartItems()) {
                    removeFromCart(item.getItemID());
                }
                return orderID;
            } else {
                return -2;
            }
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return -3;
        }
    }

    /**
     * Checks if sufficient stock is available for all products in the cart.
     *
     * This method iterates over the provided checkList of products and their quantities,
     * checking if there is enough stock for each product.
     *
     * @param checkList A map of products and their quantities to be checked.
     * @return true if enough stock is available for all products; false otherwise.
     */
    public static boolean checkStock(Map<Product, Integer> checkList){
        boolean enoughStock = true;
        try{
            for(Map.Entry<Product,Integer> entry : checkList.entrySet()){
                int productID = entry.getKey().getProductID();
                int quantity = entry.getValue();
                if (!ProductDAO.checkProductStock(productID, quantity)) {
                    enoughStock = false;
                    return false;
                }
            }
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return false;
        }
        return enoughStock;
    }

    /**
     * Reduces the stock quantity for a specified product.
     *
     * This method updates the stock quantity for a given product in the database.
     *
     * @param productID The ID of the product whose stock is to be reduced.
     * @param quantity The quantity by which the stock should be reduced.
     * @return true if the stock reduction is successful; false otherwise.
     */
    public static boolean reduceStock(int productID, int quantity) {
        try {
            Product product = ProductDAO.findProductByID(productID);
            product.setStockQuantity(product.getStockQuantity() - quantity);
            ProductDAO.updateProduct(product);
            return true;
        } catch (IllegalArgumentException i) {
            ExceptionHandler.printErrorMessage(i);
            return false;
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return false;
        }
    }

    /**
     * Restores the stock for a list of products.
     *
     * This method is typically used to revert stock changes in case of an incomplete transaction or error.
     *
     * @param successList A map of products and the quantities to be restored.
     */
    public static void restoreStock(Map<Product, Integer> successList) {
        try {
            for (Map.Entry<Product, Integer> entry : successList.entrySet()){
                int productID = entry.getKey().getProductID();
                int quantity = entry.getValue();
                reduceStock(productID, -quantity);
            }
        } catch (IllegalArgumentException i) {
            ExceptionHandler.printErrorMessage(i);
        }
    }
    
    /**
     * Adds a product and its quantity to a map, updating the quantity if the product already exists in the map.
     *
     * @param itemList A map of products and their quantities.
     * @param product The product to be added or updated in the map.
     * @param quantity The quantity of the product.
     * @return The updated map with the added or updated product.
     */
    private static Map<Product,Integer> addProductToMap(Map<Product,Integer> itemList, Product product, int quantity){
        boolean isFound = false;
        for (Map.Entry<Product, Integer> entry : itemList.entrySet()){
            int productID = entry.getKey().getProductID();
            if (productID == product.getProductID()){
                entry.setValue(entry.getValue()+quantity);
                isFound = true;
                break;
            }
        }

        if (!isFound){
            itemList.put(product, quantity);
        }
        return itemList;
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

