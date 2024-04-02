package com.example.traintracker;

// DatabaseConnector.java
import com.google.android.gms.maps.model.LatLng;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// DatabaseConnector.java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String SERVER_ADDRESS = "DESKTOP-1PFUCEE\\STUTISERVER";
    private static final int PORT_NUMBER = 59480;
    private static final String DATABASE_NAME = "TrainTrackingDB";

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:sqlserver://" + SERVER_ADDRESS + ":" + PORT_NUMBER + ";DatabaseName=" + DATABASE_NAME;

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("SQL Server JDBC Driver not found.");
        }
    }

    public static LatLng getTrainLocation(String trainNumber, String date) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        LatLng trainLocation = null;

        try {
            // Get database connection
            connection = getConnection();

            // Prepare SQL query to fetch train location
            String query = "SELECT latitude, longitude FROM TrainLocations WHERE train_number = ? AND departure_date = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, trainNumber);
            statement.setString(2, date);

            // Execute query
            resultSet = statement.executeQuery();

            // If result set contains data, extract train location
            if (resultSet.next()) {
                double latitude = resultSet.getDouble("latitude");
                double longitude = resultSet.getDouble("longitude");
                trainLocation = new LatLng(latitude, longitude);
            }
        } finally {
            // Close resources
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return trainLocation;
    }
}

