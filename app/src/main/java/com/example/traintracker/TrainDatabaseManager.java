package com.example.traintracker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TrainDatabaseManager {

    // Insert train details into the database
    public static void insertTrainDetails(String trainNumber, String currentLocation, String departureDate) throws SQLException {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "INSERT INTO Train (TrainNumber, CurrentLocation, DepartureDate) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, trainNumber);
                statement.setString(2, currentLocation);
                statement.setString(3, departureDate);
                statement.executeUpdate();
            }
        }
    }

    // Retrieve train details from the database
    public static ResultSet getTrainDetails(String trainNumber, String departureDate) throws SQLException {
        ResultSet resultSet = null;
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "SELECT * FROM Train WHERE TrainNumber = ? AND DepartureDate = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, trainNumber);
                statement.setString(2, departureDate);
                resultSet = statement.executeQuery();
            }
        }
        return resultSet;
    }
}
