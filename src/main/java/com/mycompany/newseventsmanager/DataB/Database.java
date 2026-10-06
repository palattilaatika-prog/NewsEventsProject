/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.newseventsmanager.DataB;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Denes
 */
public class Database {
    private static final String DB_URL = "jdbc:sqlite:news_events.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
}
    public static void initialize() {
        String createNewsTable = "CREATE TABLE IF NOT EXISTS news (" +
                "title TEXT NOT NULL, " +
                "description TEXT, " +
                "source TEXT NOT NULL, " +
                "author TEXT, " +
                "publisher TEXT, " +
                "publish_date TIMESTAMP, " +
                "url TEXT NOT NULL, " +
                "creation_date TIMESTAMP NOT NULL, " +
                "last_modified_date TIMESTAMP NOT NULL, " +
                "language TEXT NOT NULL, " +
                "cover_image TEXT, " +
                "PRIMARY KEY (source, url)" +
                ");";

        String createEventsTable = "CREATE TABLE IF NOT EXISTS events (" +
                "title TEXT NOT NULL, " +
                "description TEXT, " +
                "source TEXT NOT NULL, " +
                "author TEXT, " +
                "publisher TEXT, " +
                "publish_date TIMESTAMP, " +
                "url TEXT, " +
                "creation_date TIMESTAMP NOT NULL, " +
                "last_modified_date TIMESTAMP NOT NULL, " +
                "language TEXT NOT NULL, " +
                "cover_image TEXT, " +
                "event_datetime TIMESTAMP NOT NULL, " +
                "location TEXT NOT NULL, " +
                "PRIMARY KEY (title, event_datetime, location)" +
                ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) 
        {
            stmt.execute(createNewsTable);
            stmt.execute(createEventsTable);
        } catch (SQLException e) 
        {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }
}
