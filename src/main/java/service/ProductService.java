package service;

import java.util.ArrayList;
import java.util.Map;

import DAO.BoxedSetDAO;
import DAO.CartDAO;
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
    
    /**
     * Updates a product and its related boxed sets in the database.
     * 
     * This method first verifies if the user has the "MANAGE_PRODUCTS" permission.
     * If the permission is granted, it proceeds to update the specified product using {@link ProductDAO#updateProduct(Product)}.
     * It then checks for any boxed sets that include this product and updates their quantities accordingly.
     * 
     * @param p The Product object to be updated.
     * @return A string indicating the success of the operation or an error message in case of a failure.
     * @throws AuthorizationException If the user does not have the necessary permission.
     * @throws DatabaseException If there is an error during database operations.
     */
    public static String updateProduct(Product p) {
        try{
            if (!PermissionService.hasPermission("MANAGE_PRODUCTS")){
                throw new AuthorizationException("Access denied. Only Staff can manage Products.");
            }
            ProductDAO.updateProduct(p);
            ArrayList<Integer> idList = BoxedSetDAO.findProductBelongTo(p.getProductID());
            if (idList.size() > 0) {
                for (int id : idList) {
                    ProductService.updateBoxedSetQuantity(id);
                }
            }

            return "success";
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return e.getMessage();
        }
    }

    /**
     * Retrieves all products from the database.
     *
     * This function is specific designed for product manage dashboard.
     * 
     * @return An ArrayList of Product objects.
     */
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

    /**
     * Retrieves products by their type.
     *
     * @param type The type of products to retrieve.
     * @return An ArrayList of Product objects of the specified type.
     */
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

    /**
     * Validates the input for a new or updated product.
     *
     * @param productName The product's name.
     * @param productCode The product's code.
     * @param brandName The product's brand name.
     * @param retailPrice The retail price of the product.
     * @param stockQuantity The stock quantity of the product.
     * @return A string indicating any validation errors, or null if the input is valid.
     */
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

        try{
            if (ProductDAO.productCodeExist(productCode)){
              return "Product Code has existed in database.";
            }
        } catch (DatabaseException e){
            return "Server Error. Please try again later.";
        }

        return null;
    }

    /**
     * Deletes a product from the database.
     *
     * @param p The Product object to delete.
     * @return A string indicating the success or failure of the operation.
     */
    public static String deleteProduct(Product p){
        try{
            if (!PermissionService.hasPermission("MANAGE_PRODUCTS")){
                throw new AuthorizationException("Access denied. Only Staff can manage Products.");
            }
            CartDAO.deleteProduct(p.getProductID());
            switch (p.getProductType().toLowerCase()) {
                case "track":
                    TrackDAO.deleteTrack(p.getProductID());
                    break;
                case "locomotive":
                    LocomotiveDAO.deleteLocomotive(p.getProductID());
                    break;
                case "rolling stock":
                    RollingStockDAO.deleteRollingStock(p.getProductID());
                    break;
                case "controller":
                    ControllerDAO.deleteController(p.getProductID());
                    break;
                case "train set":
                    BoxedSetDAO.deleteBoxedSet(p.getProductID());
                    break;
                case "track pack":
                    BoxedSetDAO.deleteBoxedSet(p.getProductID());
                    break;
                default:
                    return "failed";
            }

            return "success";
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return null;
        }
    } 

    /**
     * Updates the quantity of boxed sets based on the stock of individual products.
     *
     * @param boxID The ID of the boxed set.
     */
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

    /**
     * Finds all eras from the database.
     *
     * @return An ArrayList of era descriptions.
     */
    public static ArrayList<String> findAllEra(){
        try {
            return EraDAO.findAllEraDescription();
        } catch (DatabaseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Finds descriptions for a list of era codes.
     *
     * @param list An array of era codes.
     * @return An ArrayList of era descriptions.
     */
    public static ArrayList<String> findEraDescription(int[] list){
        try {
            if (!PermissionService.hasPermission("BROWSE_PRODUCTS")){
                throw new AuthorizationException("Access denied. Only Register user can browse products.");
            }
            ArrayList<String> description = new ArrayList<>();
            for (int i = 0; i<list.length; i++){
                description.add(EraDAO.findDescriptionByCode(list[i]));
            }
            return description;
        } catch (DatabaseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Finds a product by its ID.
     *
     * @param productID The ID of the product to find.
     * @return The Product object, or a new Product object if not found.
     */
    public static Product findProductByID(int productID){
        try {
            return ProductDAO.findProductByID(productID);
        } catch (DatabaseException e) {
            e.printStackTrace();
            return new Product();
        }
    }

}
