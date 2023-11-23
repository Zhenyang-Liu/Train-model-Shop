package helper;

import model.User;

import java.util.Set;
import java.util.HashSet;

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
    }

    public User getCurrentUser() {
        return this.currentUser;
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
    // Set<String> permissions = AuthenticationDAO.getPermissionsForUser(loggedInUser.getUserID());
    // UserSession.getInstance().setCurrentUser(loggedInUser);
    // UserSession.getInstance().setPermissions(permissions);

    // // when need user information
    // User currentUser = UserSession.getInstance().getCurrentUser();

    // // when log out
    // UserSession.getInstance().clear();

}

