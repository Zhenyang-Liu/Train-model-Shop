package service;

import java.util.ArrayList;

import DAO.AddressDAO;
import model.Address;
import model.User;
import exception.*;
import helper.*;

public class AddressService {
    private static PermissionService permission = new PermissionService();
    
    /**
     * Adds a new address for the current user.
     *
     * This method checks if the current user has permission to edit their own address.
     * It then either associates an existing address with the user or inserts a new address into the database.
     * Returns true if the operation is successful, or false if a DatabaseException occurs.
     *
     * @param address The Address object to be added.
     * @return true if the address is successfully added; false otherwise.
     */
    public static boolean addAddress(Address address) {
        try {
            User currentUser = UserSession.getInstance().getCurrentUser();
            if (!permission.hasPermission(currentUser.getUserID(),"EDIT_OWN_ADDRESS")){
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
            return true;
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return false;
        }
    }

    /**
     * Updates the current user's address.
     *
     * This method checks if the current user has permission to edit their own address.
     * It deletes the user's existing address and then associates a new or existing address with the user.
     * Returns true if the operation is successful, or false if a DatabaseException occurs.
     *
     * @param newAddress The new Address object to update.
     * @return true if the address is successfully updated; false otherwise.
     */
    public static boolean updateAddress(Address newAddress) {
        try {
            User currentUser = UserSession.getInstance().getCurrentUser();
            if (!permission.hasPermission(currentUser.getUserID(), "EDIT_OWN_ADDRESS")){
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
            return true;
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return false;
        }
    }

    /**
     * Retrieves a list of addresses matching a specific postcode.
     *
     * This method queries the database for addresses that match the given postcode.
     * Returns a list of Address objects or null if a DatabaseException occurs.
     *
     * @param postcode The postcode to search for addresses.
     * @return An ArrayList of Address objects matching the given postcode, or null if an error occurs.
     */
    public static ArrayList<Address> getAddressByPostcode(String postcode) {
        try {
            return AddressDAO.findByPostcode(postcode);
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return null;
        }
    }

    /**
     * Retrieves the detailed information of an address by its ID.
     *
     * This method queries the database for an address matching the given ID.
     * Returns the address details as an Address object or null if a DatabaseException occurs.
     *
     * @param addressID The ID of the address to retrieve.
     * @return An Address object containing the details of the found address, or null if an error occurs.
     */
    public static Address getAddressDetailByID(int addressID) {
        try {
            return AddressDAO.findByAddressID(addressID);
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return null;
        }
    }

}
