package model;

public class Track extends Product {
    private Gauge gauge;

    public Track() {
    }

    /**
     * Constructs a new Track instance with the specified details.
     *
     * @param productID    The unique identifier for the product.
     * @param brand        The brand associated with the product.
     * @param productName  The name of the product.
     * @param productCode  The code of the product.
     * @param retailPrice  The retail price of the product.
     * @param description  The description of the product.
     * @param stockQuantity The stock quantity of the product.
     * @param gauge        The gauge of the track.
     */
    public Track(String brand, String productName, String productCode, float retailPrice, String description, int stockQuantity, String gauge) {
        super(brand, productName, productCode, retailPrice, description, stockQuantity);
        this.setGauge(gauge);
    }

    public Track(Product product, String gauge) {
        super(product.getBrand(), product.getProductName(), product.getProductCode(), product.getRetailPrice(), product.getDescription(), product.getStockQuantity());
        this.setGauge(gauge);
        this.setProductID(product.getProductID());
    }

    // Getters and setters

    /**
     * Retrieves the gauge of the track.
     *
     * @return the gauge of the track.
     */
    public String getGauge() {
        return gauge.getName();
    }

    /**
     * Sets the gauge of the track using the gauge name.
     * The gauge name is case-insensitive and must match one of the predefined gauge names.
     *
     * @param gaugeName The name of the gauge to set, which is converted to the {@link Gauge} enum.
     * @throws IllegalArgumentException If the gauge name does not match any predefined gauge names.
     */
    public void setGauge(String gaugeName){
        this.gauge = Gauge.fromName(gaugeName);
    }

    /**
     * Sets the gauge of the track directly using a {@link Gauge} enum instance.
     *
     * @param gauge The {@link Gauge} enum instance to set for the track.
     */
    public void setGauge(Gauge gauge){
        this.gauge = gauge;
    }

}

