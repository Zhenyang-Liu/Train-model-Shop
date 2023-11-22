package helper;

import model.User;

public class UserSession {
    private static UserSession instance;
    private User currentUser; 

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

    public void clear() {
        this.currentUser = null;
    }

    // // After login successful
    // UserSession.getInstance().setCurrentUser(loggedInUser);

    // // when need user information
    // User currentUser = UserSession.getInstance().getCurrentUser();

    // // when log out
    // UserSession.getInstance().clear();

}

