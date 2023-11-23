package model;

public class Brand {
    private int brandID;
    private String brandName;
    private String country;

    // No-argument constructor
    public Brand() {
    }

    // Parameterized constructor without brandID
    public Brand(String brandName, String country) {
        this.brandName = brandName;
        this.country = country;
    }
    
    @Override
    public String toString(){
        return this.brandName;
    }
    
    // Getter and Setter

    /**
     * Retrieves the brand name of the product.
     *
     * @return the brand name associated with this product
     */
    public int getBrandID() {
        return brandID;
    }

    public void setBrandID(int brandID) {
        this.brandID = brandID;
    }
    
    /**
     * Retrieves the name of the brand.
     *
     * @return the name of the brand
     */
    public String getBrandName() {
        return brandName;
    }

    /**
     * Sets the name of the brand.
     *
     * @param brandName the new name to be set for the brand
     */
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    /**
     * Retrieves the country associated with the brand.
     *
     * @return the country where the brand is based or originates from
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country associated with the brand.
     *
     * @param country the country to be set for the brand
     */
    public void setCountry(String country) {
        this.country = country;
    }


}
