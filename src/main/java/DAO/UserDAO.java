package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import exception.ConnectionException;
import exception.DatabaseException;
import helper.Logging;

import java.util.ArrayList;
import model.User;

public class UserDAO {

    /**
     * Inserts a new user
     * 
     * @param newUser A user object which will be inserted
     * @return {@code true} if the user was added, {@code false} otherwise
     * @throws SQLException If there is an error inserting into database (sometimes because user already exists)
     */
    public static boolean insertUser(User newUser) {
        // Insert user into database as they have not already been added
        String insertSQL = "INSERT INTO User (user_id, forename, surname, email, address) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            // Make sure the user is not in the database before adding
            if (doesUserExist(newUser.getUserID())) {
                System.out.println("User not added, they are already in the database");
                return false;
            }
            
            // Set the variables in SQL command
            preparedStatement.setInt(1, newUser.getUserID());
            preparedStatement.setString(2, newUser.getForename());
            preparedStatement.setString(3, newUser.getSurname());
            preparedStatement.setString(4, newUser.getEmail());
            preparedStatement.setString(5, newUser.getAddress());

            // Run SQL command
            preparedStatement.executeUpdate();
            System.out.println("Successfully added user into database!");
            return true;
        } catch (SQLException e) {
            Logging.getLogger().warning("Error while inserting new user: SQL Excepted\nStacktrace: " + e.getMessage());
        }

        // Default return false as nothing above matched
        return false;
    }

    /**
     * Checks to see if a user is already in the database using userID
     * 
     * @param userID the userID we are checking against
     * @return {@code true} if the user exists, {@code false} otherwise
     * @throws SQLException If there was an error during query
     */
    public static boolean doesUserExist(int userID) throws SQLException{
        String checkSQL = "SELECT COUNT(*) FROM User WHERE user_id = ?";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
        PreparedStatement checkStatement = connection.prepareStatement(checkSQL)) {

            checkStatement.setInt(1, userID);
            try (ResultSet results = checkStatement.executeQuery()) {
                if (results.next())
                    return results.getInt(1) > 0;
            }
        } catch (SQLException e) {
            Logging.getLogger().warning("Could not check if user " + userID + " existed as SQL Excepted\nStacktrace: " + e.getMessage());
            throw e;
        }
        // Default return false as nothing above matched
        return false;
    }

    /**
     * Checks to see if a user is already in the database using email
     * 
     * @param email the email we are checking against
     * @return {@code true} if the user exists, {@code false} otherwise
     * @throws SQLException If there was an error during query
     */
    public static boolean doesUserExist(String email) throws SQLException{
        String checkSQL = "SELECT COUNT(*) FROM User WHERE email = ?";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
        PreparedStatement checkStatement = connection.prepareStatement(checkSQL)) {

            checkStatement.setString(1, email);
            try (ResultSet results = checkStatement.executeQuery()) {
                if (results.next())
                    return results.getInt(1) > 0;
            }
        } catch (SQLException e) {
            Logging.getLogger().warning("Could not check if user " + email + " existed as SQL Excepted\nStacktrace: " + e.getMessage());
            throw e;
        }

        // Default return false as nothing above matched
        return false;
    }

    public static User findUserByEmail(String email) {
        String checkSQL = "SELECT * FROM User WHERE email = ?";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
        PreparedStatement checkStatement = connection.prepareStatement(checkSQL)) {

            checkStatement.setString(1, email);
            try (ResultSet results = checkStatement.executeQuery()) {
                User user = new User();

                // Populate new user
                if (results.next()) {
                    user.setUserID(results.getInt("user_id"));
                    user.setEmail(results.getString("email"));
                    user.setForename(results.getString("forename"));
                    user.setSurname(results.getString("surname"));
                    user.setAddress(results.getString("address"));
                }
                    
                // Return the user
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new User();
    }

    public static User findUserByID(int userID) throws DatabaseException {
        String checkSQL = "SELECT * FROM User WHERE user_id = ?";
        User user = new User();
        
        try (Connection connection = DatabaseConnectionHandler.getConnection();
        PreparedStatement checkStatement = connection.prepareStatement(checkSQL)) {

            checkStatement.setInt(1, userID);
            try (ResultSet results = checkStatement.executeQuery()) {            
                if (results.next()) {
                    user.setUserID(results.getInt("user_id"));
                    user.setEmail(results.getString("email"));
                    user.setForename(results.getString("forename"));
                    user.setSurname(results.getString("surname"));
                }
            }
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed", e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
        return user;
    }

    /**
     * Retrieves all users from the database.
     *
     * This method queries the database to retrieve all user records. It constructs and returns a list of User objects,
     * each representing a user in the database. If a database access issue occurs, it throws a corresponding exception.
     *
     * @return An ArrayList of User objects representing all users in the database.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static ArrayList<User> findAllUser() throws DatabaseException {
        String selectSQL = "SELECT * FROM User;";
        ArrayList<User> userList = new ArrayList<>();
    
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement checkStatement = connection.prepareStatement(selectSQL);
            ResultSet results = checkStatement.executeQuery()) {

            while (results.next()) {
                User user = new User();
                user.setUserID(results.getInt("user_id"));
                user.setEmail(results.getString("email"));
                user.setForename(results.getString("forename"));
                user.setSurname(results.getString("surname"));
                userList.add(user);
            }
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed", e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
        return userList;
    }

}
