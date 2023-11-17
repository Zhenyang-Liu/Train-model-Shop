package model;

public class Login{

    private final int loginID;
    private final int userID;

    private String username;
    private String passwordHash;
    private String passwordSalt;

    private int failedAttempts;
    private Timestamp lastLoginAttempt;

    private boolean lockoutEnabled;
    private Timestamp lockoutEnd;

    /**
     * Sets the loginID
     *
     * @param loginID the id of the login information
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

    public void setUserID(int userID){
        this.userID = userID;
    }

    public int getUserID(){
        return this.userID;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }

    public void setPasswordHash(String hash){
        this.passwordHash = hash;
    }

    public String getPasswordHash(){
        return this.passwordHash;
    }

    public void setPasswordSalt(String salt){
        this.passwordSalt = salt;
    }

    public String getPasswordSalt(){
        return this.passwordSalt;
    }

    public void setPassword(String hash, String salt){
        this.setPasswordHash(hash);
        this.setPasswordSalt(salt);
    }

    public void setFailedAttempts(int failedAttempts){
        this.failedAttempts = failedAttempts;
    }

    public void incrementFailedAttempts(){
        this.failedAttempts++;
    }

    public int getFailedAttempts(){
        return this.failedAttempts;
    }

    public void setLastLoginAttempt(Timestamp time){
        this.lastLoginAttempt = time;
    }

    public Timestamp getLastLoginAttempt(){
        return this.lastLoginAttempt;
    }

    public void setLockoutEnabled(boolean lockoutEnabled){
        this.lockoutEnabled = lockoutEnabled;
    }

    public void isLockoutEnabled(){
        return this.lockoutEnabled;
    }

    public void setLockoutEnd(Timestamp time){
        this.lockoutEnd = time;
    }

    public Timestamp getLockoutEnd(){
        return this.lockoutEnd;
    }

}
