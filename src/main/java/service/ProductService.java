package service;

import java.util.ArrayList;
import java.util.Map;

import DAO.BoxedSetDAO;
import DAO.ProductDAO;
import exception.*;
import model.BoxedSet;
import model.Product;

public class ProductService {
    private static PermissionService permission = new PermissionService();

    public static ArrayList<Product> getAllProducts() {
        try{
            if (!permission.hasPermission("MANAGE_PRODUCTS")){
                throw new AuthorizationException("Access denied. Only Staff can manage Products.");
            }
            ArrayList<Product> products = ProductDAO.getAllProduct();

            return products;
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return null;
        }
    }

    public static String validateProductInput(String productName, String productCode, String brandName, 
                                            String retailPrice, String stockQuantity) {
        if (productName == null || productName.trim().isEmpty()) {
            return "Product name cannot be empty.";
        }

        if (productCode == null || productCode.trim().isEmpty()) {
            return "Product code cannot be empty.";
        }
        
        if (!Product.isValidProductCode(productCode)){
            return "Product Code format is wrong.";
        }

        try{
            if (ProductDAO.productCodeExist(productCode)){
              return "Product Code has existed in database.";
            }
        } catch (DatabaseException e){
            return "Server Error. Please try again later.";
        }

        if (brandName == null || brandName.trim().isEmpty()) {
            return "Brand name cannot be empty.";
        }

        try {
            double price = Double.parseDouble(retailPrice);
            if (price < 0) {
                return "Retail price cannot be negative.";
            }
        } catch (NumberFormatException e) {
            return "Retail price must be a valid number.";
        }

        try {
            int quantity = Integer.parseInt(stockQuantity);
            if (quantity < 0) {
                return "Stock quantity cannot be negative.";
            }
        } catch (NumberFormatException e) {
            return "Stock quantity must be a valid integer.";
        }

        return null;
    }

    public static void updateBoxedSetQuantity(int boxID){
        try {
            BoxedSet set = BoxedSetDAO.findBoxedSetByID(boxID);
            int minQuantity = Integer.MAX_VALUE;
            for (Map.Entry<Product,Integer> entry : set.getContain().entrySet()){
                int quantity = entry.getKey().getStockQuantity();
                int maxQuantity = quantity / entry.getValue();
                if (maxQuantity < minQuantity){
                    minQuantity = maxQuantity;
                }
            }
            
            if (minQuantity != Integer.MAX_VALUE){
                ProductDAO.updateStock(boxID, minQuantity);
            } 
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

}
