package service;


import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;

import model.User;
import service.RoleService;
import DAO.AuthenticationDAO;
import DAO.UserDAO;
import exception.*;

public class RoleService {
    private static PermissionService permission = new PermissionService();

    /**
     * Assigns the "STAFF" role to a specified user.
     *
     * This method checks if the current session has the permission to assign the "STAFF" role.
     * If permission is granted, it assigns the "STAFF" role to the user by calling setStaff from AuthenticationDAO.
     * Returns true if the operation is successful, or false if a DatabaseException occurs.
     *
     * @param userID The ID of the user to be assigned the "STAFF" role.
     * @return true if the role is successfully assigned; false otherwise.
     */
    public static boolean assignStaff(int userID) {
        try{
            if (!permission.hasPermission("ASSIGN_STAFF_ROLE")){
                throw new AuthorizationException("Access denied. Only Manager can assign staff.");
            }
            AuthenticationDAO.setStaff(userID);
            return true;
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return false;
        }
    }

    /**
     * Dismisses the "STAFF" role from a specified user.
     *
     * This method checks if the current session has the permission to dismiss the "STAFF" role.
     * If permission is granted, it removes the "STAFF" role from the user by calling dismissStaff from AuthenticationDAO.
     * Returns true if the operation is successful, or false if a DatabaseException occurs.
     *
     * @param userID The ID of the user from whom the "STAFF" role is to be removed.
     * @return true if the role is successfully dismissed; false otherwise.
     */
    public static boolean dismissStaff(int userID) {
        try{
            if (!permission.hasPermission("ASSIGN_STAFF_ROLE")){
                throw new AuthorizationException("Access denied. Only Manager can assign staff.");
            }
            AuthenticationDAO.dismissStaff(userID);
            return true;
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return false;
        }
    }

    /**
     * Retrieves all users with their roles.
     *
     * This method checks if the current session has the permission to view user roles.
     * If permission is granted, it retrieves all users and their associated roles from the database.
     * Returns a map of User to String, where the String is the role name, or null if a DatabaseException occurs.
     *
     * @return A Map of User to their role name; null if an error occurs.
     */
    public static Map<User,String> getAllUserWithRole() {
        try{
            if (!permission.hasPermission("ASSIGN_STAFF_ROLE")){
                throw new AuthorizationException("Access denied. Only Manager can assign staff.");
            }
            Map<User,String> list = new LinkedHashMap<>();
            ArrayList<User> users = UserDAO.findAllUser();

            for (User user : users) {
                String role = AuthenticationDAO.findRoleByID(user.getUserID());
                list.put(user, role);
            }
            return list;
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return null;
        }
    }

    /**
     * Retrieves a specific user with their role.
     *
     * This method checks if the current session has the permission to view user roles.
     * If permission is granted, it retrieves the specified user and their associated role from the database.
     * Returns a map of User to String, where the String is the role name, or null if a DatabaseException occurs.
     *
     * @param userID The ID of the user whose role is to be retrieved.
     * @return A Map of User to their role name; null if an error occurs.
     */
    public static Map<User,String> getUserWithRole(int userID) {
        try{
            if (!permission.hasPermission("ASSIGN_STAFF_ROLE")){
                throw new AuthorizationException("Access denied. Only Manager can assign staff.");
            }
            Map<User,String> list = new LinkedHashMap<>();
            User user = UserDAO.findUserByID(userID);
            String role = AuthenticationDAO.findRoleByID(user.getUserID());
            list.put(user, role);
            return list;
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return null;
        }
    }
}
