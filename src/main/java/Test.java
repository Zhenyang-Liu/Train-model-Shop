import model.Brand;
import model.Era;
import model.Product;

import java.sql.SQLException;

import DAO.*;

public class Test {
    public static void main(String[] args) {
        // Initialize the database connection handler
        DatabaseConnectionHandler dbHandler = new DatabaseConnectionHandler();
        try {
            // Open a connection to the database
            dbHandler.openConnection();

            // Initialize DAO with the database connection
            ProductDAO opera = new ProductDAO(dbHandler.getConnection());
            Brand brand = new Brand("Bachmann", "UK");
            brand.setBrandID(1);
            Product product = new Product(brand, "Test Insert", "T000", 8.8, "??", 0);
        
            // Perform database operations using the DAO
            int id = opera.insertProduct(product);
            opera.getAllProduct();
            opera.deleteProduct(id);
            opera.getAllProduct();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the database connection
            dbHandler.closeConnection();
        }
    }
}