package model;

import java.util.ArrayList;

public class RollingStock extends Product {
    private RollingStockType type;
    private Gauge gauge;
    private ArrayList<Era> era;

    public enum RollingStockType {
        CARRIAGE("Carriage"),
        WAGON("Wagon");
    
        private final String type;
    
        RollingStockType(String type) {
            this.type = type;
        }
    
        public String getType() {
            return type;
        }

        public static RollingStockType fromName(String typeName) {
            for (RollingStockType type : RollingStockType.values()) {
                if (type.getType().equalsIgnoreCase(typeName)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid Rolling Stock classification: " + typeName);
        }
        
    }

    public RollingStock(Brand brand, String productName, String productCode, float retailPrice, String description, int stockQuantity,  String type, String gauge, ArrayList<Era> era) {
        super(brand, productName, productCode, retailPrice, description, stockQuantity);
        this.setRollingStockType(type);
        this.setGauge(gauge);
        this.setEra(era);
    }
    
    public String getRollingStockType() {
        return type.getType();
    }
    public void setRollingStockType(String typeName){
        this.type = RollingStockType.fromName(typeName);
    }
    
    /**
     * Retrieves the gauge of the locomotive.
     *
     * @return the gauge of the locomotive.
     */
    public String getGauge() {
        return gauge.getName();
    }

    /**
     * Sets the gauge of the locomotive using the gauge name.
     * The gauge name is case-insensitive and must match one of the predefined gauge names.
     *
     * @param gaugeName The name of the gauge to set, which is converted to the {@link Gauge} enum.
     * @throws IllegalArgumentException If the gauge name does not match any predefined gauge names.
     */
    public void setGauge(String gaugeName){
        this.gauge = Gauge.fromName(gaugeName);
    }

    /**
     * Sets the gauge of the locomotive directly using a {@link Gauge} enum instance.
     *
     * @param gauge The {@link Gauge} enum instance to set for the locomotive.
     */
    public void setGauge(Gauge gauge){
        this.gauge = gauge;
    }

    /**
     * Retrieves the era associated with the locomotive.
     *
     * @return the era associated with the locomotive.
     */
    public ArrayList<Era> getEra() {
        return era;
    }

    
    /**
     * Sets the era of the locomotive.
     *
     * @param era 
     */
    public void setEra(ArrayList<Era> era){
       this.era = era;
    }


}

