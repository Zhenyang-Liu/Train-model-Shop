package model;

public class Era {
    private int eraCode;
    private String description;

    // Constructor to initialize 
    public Era(int eraCode, String description) {
        this.setEracode(eraCode);
        this.setDescription(description);
    }

    // Getter and setter 
    public int getEracode() {
        return eraCode;
    }

    public void setEracode(int eraCode) {
        if (isValidEracode(eraCode)) {
            this.eraCode = eraCode;
        } else {
            throw new IllegalArgumentException("Era Code is not valid.");
        }
    }

    //
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (isValidDescription(description)) {
            this.description = description;
        } else {
            throw new IllegalArgumentException("Description is not valid.");
        }
    }

    // Private validation methods for each attribute
    private boolean isValidEracode(int eraCode) {
        //
        return eraCode >= 1 && eraCode <= 11;
    }

    private boolean isValidDescription(String description) {
        //
        return description != null;
    }



}
