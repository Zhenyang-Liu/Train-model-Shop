package model;

public class User {
    private int userID;
    private int roleID;
    private String email;
    private String password;
    private String forename;
    private String surname;

    // No argument constructor
    public User() {}

    // Parameterized constructor
    public User(int roleID, String email, String password, String forename, String surname) {
        this.roleID = roleID;
        this.email = email;
        this.password = password;
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
     * Get roleID
     * 
     * @return the user's current roleID
     */
    public int getRoleID() {
        return roleID;
    }

    /**
     * Updates users roleID
     * 
     * @param roleID the new role ID
     */
    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    /**
     * Gets the users email address
     * 
     * @return the users email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Updates the users email
     * 
     * @param email the new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /** 
     * Gets users hashed password
     * 
     * @return the users hashed password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Updates the users password, must be hashed!
     * 
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
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
