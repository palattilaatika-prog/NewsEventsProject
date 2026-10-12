/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.newseventsmanager.DataB;

import com.mycompany.newseventsmanager.newsevents.events;
import com.mycompany.newseventsmanager.newsevents.news;
import com.mycompany.newseventsmanager.validation.validation;
import com.mycompany.newseventsmanager.validation.ValidatorLogic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Denes
 */
public class DatabaseManager {
    // --- NEWS CRUD ---

    public static boolean insertNews(news news) {
        validation val = ValidatorLogic.validateNews(news);
        if (!val.isValid()) {
            throw new IllegalArgumentException("Validation failed at insert:\n• " + val.getFormattedErrors());
        }
        String sql = "INSERT OR IGNORE INTO news(title, description, source, author, publisher, publish_date, url, creation_date, last_modified_date, language, cover_image) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = Database.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, news.gettitle());
            pstmt.setString(2, news.getdesc());
            pstmt.setString(3, news.getsource());
            pstmt.setString(4, news.getauthor());
            pstmt.setString(5, news.getpub());
            pstmt.setTimestamp(6, news.getpDate());
            pstmt.setString(7, news.getURL());
            pstmt.setTimestamp(8, news.getcreationDate());
            pstmt.setTimestamp(9, news.getlastModifiedDate());
            pstmt.setString(10, news.getlanguage());
            pstmt.setString(11, news.getcoverImage());
            return pstmt.executeUpdate()>0;
        } catch (SQLException e) {
            System.err.println("Error inserting news: " + e.getMessage());
            return false;
        }
    }

    public static List<news> getAllNews() {
        List<news> list = new ArrayList<>();
        String sql = "SELECT * FROM news ORDER BY publish_date DESC";
        try (Connection conn = Database.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) 
        {
            while (rs.next()) {
                news news = new news(
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("source"),
                        rs.getString("author"),
                        rs.getTimestamp("publish_date"),
                        rs.getTimestamp("creation_date"),
                        rs.getTimestamp("last_modified_date"),
                        rs.getString("url"),
                        rs.getString("publisher"),
                        rs.getString("language"),
                        rs.getString("cover_image")
                );
                list.add(news);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching news: " + e.getMessage());
        }
        return list;
    }

    public static List<news> searchNews(String keyword) {
        List<news> list = new ArrayList<>();
        String sql = "SELECT * FROM news WHERE title LIKE ? OR description LIKE ? ORDER BY publish_date DESC";
        try (Connection conn = Database.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                news news = new news(
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("source"),
                        rs.getString("author"),
                        rs.getTimestamp("publish_date"),
                        rs.getTimestamp("creation_date"),
                        rs.getTimestamp("last_modified_date"),
                        rs.getString("url"),
                        rs.getString("publisher"),
                        rs.getString("language"),
                        rs.getString("cover_image")
                );
                list.add(news);
            }
        } catch (SQLException e) {
            System.err.println("Error searching news: " + e.getMessage());
        }
        return list;
    }

    public static void updateNews(news news) {
        validation val = ValidatorLogic.validateNews(news);
        if (!val.isValid()) {
            throw new IllegalArgumentException("Validation failed at update:\n• " + val.getFormattedErrors());
        }
        String sql = "UPDATE news SET title = ?, description = ?, author = ?, publisher = ?, publish_date = ?, last_modified_date = ?, language = ?, cover_image = ? WHERE source = ? AND url = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, news.gettitle());
            pstmt.setString(2, news.getdesc());
            pstmt.setString(3, news.getauthor());
            pstmt.setString(4, news.getpub());
            pstmt.setTimestamp(5, news.getpDate());
            pstmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            pstmt.setString(7, news.getlanguage());
            pstmt.setString(8, news.getcoverImage());
            pstmt.setString(9, news.getsource());
            pstmt.setString(10, news.getURL());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating news: " + e.getMessage());
        }
    }

    public static void deleteNews(String source, String url) {
        String sql = "DELETE FROM news WHERE source = ? AND url = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, source);
            pstmt.setString(2, url);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting news: " + e.getMessage());
        }
    }

    // --- EVENT CRUD ---

    public static boolean insertEvent(events event) {
        validation val = ValidatorLogic.validateEvent(event);
        if (!val.isValid()) {
            throw new IllegalArgumentException("Validation failed at insert:\n• " + val.getFormattedErrors());
        }
        String sql = "INSERT OR IGNORE INTO events(title, description, source, author, publisher, publish_date, url, creation_date, last_modified_date, language, cover_image, event_datetime, location) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = Database.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, event.gettitle());
            pstmt.setString(2, event.getdesc());
            pstmt.setString(3, event.getsource());
            pstmt.setString(4, event.getauthor());
            pstmt.setString(5, event.getpub());
            pstmt.setTimestamp(6, event.getpDate());
            pstmt.setString(7, event.getURL());
            pstmt.setTimestamp(8, event.getcreationDate());
            pstmt.setTimestamp(9, event.getlastModifiedDate());
            pstmt.setString(10, event.getlanguage());
            pstmt.setString(11, event.getcoverImage());
            pstmt.setTimestamp(12, event.geteventDate());
            pstmt.setString(13, event.getLocation());
            return pstmt.executeUpdate()>0;
        } catch (SQLException e) {
            System.err.println("Error inserting event: " + e.getMessage());
            return false;
        }
        
    }

    public static List<events> getAllEvents() {
        List<events> list = new ArrayList<>();
        String sql = "SELECT * FROM events ORDER BY event_datetime ASC";
        try (Connection conn = Database.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                events event = new events(
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("source"),
                        rs.getString("author"),
                        rs.getTimestamp("publish_date"),
                        rs.getTimestamp("creation_date"),
                        rs.getTimestamp("last_modified_date"),
                        rs.getString("url"),
                        rs.getString("publisher"),
                        rs.getString("language"),
                        rs.getString("cover_image"),
                        rs.getTimestamp("event_datetime"),
                        rs.getString("location")
                );
                list.add(event);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching events: " + e.getMessage());
        }
        return list;
    }
    public static List<events> searchEevents(String keyword) {
        List<events> list = new ArrayList<>();
        String sql = "SELECT * FROM events WHERE title LIKE ? OR description LIKE ? ORDER BY publish_date DESC";
        try (Connection conn = Database.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                events events = new events(
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("source"),
                        rs.getString("author"),
                        rs.getTimestamp("publish_date"),
                        rs.getTimestamp("creation_date"),
                        rs.getTimestamp("last_modified_date"),
                        rs.getString("url"),
                        rs.getString("publisher"),
                        rs.getString("language"),
                        rs.getString("cover_image"),
                        rs.getTimestamp("event_datetime"),
                        rs.getString("location")
                );
                list.add(events);
            }
        } catch (SQLException e) {
            System.err.println("Error searching events: " + e.getMessage());
        }
        return list;
    }

    public static void updateEvent(events event, String oldTitle, Timestamp oldDateTime, String oldLocation) {
        validation val = ValidatorLogic.validateEvent(event);
        if (!val.isValid()) {
            throw new IllegalArgumentException("Validation failed:\n• " + val.getFormattedErrors());
        }
        String sql = "UPDATE events SET title = ?, description = ?, source = ?, author = ?, publisher = ?, publish_date = ?, url = ?, last_modified_date = ?, language = ?, cover_image = ?, event_datetime = ?, location = ? WHERE title = ? AND event_datetime = ? AND location = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, event.gettitle());
            pstmt.setString(2, event.getdesc());
            pstmt.setString(3, event.getsource());
            pstmt.setString(4, event.getauthor());
            pstmt.setString(5, event.getpub());
            pstmt.setTimestamp(6, event.getpDate());
            pstmt.setString(7, event.getURL());
            pstmt.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
            pstmt.setString(9, event.getlanguage());
            pstmt.setString(10, event.getcoverImage());
            pstmt.setTimestamp(11, event.geteventDate());
            pstmt.setString(12, event.getLocation());

            pstmt.setString(13, oldTitle);
            pstmt.setTimestamp(14, oldDateTime);
            pstmt.setString(15, oldLocation);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating event: " + e.getMessage());
        }
    }

    public static void deleteEvent(String title, Timestamp dateTime, String location) {
        String sql = "DELETE FROM events WHERE title = ? AND event_datetime = ? AND location = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setTimestamp(2, dateTime);
            pstmt.setString(3, location);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting event: " + e.getMessage());
        }
    }

    // --- CLEAR DATABASE ---

    public static void clearDatabase() {
        try (Connection conn = Database.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM news");
            stmt.executeUpdate("DELETE FROM events");
        } catch (SQLException e) {
            System.err.println("Error clearing database: " + e.getMessage());
        }
    }
}
