package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;

public class UserDAO {
    private Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts a new user
     * 
     * @param newUser A user object which will be inserted
     * @param conn The connection object for the database
     * @return {@code true} if the user was added, {@code false} otherwise
     * @throws SQLException If there is an error inserting into database (sometimes because user already exists)
     */
    public boolean insertUser(User newUser, Connection conn) throws SQLException {
        try {
            // Make sure the user is not in the database before adding
            if (doesUserExist(newUser.getUserID())) {
                System.out.println("User not added, they are already in the database");
                return false;
            }

            // Insert user into database as they have not already been added
            String insertSQL = "INSERT INTO User (UserID, Email, Password, Forename, Surname, RoleID) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);
            preparedStatement.setInt(1, newUser.getUserID());
            preparedStatement.setString(2, newUser.getEmail());
            preparedStatement.setString(3, newUser.getPassword());
            preparedStatement.setString(4, newUser.getForename());
            preparedStatement.setString(5, newUser.getSurname());
            preparedStatement.setInt(6, newUser.getRoleID());

            preparedStatement.executeUpdate();
            System.out.println("Successfully added user into database!");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Checks to see if a user is already in the database
     * 
     * @param userID the userID we are checking against
     * @return {@code true} if the user exists, {@code false} otherwise
     * @throws SQLException If there was an error during query
     */
    public boolean doesUserExist(int userID) throws SQLException {
        String checkSQL = "SELECT COUNT(*) FROM User WHERE userID = ?";
        try (PreparedStatement checkStatement = conn.prepareStatement(checkSQL)) {
            checkStatement.setInt(1, userID);
            try (ResultSet results = checkStatement.executeQuery()) {
                if (results.next())
                    return results.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
