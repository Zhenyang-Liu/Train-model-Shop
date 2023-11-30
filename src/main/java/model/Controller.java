package model;

public class Controller extends Product {
    private boolean isDigital;

    public Controller() {

    }

    public Controller(String brand, String productName, String productCode, float retailPrice, String description, int stockQuantity, boolean isDigital, String image) {
        super(brand, productName, productCode, retailPrice, description, stockQuantity, image);
        this.setDigitalType(isDigital);
    }

    public Controller(Product product, boolean isDigital) {
        super(product.getBrand(), product.getProductName(), product.getProductCode(), product.getRetailPrice(), product.getDescription(), product.getStockQuantity(), product.getImageBase64());
        this.setDigitalType(isDigital);
        this.setProductID(product.getProductID());
    }

    //Getter and Setter
    /**
     * Retrieves the digital type of the controller.
     *
     * @return whether the type of controller is digital.
     */
    public boolean getDigitalType() {
        return isDigital;
    }

    /**
     * Sets the digital type of the controller.
     *
     * @param digitakType the boolean value whether the controller is digital.
     */
    public void setDigitalType(boolean isDigital) {
        this.isDigital = isDigital;
    }


}