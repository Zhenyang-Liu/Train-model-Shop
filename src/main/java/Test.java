import db.DatabaseOperations;
import model.Era;
import db.DatabaseConnectionHandler;


public class Test {
    public static void main(String[] args) {
        DatabaseConnectionHandler databaseConnectionHandler = new DatabaseConnectionHandler();
        DatabaseOperations databaseOperations = new DatabaseOperations();
        try {
            databaseConnectionHandler.openConnection();

            // Adding a era to the database.
            Era book1 = new Era(978, "To Kill a Mockingbird");
            databaseOperations.insertEra(book1, databaseConnectionHandler.getConnection());

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            databaseConnectionHandler.closeConnection();
        }
    }
}
