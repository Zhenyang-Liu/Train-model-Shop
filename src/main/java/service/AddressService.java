package service;

import java.util.ArrayList;

import DAO.AddressDAO;
import model.Address;
import model.User;
import exception.*;
import helper.*;

public class AddressService {
    
    /**
     * Adds an address for the current user.
     *
     * This method first retrieves the current user. It then checks if the address already exists in the database
     * by matching the house number and postcode. If the address exists, it links the existing address to the user.
     * If the address does not exist, it inserts the new address into the database and associates it with the user.
     *
     * @param address The Address object to be added.
     * @throws DatabaseException if there is an issue with database access or if the operation fails.
     */
    public static void addAddress(Address address) throws DatabaseException {
        try {
            User currentUser = UserSession.getInstance().getCurrentUser();
            if (!Validation.isCurrentUser(currentUser.getUserID())){
                throw new AuthorizationException("Access denied. Users can only access their own address.");
            }
            // Check whether the address has existed in the database.
            Address existAddress = AddressDAO.findByNumberAndPostcode(address.getHouseNumber(), address.getPostcode());
            if (existAddress.getHouseNumber() != null) {
                // TODO: This function may be changed based on the way of address storage.
                AddressDAO.insertUserAddress(currentUser.getUserID(), existAddress.getID());
            } else {
                AddressDAO.insertAddress(currentUser.getUserID(), address);
            }
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            throw e;
        }
    }

    /**
     * Updates the current user's address.
     *
     * This method first verifies if the current user is authorized to update their address.
     * It then deletes the user's existing address and checks if the new address already exists in the database.
     * If it does, it associates the existing address with the user. Otherwise, it inserts the new address into the
     * database and associates it with the user.
     *
     * @param newAddress The new Address object to update.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static void updateAddress(Address newAddress) throws DatabaseException {
        try {
            User currentUser = UserSession.getInstance().getCurrentUser();
            if (!Validation.isCurrentUser(currentUser.getUserID())){
                throw new AuthorizationException("Access denied. Users can only access their own address.");
            }

            AddressDAO.deleteAddressByUser(currentUser.getUserID());
            Address existAddress = AddressDAO.findByNumberAndPostcode(newAddress.getHouseNumber(), newAddress.getPostcode());
            if (existAddress.getHouseNumber() != null) {
                // TODO: This function may be changed based on the way of address storage.
                AddressDAO.insertUserAddress(currentUser.getUserID(), existAddress.getID());
            } else {
                AddressDAO.insertAddress(currentUser.getUserID(), newAddress);
            }
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            throw e;
        }
    }

    /**
     * Retrieves a list of addresses matching a specific postcode.
     *
     * This method queries the database for addresses that match the given postcode
     * and returns a list of Address objects.
     *
     * @param postcode The postcode to search for addresses.
     * @return An ArrayList of Address objects matching the given postcode.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static ArrayList<Address> getAddressByPostcode(String postcode) throws DatabaseException  {
        try {
            return AddressDAO.findByPostcode(postcode);
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            throw e;
        }
    }

    /**
     * Retrieves the detailed information of an address by its ID.
     *
     * This method queries the database for an address matching the given ID and returns
     * the address details as an Address object.
     *
     * @param addressID The ID of the address to retrieve.
     * @return An Address object containing the details of the found address.
     * @throws DatabaseException if there is an issue with database access.
     */
    public static Address getAddressDetailByID(int addressID) throws DatabaseException {
        try {
            return AddressDAO.findByAddressID(addressID);
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            throw e;
        }
    }

}
