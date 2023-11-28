package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.ArrayList;

import exception.ConnectionException;
import exception.DatabaseException;
import model.Gauge;
import model.Track;
import model.Product;

public class TrackDAO extends ProductDAO {

    /**
     * Inserts a new track record into the database.
     *
     * @param track The Track object to be inserted.
     * @throws DatabaseException If a database error occurs.
     */
    public static void insertTrack(Track track) throws DatabaseException {
        int productID = insertProduct(track);
        String insertSQL = "INSERT INTO Track (product_id, gauge) VALUES (?, ?, ?);";
        
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            
            preparedStatement.setInt(1, productID);
            preparedStatement.setString(2, track.getGauge());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // EraDAO.insertEra(productID, rollingStock.getEra());
            } else {
                throw new SQLException("Creating track failed, no rows affected.");
            }
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }

    }

    /**
     * Updates an existing track record in the database.
     *
     * @param track The Track object with updated information.
     * @throws DatabaseException If a database error occurs.
     */
    public static void updateTrack(Track track) throws DatabaseException{
        ProductDAO.updateProduct(track);
        String updateSQL = "UPDATE Track SET gauge = ? WHERE product_id = ?;";
        
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            
            preparedStatement.setString(1, track.getGauge());
            preparedStatement.setInt(2, track.getProductID());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // EraDAO.deleteEra(rollingStock.getProductID());
                // EraDAO.insertEra(rollingStock.getProductID(), rollingStock.getEra());
            } else {
                throw new SQLException("Update Track failed, no rows affected.");
            }
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

    /**
     * Deletes a track record from the database by product ID.
     *
     * @param productId The ID of the track product to be deleted.
     * @throws DatabaseException If a database error occurs.
     */
    public static void deleteTrack(int productId) throws DatabaseException{
        String deleteSQL = "DELETE FROM Track WHERE product_id = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, productId);

            int rowsAffected = preparedStatement.executeUpdate();
            
            if (rowsAffected > 0) {
                ProductDAO.deleteProduct(productId);
            } 
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
    }

    /**
     * Finds a Track record in the database by product ID.
     *
     * @param productID The ID of the Track product to be retrieved.
     * @return A Track object representing the retrieved Track record | null if can't find.
     * @throws DatabaseException If a database error occurs.
     */
    public static Track findTrackByID(int productID) throws DatabaseException {
        String selectSQL = "SELECT * FROM RollingStock WHERE product_id = ?;";
        Track track = new Track();
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, productID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                Product newProduct = ProductDAO.findProductByID(productId);
                String newGauge = resultSet.getString("gauge");

                track = new Track(newProduct, newGauge);
            }
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
        return track;
    }

    /**
     * Retrieves a list of tracks from the database that match the specified gauge.
     *
     * @param gauge The gauge to filter tracks by.
     * @return An ArrayList of track objects that match the specified gauge | null if can't find.
     * @throws DatabaseException If a database error occurs.
     */
    public static ArrayList<Track> findTracksByGauge(Gauge gauge) throws DatabaseException{
        String selectSQL = "SELECT * FROM Track WHERE gauge = ?;";
        ArrayList<Track> tracks = new ArrayList<Track>();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, gauge.getName());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                Product newProduct = ProductDAO.findProductByID(productId);
                String newGauge = resultSet.getString("gauge");

                Track track = new Track(newProduct, newGauge);
                tracks.add(track);
            }
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
        return tracks;
    }

     /**
     * Retrieves a list of all tracks from the database.
     *
     * @return An ArrayList of all Track objects in the database | null if can't find.
     * @throws DatabaseException If a database error occurs.
     */
    public static ArrayList<Track> findAllControllers() throws DatabaseException {
        ArrayList<Track> tracks = new ArrayList<Track>();
        String selectSQL = "SELECT * FROM Track;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                Product newProduct = ProductDAO.findProductByID(productId);
                String newGauge = resultSet.getString("gauge");

                Track track = new Track(newProduct, newGauge);
                tracks.add(track);
            }
        } catch (SQLTimeoutException e){
            throw new ConnectionException("Database connect failed",e);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(),e);
        }
        return tracks;
    }
    
}

