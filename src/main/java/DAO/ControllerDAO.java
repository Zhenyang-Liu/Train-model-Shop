package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Controller;
import model.Product;

public class ControllerDAO extends ProductDAO {

    public static void insertController(Controller controller) throws SQLException {
        int productID = insertProduct(controller);
        String insertSQL = "INSERT INTO Controller (productID, DigitalType) VALUES (?, ?);";
        
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            
            preparedStatement.setInt(1, productID);
            if (controller.getDigitalType()) {
                preparedStatement.setInt(2, 1);
            } else {
                preparedStatement.setInt(2, 0);
            }
            
            int rowsAffected = preparedStatement.executeUpdate();

            // Check if the insert was successful
            if (rowsAffected == 0) {
                throw new SQLException("Creating Controller failed, no rows affected.");
            } 
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error SQL query: " + insertSQL);
            throw e;
        }

       
    }

    public static void updateController(Controller controller) throws SQLException{
        ProductDAO.updateProduct(controller);
        String updateSQL = "UPDATE Controller SET DigitalType = ? WHERE ProductID = ?;";
        
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setInt(2, controller.getProductID());
            if (controller.getDigitalType()) {
                preparedStatement.setInt(2, 1);
            } else {
                preparedStatement.setInt(2, 0);
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void deleteController(int productId) throws SQLException{
        String deleteSQL = "DELETE FROM Controller WHERE ProductID = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, productId);

            int rowsAffected = preparedStatement.executeUpdate();
            
            // Print to Test
            if (rowsAffected > 0) {
                ProductDAO.deleteProduct(productId);
                System.out.println("Controller with ID " + productId + " was deleted successfully.");
            } else {
                System.out.println("No Locomotive was found with ID " + productId + " to delete.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }   
    
    }

    public static Controller findControllerByID(int productID) throws SQLException {
        String selectSQL = "SELECT * FROM Controller WHERE ProductID = ?";
        Controller controller = new Controller();
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, productID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("ProductID");
                Product newProduct = ProductDAO.findProductByID(productId);
                boolean isDigital = resultSet.getInt("DigitalType") == 1;

                controller = new Controller(newProduct, isDigital);
            }
            return controller;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    public static ArrayList<Controller> findControllersByType(boolean isDigital) throws SQLException {
        ArrayList<Controller> controllers = new ArrayList<Controller>();
        String  selectSQL = "SELECT * FROM Controller WHERE DigitalType = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            
            if (isDigital){
                preparedStatement.setString(1, "Digital");
            } else {
                preparedStatement.setString(1, "Analogue");
            }
            
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("ProductID");
                Product newProduct = ProductDAO.findProductByID(productId);
                boolean digital = resultSet.getInt("DigitalType") == 1;

                Controller controller = new Controller(newProduct, digital);
                controllers.add(controller);
            }
            return controllers;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }
    
    public static ArrayList<Controller> findAllControllers() throws SQLException {
        ArrayList<Controller> controllers = new ArrayList<Controller>();
        String  selectSQL = "SELECT * FROM Controller;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("ProductID");
                Product newProduct = ProductDAO.findProductByID(productId);
                boolean digital = resultSet.getInt("DigitalType") == 1;

                Controller controller = new Controller(newProduct, digital);
                controllers.add(controller);
            }
            return controllers;
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }
    
}
