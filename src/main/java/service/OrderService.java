package service;

import java.util.Map;

import model.Order;
import model.Product;
import DAO.OrderDAO;
import DAO.ProductDAO;
import exception.*;

public class OrderService {
    private static PermissionService permission = new PermissionService();

    public static boolean confirmOrder(Order order) {
        // TODO: Unfinished
        try{
            if (!permission.hasPermission(order.getUserID(),"EDIT_OWN_CART")){
                throw new AuthorizationException("Access denied. Users can only confirm their own order.");
            }
            // Check the address
            
            // Check the bankDetail

            // Check the stock
            for (Map.Entry<Product, Integer> entry : order.getOrderItems().entrySet()) {
                Product stockProduct = ProductDAO.findProductByID(entry.getKey().getProductID());
                if (!stockProduct.checkStock(entry.getValue())){
                    // TODO: missing the logic on holding insuffient stock
                }
            }
            OrderDAO.insertOrder(order);
            return true;
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return false;
        }
    }
}
