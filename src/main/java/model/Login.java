package model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;

public class Login{

    private int loginID;
    private int userID;

    private String passwordHash;
    private String passwordSalt;

    private int failedAttempts;
    private Timestamp lastLoginAttempt;

    private boolean lockoutEnabled;
    private Timestamp lockoutEnd;

    public Login() {}

    public Login(int userID) {
        this.setUserID(userID);
    }

    public Login(int userID, String passwordHash, String passwordSalt){
        this.setUserID(userID);
        this.setPassword(passwordHash, passwordSalt);
    }

    /**
     * Sets the loginID
     *
     * @param loginID the id of the login information. Autoincrements in the table so dont set this unless needed :D
     */
    public void setLoginID(int loginID){
        this.loginID = loginID;
    }

    /**
     * Gets the loginID of the login information
     *
     * @return the private login ID
     */
    public int getLoginID(){
        return this.loginID;
    }

    /**
     * Sets the private userID that this login information relates to
     *
     * @param userID the private userId of the concerned user
     */
    public void setUserID(int userID){
        this.userID = userID;
    }

    /**
     * Gets the private userID that this login relates to
     *
     * @return the userID
     */
    public int getUserID(){
        return this.userID;
    }

    /**
     * Sets the hashed password of the user
     *
     * @param hash the HASHED password
     */
    public void setPasswordHash(String hash){
        this.passwordHash = hash;
    }

    /**
     * Gets the hashed password of the user
     *
     * @return the hashed password
     */
    public String getPasswordHash(){
        return this.passwordHash;
    }

    /**
     * Sets the salt used while hashing the user's password
     *
     * @param salt the salt used
     */
    public void setPasswordSalt(String salt){
        this.passwordSalt = salt;
    }

    /**
     * Gets the password salt used in the creation of this user's passwordHash
     *
     * @return the password salt
     */
    public String getPasswordSalt(){
        return this.passwordSalt;
    }

    /**
     * Sets all password information for the user concerned
     *
     * @param hash the password HASH
     * @param salt the salt used to generate the password hash
     */
    public void setPassword(String hash, String salt){
        this.setPasswordHash(hash);
        this.setPasswordSalt(salt);
    }

    /**
     * Sets all password information given a plaintext password
     * Hashes the password using SHA-512, generates a salt using SecureRandom and stores
     * 
     * @param password plaintext password to store
     */
    public void setPassword(String password){
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        this.setPasswordSalt(salt.toString());
        this.setPasswordHash(hash(password, getPasswordSalt()));
    }

    /**
     * Hashes a string given a salt
     * 
     * @param toHash string to hash using SHA-512
     * @param salt salt to use in hashing
     * @return
     */
    private String hash(String toHash, String salt){
        MessageDigest md;
        try{
            md = MessageDigest.getInstance("SHA-512");
        }catch(NoSuchAlgorithmException e){
            System.out.println("Could not use SHA-512 uh oh");
            return "";
        };
        
        md.update(salt.getBytes(StandardCharsets.UTF_8));
        return md.digest(toHash.getBytes(StandardCharsets.UTF_8)).toString();
    }

    /**
     * Check if a given password matches the password stored by this login information
     * 
     * @param password the UNHASHED password of the user
     * @return True if the password matches the hashed password stored else False
     */
    public boolean doesPasswordMatch(String password){
        return hash(password, getPasswordSalt()).equals(password);
    }

    /**
     * Sets the number of failed login attempts for this user
     *
     * @param failedAttempts the number of attemps the user is currently at
     */
    public void setFailedAttempts(int failedAttempts){
        this.failedAttempts = failedAttempts;
    }

    /**
     * Increases the number of failed attempts for this user by 1
     */
    public void incrementFailedAttempts(){
        this.failedAttempts++;
    }

    /**
     * Gets the number of failed attempts
     *
     * @return the number of failed attempts
     */
    public int getFailedAttempts(){
        return this.failedAttempts;
    }

    /**
     * Sets the time the user last attempted to login
     *
     * @param time the time the user last attempted to login
     */
    public void setLastLoginAttempt(Timestamp time){
        this.lastLoginAttempt = time;
    }

    /**
     * Gets the time the user last attempted to logi
     *
     * @return the timestamp of the last login attempt
     */
    public Timestamp getLastLoginAttempt(){
        return this.lastLoginAttempt;
    }

    /**
     * Flags the user's account as locked or unlocked
     *
     * @param lockoutEnabled whether the account is locked or unlocked
     */
    public void setLockoutEnabled(boolean lockoutEnabled){
        this.lockoutEnabled = lockoutEnabled;
    }

    /**
     * Checks whether the account is locked
     *
     * @return whether lockout is enabled for this login
     */
    public boolean isLockoutEnabled(){
        return this.lockoutEnabled;
    }

    /**
     * Sets the time at which this account will be unlocked
     *
     * @param time the time for the account to be unlocked
     */
    public void setLockoutEnd(Timestamp time){
        this.lockoutEnd = time;
    }

    /**
     * Gets the time at which this account will be unlocked
     *
     * @return the timestamp at which the user's account will be unlocked
     */
    public Timestamp getLockoutEnd(){
        return this.lockoutEnd;
    }
}
