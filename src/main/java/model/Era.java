package model;

public class Era {
    private String eraCode;
    private String description;

    // Constructor to initialize a Book object with its attributes
    public Era(String eraCode, String description) {
        this.setEracode(eraCode);
        this.setDescription(description);
    }

    // Getter and setter methods for the ISBN attribute with validation
    public String getEracode() {
        return eraCode;
    }

    public void setEracode(String eraCode) {
        if (isValidEracode(eraCode)) {
            this.eraCode = eraCode;
        } else {
            throw new IllegalArgumentException("Era Code is not valid.");
        }
    }

    // Getter and setter methods for the Title attribute with validation
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
    private boolean isValidEracode(String eraCode) {
        // Implement ISBN validation logic here (e.g., length, format)
        return eraCode != null;
    }

    private boolean isValidDescription(String description) {
        // Implement title validation logic here (e.g., length)
        return description != null;
    }


    @Override
    public String toString() {
        return "{ " +
            " eraCode='" + getEracode() + "'" +
            ", description='" + getDescription() +
            " }";
    }

}
