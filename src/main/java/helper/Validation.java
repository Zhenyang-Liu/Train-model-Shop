package helper;

import model.User;

public class Validation {
    
    public static boolean isCurrentUser(int user_id){
        User currentUser = UserSession.getInstance().getCurrentUser();
        return currentUser.getUserID() == user_id;
    }
}
