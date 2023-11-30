package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.ArrayList;

import exception.ConnectionException;
import exception.DatabaseException;
import helper.Logging;
import model.Controller;
import model.Product;

public class ControllerDAO extends ProductDAO {

    public static void insertController(Controller controller) throws DatabaseException {
        int productID = insertProduct(controller);
        String insertSQL = "INSERT INTO Controller (product_id, digital_type) VALUES (?, ?);";
        
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            
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
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }

       
    }

    public static void updateController(Controller controller) throws DatabaseException {
        ProductDAO.updateProduct(controller);
        String updateSQL = "UPDATE Controller SET digital_type = ? WHERE product_id = ?;";
        
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setInt(2, controller.getProductID());
            if (controller.getDigitalType()) {
                preparedStatement.setInt(1, 1);
            } else {
                preparedStatement.setInt(1, 0);
            }

            preparedStatement.executeUpdate();
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

    public static void deleteController(int productId) throws DatabaseException {
        String deleteSQL = "DELETE FROM Controller WHERE product_id = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, productId);

            int rowsAffected = preparedStatement.executeUpdate();
            
            if (rowsAffected > 0) {
                ProductDAO.deleteProduct(productId);
                Logging.getLogger().info("Controller with ID " + productId + " was deleted successfully.");
            } else {
                Logging.getLogger().warning("No Locomotive was found with ID " + productId + " to delete.");
            }
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }  
    
    }

    public static Controller findControllerByID(int productID) throws DatabaseException {
        String selectSQL = "SELECT * FROM Controller WHERE product_id = ?";
        Controller controller = new Controller();
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, productID);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                Product newProduct = ProductDAO.findProductByID(productId);
                boolean isDigital = resultSet.getInt("digital_type") == 1;
                controller = new Controller(newProduct, isDigital);
            }
        } catch (SQLTimeoutException e) {
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
        return controller;
    }
    
    public static ArrayList<Controller> findControllersByType(boolean isDigital) {
        ArrayList<Controller> controllers = new ArrayList<Controller>();
        String  selectSQL = "SELECT * FROM Controller WHERE digital_type = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
        
            if (isDigital){
                preparedStatement.setString(1, "Digital");
            } else {
                preparedStatement.setString(1, "Analogue");
            }
            
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                Product newProduct = null;
                try{
                    newProduct = ProductDAO.findProductByID(productId);
                }catch(DatabaseException e){
                    Logging.getLogger().warning("Error when finding controllers of type " + 
                        (isDigital ? "digital" : "non-digital") + ": could not find product " 
                            + productId + "\n Stacktrace: " + e.getMessage());
                    continue;
                }
                boolean digital = resultSet.getInt("digital_type") == 1;

                Controller controller = new Controller(newProduct, digital);
                controllers.add(controller);
            }
        } catch (SQLTimeoutException e) {
            Logging.getLogger().warning("Error when finding controllers: SQL Timed out\n Stacktrace: " + e.getMessage());
        } catch (SQLException e) {
            Logging.getLogger().warning("Error when finding controllers: SQL Excepted :0\nStacktrace: " + e.getMessage());
        }
        return controllers;
    }
    
    public static ArrayList<Controller> findAllControllers() {
        ArrayList<Controller> controllers = new ArrayList<Controller>();
        String  selectSQL = "SELECT * FROM Controller;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                Product newProduct = null;
                try{
                    newProduct = ProductDAO.findProductByID(productId);
                }catch(DatabaseException e){
                    Logging.getLogger().warning("Error when finding all controllers: could not find product " 
                            + productId + "\n Stacktrace: " + e.getMessage());
                    continue;
                }
                boolean digital = resultSet.getInt("digital_type") == 1;

                Controller controller = new Controller(newProduct, digital);
                controllers.add(controller);
            }
        } catch (SQLTimeoutException e) {
            Logging.getLogger().warning("Error when finding controllers: SQL Timed out\n Stacktrace: " + e.getMessage());
        } catch (SQLException e) {
            Logging.getLogger().warning("Error when finding controllers: SQL Excepted :0\nStacktrace: " + e.getMessage());
        }
        return controllers;
    }
    
}
