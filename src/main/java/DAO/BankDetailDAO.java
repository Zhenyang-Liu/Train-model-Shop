package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;

import exception.ConnectionException;
import exception.DatabaseException;
import model.BankDetail;

public class BankDetailDAO {
    
    /**
     * Inserts a new bank detail record into the database.
     *
     * @param bankDetail The BankDetail object containing the details to be inserted.
     * @throws DatabaseException If a database-related error occurs.
     */
    public static void insertBankDetail(BankDetail bankDetail) throws DatabaseException {
        String insertSQL = "INSERT INTO Bank_Detail "
            + "(card_name, card_holder_name, card_number, expiry_date, security_code, user_id) "
            + "VALUES (?,?,?,?,?,?);";
        
        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            
            preparedStatement.setString(1, bankDetail.getCardName());
            preparedStatement.setString(2, bankDetail.getCardHolderName());
            preparedStatement.setString(3, bankDetail.getCardNumber());
            preparedStatement.setString(4, bankDetail.getExpiryDate());
            preparedStatement.setString(5, bankDetail.getSecurityCode());
            preparedStatement.setInt(6, bankDetail.getUserID());
    
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Insert Bank Detail failed, no rows affected.");
            }
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

    /**
     * Updates an existing bank detail record in the database.
     *
     * @param bankDetail The BankDetail object containing the updated details.
     * @throws DatabaseException If a database-related error occurs.
     */
    public static void updateBankDetail(BankDetail bankDetail) throws DatabaseException {
        String updateSQL = "UPDATE Bank_Detail "
            + "SET card_name = ?, card_holder_name = ?, card_number = ?, expiry_date = ?, security_code = ? "
            + "WHERE user_id = ?;";
    
        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
    
            preparedStatement.setString(1, bankDetail.getCardName());
            preparedStatement.setString(2, bankDetail.getCardHolderName());
            preparedStatement.setString(3, bankDetail.getCardNumber());
            preparedStatement.setString(4, bankDetail.getExpiryDate());
            preparedStatement.setString(5, bankDetail.getSecurityCode());
            preparedStatement.setInt(6, bankDetail.getUserID());
    
            preparedStatement.executeUpdate();

        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed", e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Deletes a bank detail record by its ID.
     *
     * @param bankDetailID The ID of the bank detail to be deleted.
     * @throws DatabaseException If a database-related error occurs.
     */
    public static void deleteBankDetail(int bankDetailID) throws DatabaseException {
        String deleteSQL = "DELETE FROM Bank_Detail WHERE bank_detail_id = ?;";
    
        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
    
            preparedStatement.setInt(1, bankDetailID);
    
            int rowsAffected = preparedStatement.executeUpdate();
    
            if (rowsAffected == 0) {
                throw new SQLException("Delete Bank Detail failed, no rows affected.");
            }
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed", e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }    

    /**
     * Deletes all bank detail records associated with a user by their user ID.
     *
     * @param userID The ID of the user whose bank details will be deleted.
     * @throws DatabaseException If a database-related error occurs.
     */
    public static void deleteBankDetailByUserID(int userID) throws DatabaseException {
        String deleteSQL = "DELETE FROM Bank_Detail WHERE user_id = ?;";
    
        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
    
            preparedStatement.setInt(1, userID);
    
            int rowsAffected = preparedStatement.executeUpdate();
    
            if (rowsAffected == 0) {
                throw new SQLException("Delete Bank Detail failed, no rows affected.");
            }
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed", e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }    

    /**
     * Finds and retrieves a bank detail record by user ID.
     *
     * @param userID The ID of the user for whom the bank detail is retrieved.
     * @return A BankDetail object representing the retrieved bank detail record.
     * @throws DatabaseException If a database-related error occurs.
     */
    public static BankDetail findBankDetail(int userID) throws DatabaseException {
        String selectSQL = "SELECT * FROM Bank_Detail WHERE user_id = ?;";
        BankDetail bankDetail = null;

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int detailID = resultSet.getInt("bank_detail_id");
                String cardName = resultSet.getString("card_name");
                String cardHolderName = resultSet.getString("card_holder_name");
                String cardNumber = resultSet.getString("card_number");
                String expiryDate = resultSet.getString("expiry_date");
                String securityCode = resultSet.getString("security_code");

                bankDetail = new BankDetail(detailID,userID, cardName, cardHolderName, cardNumber, expiryDate, securityCode);
            }
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed", e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }

        return bankDetail;
    }


}
