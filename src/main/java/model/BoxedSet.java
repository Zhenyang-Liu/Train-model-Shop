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

    }

    public BoxedSet(Brand brand, String productName, String productCode, float retailPrice, String description, int stockQuantity, String boxedType) {
        super(brand, productName, productCode, retailPrice, description, stockQuantity);
        this.setBoxedType(boxedType);
        this.contain = new HashMap<>();
    }

    public BoxedSet(Product product, String boxedType) {
        super(product.getBrand(), product.getProductName(), product.getProductCode(), product.getRetailPrice(), product.getDescription(), product.getStockQuantity());
        this.setBoxedType(boxedType);
        this.contain = new HashMap<>();
        this.setProductID(product.getProductID());
    }

    public BoxedSet(Product product, String boxedType, Map<Product, Integer> itemList) {
        super(product.getBrand(), product.getProductName(), product.getProductCode(), product.getRetailPrice(), product.getDescription(), product.getStockQuantity());
        this.setBoxedType(boxedType);
        this.contain = itemList;
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

    public void addProduct(Product product, int quantity) {
        this.contain.put(product, quantity);
    }
}
