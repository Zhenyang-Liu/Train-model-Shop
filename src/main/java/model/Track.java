package model;

public class Track extends Product {
    private TrackType trackType;
    private Gauge gauge;

    //Represents the type of a track.
    public enum TrackType {
        SINGLE_STRAIGHT("single straight"),
        DOUBLE_STRAIGHT("double straight"),
        FIRST_RADIUS_CURVE("first radius curve"),
        SECOND_RADIUS_CURVE("second radius curve"),
        THIRD_RADIUS_CURVE("third radius curve"),
        LEFT_HAND_POINT("left hand point"),
        RIGHT_HAND_POINT("right hand point"),
        LEFT_HAND_CROSSOVER("left hand crossover"),
        RIGHT_HAND_CROSSOVER("right hand crossover");

        private final String type;
    
        TrackType(String type) {
            this.type = type;
        }
    
        public String getType() {
            return type;
        }

        public static TrackType fromName(String typeName) {
            for (TrackType type : TrackType.values()) {
                if (type.getType().equalsIgnoreCase(typeName)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid Rolling Stock classification: " + typeName);
        }
    }

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
     * @param trackType    The type of the track.
     * @param gauge        The gauge of the track.
     */
    public Track(Brand brand, String productName, String productCode, float retailPrice, String description, int stockQuantity, String trackType, String gauge) {
        super(brand, productName, productCode, retailPrice, description, stockQuantity);
        this.setTrackType(trackType);
        this.setGauge(gauge);
    }

    public Track(Product product, String trackType, String gauge) {
        super(product.getBrand(), product.getProductName(), product.getProductCode(), product.getRetailPrice(), product.getDescription(), product.getStockQuantity());
        this.setGauge(gauge);
        this.setTrackType(trackType);
    }

    // Getters and setters

    /**
     * Retrieves the type of the track.
     *
     * @return the type of the track.
     */
    public String getTrackType() {
        return trackType.getType();
    }

    /**
     * Sets the track type of this product based on the provided text.
     * The method matches the text to the name of the enumeration values of TrackType,
     * ignoring case considerations.
     *
     * @param text the string representation of the track type to be set
     * @throws IllegalArgumentException if the text does not match any track type
     */
    public void setTrackType(String text) {
        for (TrackType type : TrackType.values()) {
            if (type.name().equalsIgnoreCase(text)) {
                this.trackType = type;
                return;
            }
        }
        throw new IllegalArgumentException("No track type with text " + text + " found");
    }

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

