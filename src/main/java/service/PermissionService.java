package service;

import helper.UserSession;

public class PermissionService {

    /**
     * Checks if the current user session has a specified permission.
     *
     * This method checks if the current user session has the specified permission. It returns false 
     * immediately if the permission name contains "OWN", suggesting a restriction on owning permissions.
     * Otherwise, it checks the user session for the specified permission.
     *
     * @param permissionName The name of the permission to check.
     * @return true if the current user session has the specified permission; false otherwise.
     */
    public boolean hasPermission(String permissionName) {
        if (permissionName.contains("OWN")) {
            return false;
        }
        return UserSession.getInstance().hasPermission(permissionName);
    }

    /**
     * Checks if the current user session has a specified permission for a specific user.
     *
     * This method first checks if the current user session has the basic permission. If the permission name 
     * contains "OWN", it further checks if the current user's ID matches the provided userID, implying an ownership check.
     * Otherwise, it returns the result of the basic permission check.
     *
     * @param userID The ID of the user for whom to check the permission.
     * @param permissionName The name of the permission to check.
     * @return true if the current user session has the specified permission for the provided user; false otherwise.
     */
    public boolean hasPermission(int userID, String permissionName) {
        UserSession session = UserSession.getInstance();
        boolean hasBasicPermission = session.hasPermission(permissionName);

        if (permissionName.contains("OWN")) {
            return hasBasicPermission && userID == session.getCurrentUser().getUserID();
        }

        return hasBasicPermission;
    }
}
