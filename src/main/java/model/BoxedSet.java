package model;

import java.util.HashMap;
import java.util.Map;

public class BoxedSet extends Product{
    private BoxedType boxedType;
    private Map<Product, Integer> contain;

    public enum BoxedType {
        TRAINSET("Train Set"),
        TRACKPACK("Track Pack");

        private final String type;
    
        BoxedType(String type) {
            this.type = type;
        }
    
        public String getType() {
            return type;
        }

        public static BoxedType fromName(String typeName) {
            for (BoxedType type : BoxedType.values()) {
                if (type.getType().equalsIgnoreCase(typeName)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid Boxed set classification: " + typeName);
        }
    }

    public BoxedSet() {
        this.contain = new HashMap<>();
    }

    public BoxedSet(String brand, String productName, String productCode, float retailPrice, String description, int stockQuantity, String boxedType, String image) {
        super(brand, productName, productCode, retailPrice, description, stockQuantity, image);
        this.setBoxedType(boxedType);
        this.contain = new HashMap<>();
    }

    public BoxedSet(Product product, String boxedType, Map<Product,Integer> itemList) {
        super(product.getBrand(), product.getProductName(), product.getProductCode(), product.getRetailPrice(), product.getDescription(), product.getStockQuantity(), product.getImageBase64());
        this.setBoxedType(boxedType);
        this.setContain(itemList);
        this.setProductID(product.getProductID());
    }

    public String getBoxedType() {
        return boxedType.getType();
    }

    public void setBoxedType(String typeName) {
        this.boxedType = BoxedType.fromName(typeName);
    }

    public Map<Product, Integer> getContain() {
        return contain;
    }

    public void setContain(Map<Product,Integer> itemList){
        this.contain = new HashMap<>();
        this.contain.putAll(itemList);
    }

    public void addProduct(Product product, int quantity) {
        boolean productExists = false;
        if (contain != null){
            for (Product existingProduct : contain.keySet()) {
                if (existingProduct.getProductID() == product.getProductID()) {
                    int existingQuantity = contain.get(existingProduct);
                    contain.put(existingProduct, existingQuantity + quantity);
                    productExists = true;
                    break;
                }
            }
        } 
        if (!productExists) {
            contain.put(product, quantity);
        }
    }
}
