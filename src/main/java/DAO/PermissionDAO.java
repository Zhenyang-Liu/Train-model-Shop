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

public class PermissionDAO {

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
    public Set<String> getUserPermissions(int userID) throws DatabaseException {
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
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
        return permissions;
    }
}
