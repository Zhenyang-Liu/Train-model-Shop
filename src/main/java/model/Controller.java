package model;

public class Controller extends Product {
    private boolean digitalType;

    public Controller(Brand brand, String productName, String productCode, float retailPrice, String description, int stockQuantity, Boolean digitalType) {
        super(brand, productName, productCode, retailPrice, description, stockQuantity);
        this.digitalType = digitalType;
    }

    //Getter and Setter
    /**
     * Retrieves the digital type of the controller.
     *
     * @return whether the type of controller is digital.
     */
    public Boolean getDigitalType() {
        return digitalType;
    }

    /**
     * Sets the digital type of the controller.
     *
     * @param digitakType the boolean value whether the controller is digital.
     */
    public void setDigitalType(Boolean digitalType) {
        this.digitalType = digitalType;
    }


}