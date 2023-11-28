package service;

import DAO.UserDAO;
import exception.AuthorizationException;
import exception.DatabaseException;
import exception.ExceptionHandler;
import model.User;

public class UserService {
    public static User getUserInfo(int userID) {
        try{
            if (!PermissionService.hasPermission("VIEW_CUSTOMER_DETAILS")
             && !PermissionService.hasPermission(userID, "VIEW_OWN_ORDERS")){
                throw new AuthorizationException("Access denied. Only Manager can assign staff.");
            }
            User user = UserDAO.findUserByID(userID);
            return user; 
        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return null;
        }
    }
}
