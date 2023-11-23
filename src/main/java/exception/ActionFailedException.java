package exception;

public class ActionFailedException extends DatabaseException {
    public ActionFailedException(String message) {
        super(message);
    }
    
    public ActionFailedException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
