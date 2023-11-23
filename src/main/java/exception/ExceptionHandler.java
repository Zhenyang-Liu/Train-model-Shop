package exception;

public class ExceptionHandler {

    public static void printErrorMessage(Exception e) {
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        int stackTraceLength = stackTraceElements.length;
    
        int start = Math.max(0, stackTraceLength - 5);
        int end = Math.min(5,stackTraceLength);
        System.err.println("Error Message: " + e.getMessage());
        System.err.println("Stack Trace:");
        for (int i = 0; i < end; i++) {
            System.err.println(stackTraceElements[i]);
        }
        for (int i = start; i < stackTraceLength; i++) {
            System.err.println(stackTraceElements[i]);
        }
    }
}
