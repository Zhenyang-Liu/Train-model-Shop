package service;

import java.util.ArrayList;
import java.util.Map;

import DAO.BoxedSetDAO;
import DAO.ControllerDAO;
import DAO.EraDAO;
import DAO.LocomotiveDAO;
import DAO.ProductDAO;
import DAO.RollingStockDAO;
import DAO.TrackDAO;
import exception.*;
import model.BoxedSet;
import model.Product;
import model.BoxedSet.BoxedType;

public class ProductService {

    public static ArrayList<Product> getAllProducts() {
        try{
            if (!PermissionService.hasPermission("MANAGE_PRODUCTS")){
                throw new AuthorizationException("Access denied. Only Staff can manage Products.");
            }
            ArrayList<Product> products = ProductDAO.getAllProduct();

            return products;
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return null;
        }
    }

    public static ArrayList<Product> getAllProductsByType(String type) {
        try{
            if (!PermissionService.hasPermission("MANAGE_PRODUCTS")){
                throw new AuthorizationException("Access denied. Only Staff can manage Products.");
            }
            ArrayList<? extends Product> productList = new ArrayList<>();
            switch (type.toLowerCase()) {
                case "all":
                    productList = ProductDAO.getAllProduct();
                    break;
                case "track":
                    productList = TrackDAO.findAllTracks();
                    break;
                case "locomotive":
                    productList = LocomotiveDAO.findAllLocomotives();
                    break;
                case "rolling stock":
                    productList = RollingStockDAO.findAllRollingStocks();
                    break;
                case "controller":
                    productList = ControllerDAO.findAllControllers();
                    break;
                case "train set":
                    productList = BoxedSetDAO.findBoxedSetByType(BoxedType.TRAINSET);
                    break;
                case "track pack":
                    productList = BoxedSetDAO.findBoxedSetByType(BoxedType.TRACKPACK);
                    break;
                default:
                    productList = new ArrayList<>();
            }
            
            ArrayList<Product> filterList = new ArrayList<>();
            for (Product product : productList) {
                filterList.add(product);
            }

            return filterList;
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

    public static void updateBoxedSetQuantity(int boxID) {
        try {
            BoxedSet set = BoxedSetDAO.findBoxedSetByID(boxID);

            if (set != null) {
                int minQuantity = Integer.MAX_VALUE;
    
                for (Map.Entry<Product, Integer> entry : set.getContain().entrySet()) {
                    Product product = entry.getKey();
                    int quantity = product.getStockQuantity();
                    int maxQuantity = quantity / entry.getValue();

                    if (maxQuantity < minQuantity) {
                        minQuantity = maxQuantity;
                    }
                }
    
                if (minQuantity != Integer.MAX_VALUE) {
                    ProductDAO.updateStock(boxID, minQuantity);
                }
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }    

    public static ArrayList<String> findAllEra(){
        try {
            return EraDAO.findAllEraDescription();
        } catch (DatabaseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Product findProductByID(int productID){
        try {
            return ProductDAO.findProductByID(productID);
        } catch (DatabaseException e) {
            e.printStackTrace();
            return new Product();
        }
    }

}
