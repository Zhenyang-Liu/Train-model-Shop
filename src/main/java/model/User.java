package model;

public class User {
    private int userID;
    private String forename;
    private String surname;

    public User(int id, String forename, String surname) {
        this.userID = id;
        this.forename = forename;
        this.surname = surname;
    }

    // Getters and Setters
    
    /**
     * Gets userID
     * 
     * @return the user's private ID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Updates the user's id
     * 
     * @param id the user's private ID
     */
    public void setUserID(int id) {
        this.userID = id;
    }

    /**
     * Gets the users forename
     * 
     * @return the users forename
     */
    public String getForename() {
        return forename;
    }

    /**
     * Updates the users forename
     * 
     * @param forename the new forename
     */
    public void setForename(String forename) {
        this.forename = forename;
    }

    /**
     * Gets the users surname
     * 
     * @return the users surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Updates the users surname
     * 
     * @param surname the new surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

}