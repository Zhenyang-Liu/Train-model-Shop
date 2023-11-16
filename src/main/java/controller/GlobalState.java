package controller;

import java.util.concurrent.atomic.AtomicBoolean;

public class GlobalState {
    private static final AtomicBoolean isLoggedIn = new AtomicBoolean(false);

    public static boolean isLoggedIn() {
        return isLoggedIn.get();
    }

    public static void setLoggedIn(boolean loggedIn) {
        isLoggedIn.set(loggedIn);
    }
}
