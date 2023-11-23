package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.*;

public class LoginDAO{
    /**
     * Inserts new login information into the database
     *
     * @param login the login information to insert
     * @return the loginID of the login inserted
     * @throws SQLException
     */
    public static boolean insertLoginDetails(Login login){
        String stmt = "INSERT INTO Login (user_id, password_hash, password_salt, failed_attempts," +
            "last_login_attempt, lockout_enabled, lockout_end) VALUES (?,?,?,?,?,?,?)";
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(stmt)) {

            if(doesLoginExist(login.getUserID())){
                System.out.println("Login information for this user " + login.getUserID() + " already exists in the database.");
                return false;
            }
            preparedStatement.setInt(1, login.getUserID());
            preparedStatement.setString(2, login.getPasswordHash());
            preparedStatement.setString(3, login.getPasswordSalt());
            preparedStatement.setInt(4, login.getFailedAttempts());
            preparedStatement.setTimestamp(5, login.getLastLoginAttempt());
            preparedStatement.setBoolean(6, login.isLockoutEnabled());
            preparedStatement.setTimestamp(7, login.getLockoutEnd());

            preparedStatement.executeUpdate();
            return true;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates login details given that the login provided has a userID that corresponds to a userID in the database
     *
     * @param login the login details to update
     * @return @code{true} if the details are updated successfully otherwise @code{false}
     * @throws SQLException
     */
    public static boolean updateLoginDetails(Login login) throws SQLException {
        String stmt = "UPDATE Login SET password_hash = ?, password_salt = ?, failed_attempts = ?, " +
            "last_login_attempt = ?, lockout_enabled = ?, lockout_end = ? WHERE user_id = ?";

        if(!doesLoginExist(login.getUserID())){
            System.out.println("No login exists in the database with the given userID.");
            return false;
        }

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(stmt)) {
            preparedStatement.setString(1, login.getPasswordHash());
            preparedStatement.setString(2, login.getPasswordSalt());
            preparedStatement.setInt(3, login.getFailedAttempts());
            preparedStatement.setTimestamp(4, login.getLastLoginAttempt());
            preparedStatement.setBoolean(5, login.isLockoutEnabled());
            preparedStatement.setTimestamp(6, login.getLockoutEnd());
            preparedStatement.setInt(7, login.getUserID());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating login details failed as no order could be found ");
            }
            
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Finds login information given the user's unique user ID
     *
     * @param userID the unique userID of the user
     * @return the login information of the user
     * @throws SQLException
     */
    public static Login findLoginByUserID(int userID) throws SQLException {
        String stmt = "SELECT * FROM Login WHERE user_id = ?";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(stmt)) {

            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            Login login = new Login();

            while (resultSet.next()) {
                login.setLoginID(resultSet.getInt("login_id"));
                login.setUserID(resultSet.getInt("user_id"));
                login.setPassword(resultSet.getString("password_hash"), resultSet.getString("password_salt"));
                login.setFailedAttempts(resultSet.getInt("failed_attempts"));
                login.setLastLoginAttempt(resultSet.getTimestamp("last_login_atttempt"));
                login.setLockoutEnabled(resultSet.getBoolean("lockout_enabled"));
                login.setLockoutEnd(resultSet.getTimestamp("lockout_end"));
            }

            return login;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Checks if a password hash matches the stored password hash for a given userID
     *
     * @param userID the userId of the password to check
     * @param passwordhash the hash of the password to check
     * @return @code{true} if the passwords match else @code{false}
     */
    public static boolean doesPasswordMatch(int userID, String passwordhash){
        try{
            Login dbLoginDetails = findLoginByUserID(userID);
            return dbLoginDetails.getPasswordHash().equals(passwordhash);
        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes login details given the user ID of the row
     *
     * @param userID userID to delete from the database
     * @return @code{true} if the row is deleted otherwise @code{false}
     */
    public static boolean deleteLoginDetailsFromUser(int userID){
        try{
            return deleteFromGenericID("DELETE FROM login WHERE user_id = ?", userID);
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes login details given the login ID of the row
     *
     * @param loginID loginID to delete from the database
     * @return @code{true} if the row is deleted otherwise @code{false}
     */
    public static boolean deleteLoginDetailsFromLogin(int loginID){
        try{
            return deleteFromGenericID("DELETE FROM Login WHERE login_id = ?", loginID);
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes login details given either of the unique keys stored in this table
     * @param stmt The statement to bind the ID to
     * @param id The ID to use
     * @return
     * @throws SQLException
     */
    private static boolean deleteFromGenericID(String stmt, int id) throws SQLException{
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(stmt)) {
            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }
            return true;
        }
    }

    /**
     * Checks if login information already exists for a given userID, as only one entry for each user can exist
     *
     * @param userID The userID to check
     * @return whether or not login information pertaining to the given user exists already
     * @throws SQLException if you are silly
     */
    public static boolean doesLoginExist(int userID) throws SQLException{
        String stmt = "SELECT COUNT(*) FROM Login WHERE user_id = ?";
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(stmt)) {
            preparedStatement.setInt(1, userID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return false;
        }
    }

