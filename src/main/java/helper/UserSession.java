package helper;
import java.util.HashSet;
import java.util.Set;

import model.User;
import service.PermissionService;

public class UserSession {
    private static UserSession instance;
    private User currentUser;
    private Set<String> permissions = new HashSet<>();

    private UserSession() {}

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        Set<String> permissions = PermissionService.getUserPermissions(currentUser.getUserID());
        setPermissions(permissions);
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public boolean isLoggedIn() {
        return this.currentUser != null;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public Set<String> getPermissions() {
        return this.permissions;
    }

    public boolean hasPermission(String permission) {
        return this.permissions.contains(permission);
    }

    public void clear() {
        this.currentUser = null;
        this.permissions.clear();
    }


    // After login successful
    // UserSession.getInstance().setPermissions(permissions);
    
    // // when need user information
    // User currentUser = UserSession.getInstance().getCurrentUser();

    // // when log out
    // UserSession.getInstance().clear();
}

