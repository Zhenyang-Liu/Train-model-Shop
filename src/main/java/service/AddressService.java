package service;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static Address getAddressByUser() {
        try {
            int userID = UserSession.getInstance().getCurrentUser().getUserID();
            if (!permission.hasPermission(userID, "EDIT_OWN_ADDRESS")){
                throw new AuthorizationException("Access denied. Users can only access their own address.");
            }
            int addressID = AddressDAO.findAddressIDByUser(userID);
            return AddressDAO.findByAddressID(addressID);
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return null;
        }
    }

    public static boolean isAddressEmpty(Address address) {
        if (address == null) {
            return true;
        }
        
        if (address.getHouseNumber() == null || address.getHouseNumber().isEmpty()) {
            return true;
        }
        
        if (address.getCity() == null || address.getCity().isEmpty()) {
            return true;
        }
        
        if (address.getRoadName() == null || address.getRoadName().isEmpty()) {
            return true;
        }
        
        if (address.getPostcode() == null || address.getPostcode().isEmpty()) {
            return true;
        }

        return false;
    }

    public static boolean isValidUKPostcode(String postcode) {
        final String UK_POSTCODE_PATTERN = "^[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$";
        Pattern pattern = Pattern.compile(UK_POSTCODE_PATTERN);
        Matcher matcher = pattern.matcher(postcode);
        return matcher.matches();
    }

}
