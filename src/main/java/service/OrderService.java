package service;

import java.util.ArrayList;
import java.util.Map;

import model.Cart;
import model.Order;
import model.Product;
import model.User;
import model.Order.OrderStatus;
import DAO.AddressDAO;
import DAO.OrderDAO;
import DAO.UserDAO;
import exception.*;
import helper.UserSession;

public class OrderService {
    
    /**
     * Confirms an order if certain conditions are met.
     *
     * @param order The Order object to confirm.
     * @return true if the order is successfully confirmed; false otherwise.
     */
    public static boolean confirmOrder(Order order) {
        try{
            if (!PermissionService.hasPermission(order.getUserID(),"VIEW_OWN_ORDERS")){
                throw new AuthorizationException("Access denied. Users can only confirm their own order.");
            }
            // Check the address
            if (order.getAddressID() == 0 || AddressDAO.findByAddressID(order.getAddressID()).getID() == 0){
                return false;
            }
            // Check the bankDetail
            if (!order.getBankDetailState()){
                return false;
            }
            OrderDAO.updateOrder(order);
            return true;
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return false;
        }
    }

    /**
     * Progresses the status of an order to the next stage in the fulfillment process.
     *
     * @param order The Order object to fulfill.
     * @return true if the order status is successfully updated; false otherwise.
     */
    public static boolean fulfillOrder(Order order) {
        try{
            if (!PermissionService.hasPermission("MANAGE_ORDERS")){
                throw new AuthorizationException("Access denied. Only Staff can manage orders.");
            }
            order.setUpdateTime();
            order.nextStatus();
            OrderDAO.updateOrder(order);
            return true;
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return false;
        }
    }

    /**
     * Cancels an order and records the reason for cancellation.
     *
     * @param order The Order object to cancel.
     * @param reason The reason for cancelling the order.
     * @return true if the order is successfully cancelled; false otherwise.
     */
    public static boolean cancelOrder(Order order, String reason) {
        try{
            if (!PermissionService.hasPermission(order.getUserID(),"VIEW_OWN_ORDERS") 
            && !PermissionService.hasPermission("MANAGE_ORDERS")){
                throw new AuthorizationException("Access denied. Only Staff can manage orders.");
            }
            order.setUpdateTime();
            order.setStatus("Cancelled");
            order.setReason(reason);
            OrderDAO.cancelOrder(order);
            return true;
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return false;
        }
    }

    /**
     * Retrieves all orders from the database.
     *
     * @return An ArrayList of Order objects.
     */
    public static ArrayList<Order> getAllOrders() {
        try{
            if (!PermissionService.hasPermission("MANAGE_ORDERS")){
                throw new AuthorizationException("Access denied. Only Staff can manage orders.");
            }
            ArrayList<Order> orders = OrderDAO.findAllOrder();

            return orders;
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return null;
        }
    }

    /**
     * Retrieves all orders for a specific user.
     *
     * @return An ArrayList of Order objects belonging to the current user.
     */
    public static ArrayList<Order> getUserAllOrder(){
        try{
            int userID = UserSession.getInstance().getCurrentUser().getUserID();
            if (!PermissionService.hasPermission(userID,"VIEW_OWN_ORDERS")){
                throw new AuthorizationException("Access denied. Users can only see their own order.");
            }
            ArrayList<Order> orders = OrderDAO.findUserAllOrder(userID);


            return orders;
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return null;
        }
    }

    /**
     * Retrieves orders filtered by a specific status.
     *
     * @param statusFilter The status to filter orders by.
     * @return An ArrayList of Order objects that match the specified status.
     */
    public static ArrayList<Order> getOrdersByStatus(String statusFilter) {
        try{
            if (!PermissionService.hasPermission("MANAGE_ORDERS")){
                throw new AuthorizationException("Access denied. Only Staff can manage orders.");
            }
            ArrayList<Order> orders = OrderDAO.findOrderByStatus(OrderStatus.fromName(statusFilter).getStatus());

            return orders;
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return null;
        }
    }

    /**
     * Returns the items from an order back into the user's cart.
     *
     * @param order The Order object whose items are to be returned to the cart.
     */
    public static void returnToCart(Order order){
        try{
            int userID = order.getUserID();
            if (!PermissionService.hasPermission(order.getUserID(),"VIEW_OWN_ORDERS")){
                throw new AuthorizationException("Access denied. Users can only edit their own order.");
            }
            Cart cart = CartService.getCartDetails(userID);
            Map<Product,Integer> itemList = order.getOrderItems();
            for (Map.Entry<Product,Integer> entry : itemList.entrySet()){
                Product product = entry.getKey();
                int quantity = entry.getValue();
                CartService.addToCart(cart.getCartID(), product.getProductID(), quantity);
            }
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
        }
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param orderID The ID of the order to retrieve.
     * @return The Order object, or null if not found.
     */
    public static Order findOrderByID(int orderID){
        try{
            if (!PermissionService.hasPermission("MANAGE_ORDERS")){
                throw new AuthorizationException("Access denied. Only Staff can manage orders.");
            }
            Order order = OrderDAO.findOrderByID(orderID);
            return order;
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return null;
        }
    }

    /**
     * Retrieves user information from the database based on the user ID.
     *
     * This method checks if the current session has the permission to view customer details or to view the own orders of the specified user.
     * If the permission check passes, it fetches the user's details from the database.
     * Returns the User object if found, or null in case of a database error or if the user is not found.
     *
     * @param userID The ID of the user whose information is to be retrieved.
     * @return A User object containing the user's information, or null if an error occurs or if the user is not found.
     */
    public static User getUserInfo(int userID) {
        try{
            if (!PermissionService.hasPermission("VIEW_CUSTOMER_DETAILS")
             && !PermissionService.hasPermission(userID, "VIEW_OWN_ORDERS")){
                throw new AuthorizationException("Access denied. You have no permission to see the detail.");
            }
            User user = UserDAO.findUserByID(userID);
            return user; 
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return null;
        }
    }
}
