package model;

import java.util.Random;

import DAO.UserDAO;

public class User {
    private int userID;
    private String email;
    private String forename;
    private String surname;
    private String address;

    public User() {}

    public User(String email, String forename, String surname, String address) {
        this.userID = generateUserID();
        this.email = email;
        this.forename = forename;
        this.surname = surname;
        this.address = address;
    }

    public User(int id, String email, String forename, String surname, String address) {
        this.userID = id;
        this.email = email;
        this.forename = forename;
        this.surname = surname;
        this.address = address;
    }

    /**
     * Create a userID for a new user
     * @return the users newID
     */
    public int generateUserID() {
        boolean valid = false;
        while (!valid) {
            // Generate ID
            Random r = new Random();
            int id = r.nextInt(65536);  // 16-bit random ID
    
            // Validate ID
            boolean doesUserExist = UserDAO.doesUserExist(id);
            if (!doesUserExist) {
                valid = true;
                return id;
            }
        }
        return -1;
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
     * Gets email
     * 
     * @return the user's private ID
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email
     * 
     * @param email the user's email
     */
    public void setEmail(String email) {
        this.email = email;
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

    /**
     * Gets the users current address
     * 
     * @return the users current address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets user address
     * 
     * @param address the new address
     */
    public void setAddress(String address) {
        this.address = address;
    }
}