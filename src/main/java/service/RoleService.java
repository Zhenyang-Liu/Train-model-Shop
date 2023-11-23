package service;

import DAO.AuthenticationDAO;
import exception.AuthorizationException;
import exception.DatabaseException;
import exception.ExceptionHandler;

public class RoleService {
    private static PermissionService permission = new PermissionService();

    /**
     * Assigns the "STAFF" role to a specified user.
     *
     * This method checks if the current session has the permission to assign the "STAFF" role.
     * If permission is granted, it assigns the "STAFF" role to the user by calling the setStaff method
     * from the AuthenticationDAO. This method is typically used by managers to grant staff-level access to users.
     *
     * @param userID The ID of the user to be assigned the "STAFF" role.
     * @throws DatabaseException if there is an issue with database access.
     * @throws AuthorizationException if the current session lacks the necessary permission.
     */
    public static void assignStaff(int userID) throws DatabaseException{
        try{
            if (!permission.hasPermission("ASSIGN_STAFF_ROLE")){
                throw new AuthorizationException("Access denied. Only Manager can assign staff.");
            }
            AuthenticationDAO.setStaff(userID);
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            throw e;
        }
    }

    /**
     * Removes the "STAFF" role from a specified user.
     *
     * This method checks if the current session has the permission to dismiss the "STAFF" role.
     * If permission is granted, it removes the "STAFF" role from the user by calling the dismissStaff method
     * from the AuthenticationDAO. This method is typically used by managers to revoke staff-level access from users.
     *
     * @param userID The ID of the user from whom the "STAFF" role is to be removed.
     * @throws DatabaseException if there is an issue with database access.
     * @throws AuthorizationException if the current session lacks the necessary permission.
     */
    public static void dismissStaff(int userID) throws DatabaseException{
        try{
            if (!permission.hasPermission("ASSIGN_STAFF_ROLE")){
                throw new AuthorizationException("Access denied. Only Manager can assign staff.");
            }
            AuthenticationDAO.dismissStaff(userID);
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            throw e;
        }
    }
}
