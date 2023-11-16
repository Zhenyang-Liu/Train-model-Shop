package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Gauge;
import model.Track;
import model.Track.TrackType;
import model.Product;

public class TrackDAO extends ProductDAO {

    /**
     * Inserts a new track record into the database.
     *
     * @param track The Track object to be inserted.
     * @throws SQLException If a database error occurs.
     */
    public static void insertTrack(Track track) throws SQLException {
        int productID = insertProduct(track);
        String insertSQL = "INSERT INTO Track (product_id, track_type, gauge) VALUES (?, ?, ?);";
        
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            
            preparedStatement.setInt(1, productID);
            preparedStatement.setString(2, track.getTrackType());
            preparedStatement.setString(3, track.getGauge());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // EraDAO.insertEra(productID, rollingStock.getEra());
            } else {
                throw new SQLException("Creating track failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error SQL query: " + insertSQL);
            throw e;
        }

    }

    /**
     * Updates an existing track record in the database.
     *
     * @param track The Track object with updated information.
     * @throws SQLException If a database error occurs.
     */
    public static void updateTrack(Track track) throws SQLException{
        ProductDAO.updateProduct(track);
        String updateSQL = "UPDATE Track SET track_type = ?, gauge = ? WHERE product_id = ?;";
        
        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL, Statement.RETURN_GENERATED_KEYS)) {
            
            preparedStatement.setString(1, track.getTrackType());
            preparedStatement.setString(2, track.getGauge());
            preparedStatement.setInt(3, track.getProductID());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // EraDAO.deleteEra(rollingStock.getProductID());
                // EraDAO.insertEra(rollingStock.getProductID(), rollingStock.getEra());
            } else {
                throw new SQLException("Update Track failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Deletes a track record from the database by product ID.
     *
     * @param productId The ID of the track product to be deleted.
     * @throws SQLException If a database error occurs.
     */
    public static void deleteTrack(int productId) throws SQLException{
        String deleteSQL = "DELETE FROM Track WHERE product_id = ?;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, productId);

            int rowsAffected = preparedStatement.executeUpdate();
            
            if (rowsAffected > 0) {
                ProductDAO.deleteProduct(productId);
                System.out.println("Track with ID " + productId + " was deleted successfully.");
            } else {
                System.out.println("No Track was found with ID " + productId + " to delete.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }  
    }

    /**
     * Finds a Track record in the database by product ID.
     *
     * @param productID The ID of the Track product to be retrieved.
     * @return A Track object representing the retrieved Track record | null if can't find.
     * @throws SQLException If a database error occurs.
     */
    public static Track findTrackByID(int productID) throws SQLException {
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
                String newType = resultSet.getString("track_type");

                track = new Track(newProduct, newType, newGauge);
            }
            return track;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Retrieves a list of tracks from the database that match the specified gauge.
     *
     * @param gauge The gauge to filter tracks by.
     * @return An ArrayList of track objects that match the specified gauge | null if can't find.
     * @throws SQLException If a database error occurs.
     */
    public static ArrayList<Track> findTracksByGauge(Gauge gauge) throws SQLException{
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
                String newType = resultSet.getString("track_type");

                Track track = new Track(newProduct, newType, newGauge);
                tracks.add(track);
            }
            return tracks;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Retrieves a list of tracks from the database that match the specified type.
     *
     * @param type The track type to filter rollingstocks by.
     * @return An ArrayList of Track objects that match the specified type | null if can't find.
     * @throws SQLException If a database error occurs.
     */
    public static ArrayList<Track> findTracksByType(TrackType type) throws SQLException{
        String selectSQL = "SELECT * FROM Track WHERE track_type = ?;";
        ArrayList<Track> tracks = new ArrayList<Track>();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, type.getType());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                Product newProduct = ProductDAO.findProductByID(productId);
                String newGauge = resultSet.getString("gauge");
                String newType = resultSet.getString("track_type");

                Track track = new Track(newProduct, newType, newGauge);
                tracks.add(track);
            }
            return tracks;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

     /**
     * Retrieves a list of all tracks from the database.
     *
     * @return An ArrayList of all Track objects in the database | null if can't find.
     * @throws SQLException If a database error occurs.
     */
    public static ArrayList<Track> findAllControllers() throws SQLException {
        ArrayList<Track> tracks = new ArrayList<Track>();
        String selectSQL = "SELECT * FROM Track;";

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                Product newProduct = ProductDAO.findProductByID(productId);
                String newGauge = resultSet.getString("gauge");
                String newType = resultSet.getString("track_type");

                Track track = new Track(newProduct, newType, newGauge);
                tracks.add(track);
            }
            return tracks;
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }
    
}

