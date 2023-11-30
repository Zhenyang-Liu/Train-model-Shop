package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.Set;

import java.util.HashSet;

import exception.ConnectionException;
import exception.DatabaseException;
import exception.NotFoundException;
import helper.Logging;

public class AuthenticationDAO {

    /**
     * Retrieves the set of permissions associated with a user based on their roles.
     *
     * This method queries the database to find all permissions linked to the roles of a specified user.
     * It collects these permissions into a set and returns them. Each permission is identified by its name.
     *
     * @param userID The ID of the user whose permissions are to be retrieved.
     * @return A Set of strings, where each string represents a permission name.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static Set<String> getUserPermissions(int userID) {
        Set<String> permissions = new HashSet<>();
        String selectSQL = "SELECT p.permission_name FROM Permission p " +
                     "JOIN Role_Permission rp ON p.permission_id = rp.permission_id " +
                     "JOIN User_Role ur ON rp.role_id = ur.role_id " +
                     "WHERE ur.user_id = ?";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, userID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    permissions.add(resultSet.getString("permission_name"));
                }
            }
        } catch (SQLTimeoutException e){
            Logging.getLogger().warning("Error getting user permissions: SQL timed out\n Stacktrace: " + e.getMessage());
        } catch (SQLException e) {
            Logging.getLogger().warning("Error getting user permissions: SQL exception\n Stacktrace: " + e.getMessage());
        }
        return permissions;
    }

    
    /**
     * Sets the "CUSTOMER" role for a user in the database.
     *
     * This method assigns a "CUSTOMER" role to a user by inserting a record into the User_Role table.
     *
     * @param userID The ID of the user for whom the "CUSTOMER" role is to be set.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static void setDefaultRole(int userID) throws DatabaseException {
        String insertSQL = "INSERT INTO User_Role (user_id, role_id) VALUES (?, ?);";
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, getRoleId("CUSTOMER"));

            int rowsAffected = preparedStatement.executeUpdate();
            
            if (rowsAffected == 0) {
                Logging.getLogger().warning("Insert Role failed, no rows affected.");
                throw new SQLException("Insert Role failed, no rows affected.");
            }
        } catch (SQLTimeoutException e) {
            Logging.getLogger().warning("Database connection failed\nStackTrace: " + e.getMessage());
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            Logging.getLogger().warning("Failed to add default role\nStackTrace: " + e.getMessage());
            throw new DatabaseException(e.getMessage(),e);
        }
    }

    /**
     * Updates the role of a user to "STAFF" in the database.
     *
     * This method changes the user's role to "STAFF" by updating the User_Role table. It retrieves the role ID
     * for "STAFF" using the getRoleId method and updates the relevant record for the specified user.
     * This is typically used to grant staff-level access to a user.
     *
     * @param userID The ID of the user whose role is to be updated to "STAFF".
     * @throws DatabaseException if there is an issue with database access.
     */
    public static void setStaff(int userID) throws DatabaseException {
        String updateSQL = "UPDATE User_Role SET role_id = ? WHERE user_id = ?;";
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            
            preparedStatement.setInt(1, getRoleId("STAFF"));
            preparedStatement.setInt(2, userID);
            preparedStatement.executeUpdate();
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

    /**
     * Reverts the role of a user from "STAFF" to "CUSTOMER" in the database.
     *
     * This method changes a user's role back to "CUSTOMER" by updating the User_Role table.
     * It retrieves the role ID for "CUSTOMER" using the getRoleId method and updates the relevant
     * record for the specified user. This is typically used to remove staff-level access from a user.
     *
     * @param userID The ID of the user whose role is to be reverted to "CUSTOMER".
     * @throws DatabaseException if there is an issue with database access.
     */
    public static void dismissStaff(int userID) throws DatabaseException {
        String updateSQL = "UPDATE User_Role SET role_id = ? WHERE user_id = ?;";
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            
            preparedStatement.setInt(1, getRoleId("CUSTOMER"));
            preparedStatement.setInt(2, userID);
            preparedStatement.executeUpdate();
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

    public static String findRoleByID(int userID) throws DatabaseException {
        String roleName = null;
        String selectSQL = "SELECT Role.role_name " +
                         "FROM User_Role " +
                         "JOIN Role ON User_Role.role_id = Role.role_id " +
                         "WHERE User_Role.user_id = ?";

         try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, userID);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    roleName = resultSet.getString("role_name");
                }
            }
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
        return roleName;
    }

    /**
     * Retrieves the ID of a role based on its name.
     *
     * This method queries the database for a role matching the provided name and returns the role's ID.
     * If the role is not found, it throws a NotFoundException.
     *
     * @param roleName The name of the role whose ID is to be retrieved.
     * @return The ID of the role.
     * @throws DatabaseException if there is an issue with database access.
     * @throws NotFoundException if the role is not found in the database.
     */
    private static int getRoleId(String roleName) throws DatabaseException {
        String selectSQL = "SELECT role_id FROM Role WHERE role_name = ?;";
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, roleName.toUpperCase());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("role_id");
            } else {
                throw new NotFoundException("Role "+roleName+" not found");
            }
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

}
