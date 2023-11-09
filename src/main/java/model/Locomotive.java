package model;

import java.util.ArrayList;

public class Locomotive extends Product {
    private Gauge gauge;
    private DCCType dccType;
    private ArrayList<Era> era;

    /**
     * Represents the Digital Command Control (DCC) type of a locomotive.
     * DCC is a standard for a system to operate model railways digitally.
     */
    public enum DCCType {
        ANALOGUE,
        READY,
        FITTED,
        SOUND
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
     * @param gauge        The gauge of the locomotive.
     * @param dccType      The Digital Command Control type of the locomotive.
     * @param era          The era classification of the locomotive.
     */
    public Locomotive(Brand brand, String productName, String productCode, float retailPrice, String description, int stockQuantity, String gauge, String dccType, ArrayList<Era> era) {
        super(brand, productName, productCode, retailPrice, description, stockQuantity);
        this.setGauge(gauge);
        this.setDCCType(dccType);
        this.setEra(era);
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
     * Retrieves the DCC type of the locomotive.
     *
     * @return the DCC type of the locomotive.
     */
    public DCCType getDCCType() {
        return dccType;
    }

    /**
     * Retrieves the DCC type of the locomotive as a string.
     *
     * @return the string representation of the DCC type of the locomotive.
     */
    public String getDCCTypeString() {
        return dccType.name();
    }


    /**
     * Sets the DCC type of this locomotive by converting a string to its corresponding {@code DCCType} enum constant,
     * ignoring case considerations. If the string does not match any {@code DCCType}, an exception is thrown.
     *
     * @param text the string to convert to a {@code DCCType} enum constant
     * @throws IllegalArgumentException if the specified string does not match any {@code DCCType}
     */
    public void setDCCType(String text) {
        for (DCCType type : DCCType.values()) {
            if (type.name().equalsIgnoreCase(text)) {
                this.dccType = type;
                return; // Match found, exit the method
            }
        }
        // If no match was found, then throw the exception
        throw new IllegalArgumentException("No constant with text " + text + " found");
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
