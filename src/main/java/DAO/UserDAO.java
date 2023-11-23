package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            e.printStackTrace();
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
    public static boolean doesUserExist(int userID) {
        String checkSQL = "SELECT COUNT(*) FROM User WHERE user_id = ?";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
        PreparedStatement checkStatement = connection.prepareStatement(checkSQL)) {

            checkStatement.setInt(1, userID);
            try (ResultSet results = checkStatement.executeQuery()) {
                if (results.next())
                    return results.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
    public static boolean doesUserExist(String email) {
        String checkSQL = "SELECT COUNT(*) FROM User WHERE email = ?";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
        PreparedStatement checkStatement = connection.prepareStatement(checkSQL)) {

            checkStatement.setString(1, email);
            try (ResultSet results = checkStatement.executeQuery()) {
                if (results.next())
                    return results.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
}
