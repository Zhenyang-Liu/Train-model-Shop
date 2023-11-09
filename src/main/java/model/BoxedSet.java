package model;

import java.util.ArrayList;

import model.RollingStock.RollingStockType;

public class BoxedSet extends Product{
    private BoxedType boxedType;
    private ArrayList<Product> contain;

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

    public BoxedSet(Brand brand, String productName, String productCode, float retailPrice, String description, int stockQuantity, String boxedType, ArrayList<Product> contain) {
        super(brand, productName, productCode, retailPrice, description, stockQuantity);
        this.setBoxedType(boxedType);
        this.setContain(contain);
    }

    public String getBoxedType() {
        return boxedType.getType();
    }

    public void setBoxedType(String typeName) {
        this.boxedType = BoxedType.fromName(typeName);
    }

    public ArrayList<Product> getContain() {
        return contain;
    }

    public void setContain(ArrayList<Product> productList) {
        this.contain = productList;
    }
}
