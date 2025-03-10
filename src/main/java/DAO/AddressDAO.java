package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.ArrayList;

import exception.*;
import helper.Logging;
import model.Address;

public class AddressDAO {

    /**
     * Inserts a new address into the database and associates it with a user.
     *
     * This method inserts a new address record into the database. If the insertion is successful,
     * it retrieves the generated address ID and associates this new address with the specified user
     * by calling the insertUserAddress method.
     *
     * @param userID  The ID of the user to whom the address belongs.
     * @param address The Address object containing the address details.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static void insertAddress(int userID, Address address) throws DatabaseException {
        String insertSQL = "INSERT INTO Address (house_number, road_name, city_name, postcode) VALUES (?, ?, ?, ?);";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, address.getHouseNumber());
            preparedStatement.setString(2, address.getRoadName());
            preparedStatement.setString(3, address.getCity());
            preparedStatement.setString(4, address.getPostcode());
    
            int rowsAffected = preparedStatement.executeUpdate();
    
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int addressID = generatedKeys.getInt(1);
                        insertUserAddress(userID, addressID);
                    }
                }
            } else {
                throw new ActionFailedException("Creating cart failed, no rows affected.");
            }
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

    /**
     * Associates an existing address with a user in the database.
     *
     * This method inserts a record into the User_Address table to create an association between
     * a user and an address using their respective IDs.
     *
     * @param userID    The user ID to associate with the address.
     * @param addressID The address ID to be associated with the user.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static void insertUserAddress(int userID, int addressID) throws DatabaseException {
        String insertSQL = "INSERT INTO User_Address (user_id, address_id) VALUES (?, ?);";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, addressID);
    
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new ActionFailedException("Insert User_Address failed, no rows affected.");
            }
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

    /**
     * Deletes the address association of a user.
     *
     * This method removes the association between the specified user and their address in the User_Address table.
     * It does not delete the address itself from the database, only the association.
     *
     * @param userID The user ID whose address association is to be deleted.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static void deleteAddressByUser(int userID) throws DatabaseException {
        String deleteSQL = "DELETE FROM User_Address WHERE user_id = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, userID);

            preparedStatement.executeUpdate();
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

    /**
     * Retrieves an address from the database based on its ID.
     *
     * This method searches the database for an address that matches the provided address ID.
     * If a matching address is found, it constructs and returns an Address object populated with
     * the address details. If no matching address is found, an empty Address object is returned.
     *
     * @param addressID The ID of the address to search for.
     * @return An Address object representing the found address or an empty Address object if no match is found.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static Address findByAddressID(int addressID) throws DatabaseException {
        String selectSQL = "SELECT * FROM Address WHERE address_id = ?;";
        Address address = new Address();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, addressID);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int newID = resultSet.getInt("address_id");
                String houseNumber = resultSet.getString("house_number");
                String roadName = resultSet.getString("road_name");
                String cityName = resultSet.getString("city_name");
                String postCode = resultSet.getString("postcode");

                address = new Address(newID, houseNumber, roadName, cityName, postCode);
            }
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
        return address;
    }

    /**
     * Retrieves a list of addresses matching a specific postcode.
     *
     * This method queries the database for all addresses that match the given postcode.
     * It constructs a list of Address objects, each representing an address in the database with the specified postcode.
     * In case of a database access issue, it logs the error using the Logging service.
     *
     * @param postcode The postcode used to filter addresses.
     * @return An ArrayList of Address objects that match the specified postcode.
     */
    public static ArrayList<Address> findByPostcode(String postcode) {
        String selectSQL = "SELECT * FROM Address WHERE postcode = ?;";
        ArrayList<Address> addressList = new ArrayList<>();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, postcode);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int newID = resultSet.getInt("address_id");
                String houseNumber = resultSet.getString("house_number");
                String roadName = resultSet.getString("road_name");
                String cityName = resultSet.getString("city_name");
                String postCode = resultSet.getString("postcode");
                Address address = new Address(newID, houseNumber, roadName, cityName, postCode);

                addressList.add(address);
            }
        } catch (SQLTimeoutException e){
            Logging.getLogger().warning("Error finding address by postcode: SQL Timed out. Stacktrace: \n" + e.getMessage());
        } catch (SQLException e) {
            Logging.getLogger().warning("Error finding address by postcode: SQL Exception. Stacktrace: \n" + e.getMessage());
        }
        return addressList;
    }

    /**
     * Retrieves an address from the database based on house number and postcode.
     *
     * This method searches for an address in the database that matches the given house number and postcode.
     * If a matching address is found, it is returned as an Address object. If no matching address is found,
     * an empty Address object is returned.
     *
     * @param houseNumber The house number of the address to search for.
     * @param postcode The postcode of the address to search for.
     * @return An Address object representing the found address or an empty Address object if no match is found.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static Address findByNumberAndPostcode(String houseNumber, String postcode) throws DatabaseException {
        String selectSQL = "SELECT * FROM Address WHERE house_number = ? AND postcode = ?;";
        Address address = new Address();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, houseNumber);
            preparedStatement.setString(2, postcode);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int addressID = resultSet.getInt("address_id");
                String newHouseNumber = resultSet.getString("house_number");
                String roadName = resultSet.getString("road_name");
                String cityName = resultSet.getString("city_name");
                String postCode = resultSet.getString("postcode");

                address = new Address(addressID, newHouseNumber, roadName, cityName, postCode);
            }

        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
        return address;
    }

    /**
     * Retrieves the address ID associated with a specific user from the database.
     *
     * This method queries the User_Address table to find the address ID linked to the given user ID.
     * Returns the address ID if found, or 0 if the user does not have an associated address or in case of an SQL error.
     *
     * @param userID The ID of the user whose address ID is to be retrieved.
     * @return The address ID associated with the user, or 0 if not found or in case of an error.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static int findAddressIDByUser(int userID) throws DatabaseException {
        String selectSQL = "SELECT address_id FROM User_Address WHERE user_id = ?;";
        int addressID = 0;

        try (Connection connection = DatabaseConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, userID);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                addressID = resultSet.getInt("address_id");
            }
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
        return addressID;
    }

}
