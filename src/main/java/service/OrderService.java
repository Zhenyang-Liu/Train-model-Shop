package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.Cart;
import model.Order;
import model.Product;
import DAO.AddressDAO;
import DAO.OrderDAO;
import exception.*;

public class OrderService {
    
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
}
