package model;

import java.net.URL;

public class Product {
    private int productID;
    private Brand brand; // Association to Brand
    private String productName;
    private String productCode;
    private double retailPrice;
    private String description;
    private int stockQuantity;
    private String imagePath;

    // No-argument constructor
    public Product() {
    }

    /**
     * Constructs a new Product instance with the specified details.
     *
     * @param brand        The brand associated with the product.
     * @param productName  The name of the product.
     * @param productCode  The code of the product.
     * @param d  The retail price of the product.
     * @param description  The description of the product.
     * @param stockQuantity The stock quantity of the product.
     */
    public Product(Brand brand, String productName, String productCode,
                   double d, String description, int stockQuantity) {
        this.setBrand(brand);
        this.setProductName(productName);
        this.setProductCode(productCode);
        this.setRetailPrice(d);
        this.setDescription(description);
        this.setStockQuantity(stockQuantity);
    }

    // Getter and Setter

    /**
     * Retrieves the brand name of the product.
     *
     * @return the brand name associated with this product
     */
    public Brand getBrand() {
        return brand;
    }

    /**
     * Sets the brand of the product.
     *
     * @param brand the Brand object to associate with this product
     */
    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    /**
     * Retrieves the unique identifier for the product.
     *
     * @return the product ID
     */
    public int getProductID() {
        return productID;
    }

     /**
     * Sets the id of the product.
     *
     * @param productID the unique id of this product
     */
    public void setProductID(int productID) {
        this.productID = productID;
    }

    /**
     * Retrieves the name of the product.
     *
     * @return the product name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets the name of the product.
     *
     * @param productName the name to set for this product
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Retrieves the code of the product.
     *
     * @return the product code
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * Sets the code of the product. The code must be valid according to the business rules.
     *
     * @param productCode the code to set for this product
     * @throws IllegalArgumentException if the product code is not valid
     */
    public void setProductCode(String productCode) {
        if (isValidProductCode(productCode)) {
            this.productCode = productCode;
        } else {
            throw new IllegalArgumentException("ProductCode is not valid.");
        }
    }

    /**
     * Retrieves the retail price of the product.
     *
     * @return the retail price
     */
    public double getRetailPrice() {
        return retailPrice;
    }

    /**
     * Sets the retail price of the product. The price must be non-negative.
     *
     * @param retailPrice the non-negative price to set for this product
     * @throws IllegalArgumentException if the provided retailPrice is negative
     */
    public void setRetailPrice(double retailPrice) {
        if (retailPrice < 0) {
            throw new IllegalArgumentException("Retail Price cannot be negative.");
        }
        this.retailPrice = retailPrice;
    }

    /**
     * Retrieves the description of the product.
     *
     * @return the product description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the product.
     *
     * @param description the description to set for this product
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieves the quantity of the product in stock.
     *
     * @return the stock quantity
     */
    public int getStockQuantity() {
        return stockQuantity;
    }

    /**
     * Sets the quantity of the product in stock.
     *
     * @param stockQuantity the quantity to set for this product's stock
     * @throws IllegalArgumentException if the stock quantity is negative
     */
    public void setStockQuantity(int stockQuantity) {
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative.");
        }
        this.stockQuantity = stockQuantity;
    }

    /**
     * Get the image path.
     *
     * @return Returns the image path of the current object.
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Set the image path.
     *
     * @param imagePath The new image path to be set.
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


    // Other Function List
    
    /**
     * Checks if the specified quantity of the product is available in stock.
     *
     * @param quantity the quantity of the product to check
     * @return true if there is sufficient stock, false otherwise
     */
    public boolean checkStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        return stockQuantity >= quantity;
    }

    /**
     * Returns the product type based on the product code.
     * The product code starts with a letter that indicates the product type,
     * followed by a three to five-digit serial number.
     * 
     * @param productCode The product code to analyze.
     * @return The product type as a string, or null if the product code is invalid.
     */
    public String getProductType() {
        // Validate the product code
        // if (isValidProductCode(productCode)) {
        //     return null; // Invalid product code
        // }

        char typeIndicator = productCode.charAt(0);

        switch (typeIndicator) {
            case 'R':
                return "Track";
            case 'C':
                return "Controller";
            case 'L':
                return "Locomotive";
            case 'S':
                return "Rolling Stock";
            case 'M':
                return "Train Set";
            case 'P':
                return "Track Pack";
            default:
                return null; // Invalid type indicator
        }
    }

    /**
     * Get the URL of the product image.
     *
     * @return Returns the URL of the product image if the image path is valid; otherwise, returns null.
     */
    public URL getProductImage() {
        /**
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                return new File(imagePath).toURI().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                // Handle the exception (e.g., return a default image URL or null)
            }
        }
         */

        URL imageUrl = getClass().getResource("/images/tgv.jpeg");

// 如果你需要将 URL 转换为字符串
        return imageUrl;
    }


    // Private validation methods for product code

    /**
     * Validates the product code.
     * The product code must start with a specific letter that corresponds to the product type,
     * followed by a serial number that is between three to five digits long.
     * R - Track codes
     * C - Controller codes
     * L - Locomotive codes
     * S - Rolling stock codes
     * M - Train set codes
     * P - Track pack codes
     * T - Other product codes
     *
     * @param productCode the product code to validate
     * @return true if the product code is valid, false otherwise
     */
    public static boolean isValidProductCode(String productCode) {
        // Regular expression for the product code validation
        String regex = "^[RCLSMPT]\\d{3,5}$";

        return productCode != null && productCode.matches(regex);
    }

    //Test use
    @Override
    public String toString() {
        return "ID: " + this.productID + "; Brand: " + this.getBrand().getBrandName() + "; ProductName: " + this.getProductName();
    }


}
