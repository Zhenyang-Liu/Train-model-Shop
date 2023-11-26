package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Brand;

public class BrandDAO {
    /**
     * Finds brand information based on the brand ID.
     *
     * @return An ArrayList<String> contains all brands in the database.
     * @throws SQLException If a SQLException occurs while executing the database operation.
     */
    public static ArrayList<String> findAllBrand() {
        String selectSQL = "SELECT DISTINCT brand_name FROM Product;";
        ArrayList<String> brandList = new ArrayList<>();

        try (Connection connection = DatabaseConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    brandList.add(resultSet.getString("brand_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return brandList;
    }
}

