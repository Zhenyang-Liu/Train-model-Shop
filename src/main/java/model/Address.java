package model;

public class Address {
    private int id;
    private String houseNumber;
    private String roadName;
    private String city;
    private String postcode;

    public Address() {
    }
    
    public Address(String houseNumber, String roadName, String city, String postcode) {
        this.houseNumber = houseNumber;
        this.roadName = roadName;
        this.city = city;
        this.postcode = postcode;
    }

    public Address(int id, String houseNumber, String roadName, String city, String postcode) {
        this(houseNumber, roadName, city, postcode);
        this.id = id;
    }

    // Getter and Setter
    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }
    
    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public boolean isEmpty() {
        return getHouseNumber().isEmpty() || getRoadName().isEmpty() || getCity().isEmpty() || getPostcode().isEmpty();
    }
}