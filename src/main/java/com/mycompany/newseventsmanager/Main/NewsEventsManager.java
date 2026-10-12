/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.newseventsmanager.Main;


import com.mycompany.newseventsmanager.DataB.Database;
import com.mycompany.newseventsmanager.UI.MainWindow;

import javax.swing.*;
/**
 *
 * @author Denes
 */
public class NewsEventsManager {

    public static void main(String[] args) {
      // 1. Initialize SQLite database & tables if they don't exist yet
        try {
            Database.initialize();
        } catch (Exception e) {
            System.err.println("Failed to initialize SQLite database: " + e.getMessage());
            e.printStackTrace();
        }

        // 2. Schedule GUI creation on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
        });
    }
}

