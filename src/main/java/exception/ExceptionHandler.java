package exception;

public class ExceptionHandler {

    /**
     * Prints an error message and a condensed stack trace to standard error.
     *
     * This method prints the message of the provided exception and a brief version of its stack trace.
     * If the stack trace has 10 or fewer elements, it prints the entire stack trace.
     * If the stack trace is longer, it prints the first five and last five elements to provide a snapshot of the error context.
     *
     * @param e The exception whose message and stack trace are to be printed.
     */
    public static void printErrorMessage(Exception e) {
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        int stackTraceLength = stackTraceElements.length; 
        System.err.println("Error Message: " + e.getMessage());
        System.err.println("Stack Trace:");
    
        if (stackTraceLength <= 10) {
            for (int i = 0; i < stackTraceLength; i++) {
                System.err.println(stackTraceElements[i]);
            }
        } else {
            for (int i = 0; i < 5; i++) {
                System.err.println(stackTraceElements[i]);
            }
            for (int i = stackTraceLength - 5; i < stackTraceLength; i++) {
                System.err.println(stackTraceElements[i]);
            }
        }
    }
}
