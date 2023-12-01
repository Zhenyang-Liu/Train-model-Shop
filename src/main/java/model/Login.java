package model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import helper.Logging;


public class Login{

    private int loginID;
    private int userID;

    private String passwordHash;
    private String passwordSalt;

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
        this.setPasswordSalt(new String(salt));
        this.setPasswordHash(hash(password, getPasswordSalt()));
    }

    private static String bytesToHex(byte[] bytes){
        final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
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
            Logging.getLogger().warning("Could not use SHA-512 to hash uh-oh not cool");
            return "";
        };
        return bytesToHex(md.digest((toHash + salt).getBytes()));
    }

    /**
     * Check if a given password matches the password stored by this login information
     * 
     * @param password the UNHASHED password of the user
     * @return True if the password matches the hashed password stored else False
     */
    public boolean doesPasswordMatch(String password){
        return hash(password, getPasswordSalt()).equals(getPasswordHash());
    }

}
