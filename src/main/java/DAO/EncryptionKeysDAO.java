package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;

import exception.ConnectionException;
import exception.DatabaseException;
import exception.ExceptionHandler;


public class EncryptionKeysDAO {
    
    /**
     * Inserts an encryption key into the database.
     *
     * This method adds a new encryption key to the EncryptionKeys table and returns the generated key ID.
     * Throws an SQLException if no rows are affected during the insert operation.
     *
     * @param keyValue The value of the encryption key to be inserted.
     * @return The ID of the newly inserted encryption key.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static int insertKey(String keyValue) throws DatabaseException {
        String insertSQL = "INSERT INTO EncryptionKeys (key_value) VALUES (?);";
        int keyID = 0;

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, keyValue);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                // Retrieve the generated keys (if any)
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            } else {
                throw new SQLException("Creating encryption key failed, no rows affected.");
            }
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connection failed", e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }

        return keyID;
    }

    /**
     * Inserts a user-key association into the database.
     *
     * This method creates a new entry in the User_Key table linking a specific user to a specific key.
     * It ensures the association is successfully added to the database, throwing an exception if no rows are affected.
     *
     * @param userID The ID of the user.
     * @param keyID The ID of the key to be associated with the user.
     * @return The key ID if the operation is successful.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static int insertUserKey(int userID, int keyID) throws DatabaseException {
        String insertSQL = "INSERT INTO User_Key (user_id,key_id) VALUES (?,?);";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, keyID);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Insert User & key failed, no rows affected."); 
            } 
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connection failed", e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }

        return keyID;
    }

    /**
     * Finds an encryption key by its ID in the database.
     *
     * This method retrieves the value of an encryption key from the EncryptionKeys table based on the given key ID.
     * Returns the key value if found, or an empty string if not found.
     *
     * @param keyID The ID of the encryption key to retrieve.
     * @return The value of the encryption key found, or an empty string if not found.
     * @throws DatabaseException If a database error occurs.
     */
    public static String findKey(int keyID) throws DatabaseException {
        String selectSQL = "SELECT key_value FROM EncryptionKeys WHERE key_id = ?;";
        String keyValue = "";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, keyID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                keyValue = resultSet.getString("key_value");
            }
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connection failed", e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
        return keyValue;
    }

    /**
     * Finds the encryption key ID associated with a specific user.
     *
     * This method queries the User_Key table to find the encryption key ID associated with the given user ID.
     * Returns the key ID if found, -1 if an SQL or SQLTimeoutException occurs.
     *
     * @param userID The ID of the user whose encryption key ID is to be retrieved.
     * @return The ID of the encryption key associated with the user, or -1 if an error occurs.
     */
    public static int findUserKey(int userID) {
        String selectSQL = "SELECT key_id FROM User_Key WHERE user_id = ?;";
        int keyID = 0;

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                keyID = resultSet.getInt("key_id");
            }
        } catch (SQLTimeoutException e) {
            ExceptionHandler.printErrorMessage(e);
            return -1;
        } catch (SQLException e) {
            ExceptionHandler.printErrorMessage(e);
            return -1;
        }
        return keyID;
    }

}
