/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.newseventsmanager.UI.comps;
import com.mycompany.newseventsmanager.newsevents.news;
import com.mycompany.newseventsmanager.newsevents.events;

import javax.swing.*;
import java.awt.*;
/**
 *
 * @author Denes
 */
public class DetailPanel extends JPanel {
    private static final String DEFAULT_IMAGE = "res/images/default.jpg";

    private final JLabel imageLabel;
    private final JLabel titleLabel;
    private final JTextArea descriptionArea;
    private final JLabel metadataLabel;
    private final JLabel extraLabel;

    public DetailPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header / Image area
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(220, 140));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        headerPanel.add(imageLabel, BorderLayout.WEST);

        // Title and Metadata
        JPanel titlePanel = new JPanel(new GridLayout(3, 1, 5, 5));
        titleLabel = new JLabel("Select an item to view details");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        metadataLabel = new JLabel("");
        metadataLabel.setForeground(Color.DARK_GRAY);

        extraLabel = new JLabel("");
        extraLabel.setForeground(Color.BLUE);

        titlePanel.add(titleLabel);
        titlePanel.add(metadataLabel);
        titlePanel.add(extraLabel);

        headerPanel.add(titlePanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Description Content Body
        descriptionArea = new JTextArea();
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setMargin(new Insets(8, 8, 8, 8));

        add(new JScrollPane(descriptionArea), BorderLayout.CENTER);
    }

    public void displayNews(news n) {
        if (n == null) {
            clear();
            return;
        }
        titleLabel.setText(n.gettitle());
        descriptionArea.setText(n.getdesc());
        metadataLabel.setText(String.format("Source: %s | Author: %s | Publisher: %s",
                n.getsource(), n.getauthor(), n.getpub()));
        extraLabel.setText(String.format("Published: %s | Lang: %s | URL: %s",
                n.getpDate(), n.getlanguage(), n.getURL()));

        loadImage(n.getcoverImage());
    }

    public void displayEvent(events e) {
        if (e == null) {
            clear();
            return;
        }
        titleLabel.setText(e.gettitle());
        descriptionArea.setText(e.getdesc());
        metadataLabel.setText(String.format("Location: %s | Event Time: %s",
                e.getLocation(), e.geteventDate()));
        extraLabel.setText(String.format("Source: %s | Author: %s | Publisher: %s",
                e.getsource(), e.getauthor(), e.getpub()));

        loadImage(e.getcoverImage());
    }

    public void clear() {
        titleLabel.setText("Select an item to view details");
        descriptionArea.setText("");
        metadataLabel.setText("");
        extraLabel.setText("");
        imageLabel.setIcon(null);
    }

    private void loadImage(String imagePath) {
    try {
        ImageIcon icon;

        if (imagePath == null || imagePath.trim().isEmpty()) {
            // No image path was provided
            icon = new ImageIcon(DEFAULT_IMAGE);
        } else if (imagePath.startsWith("http://")
                || imagePath.startsWith("https://")) {
            // Load the image from the internet
            icon = new ImageIcon(new java.net.URL(imagePath));
        } else {
            // Load the image from a local file
            java.io.File file = new java.io.File(imagePath);

            if (file.exists()) {
                icon = new ImageIcon(file.getAbsolutePath());
            } else {
                icon = new ImageIcon(DEFAULT_IMAGE);
            }
        }

        // Check whether the image loaded successfully
        if (icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) {
            imageLabel.setIcon(null);
            imageLabel.setText("No Image Available");
            return;
        }

        // Resize the image to fit the panel
        Image scaled = icon.getImage().getScaledInstance(
                220, 140, Image.SCALE_SMOOTH
        );

        imageLabel.setText("");
        imageLabel.setIcon(new ImageIcon(scaled));

    } catch (Exception e) {
        imageLabel.setIcon(null);
        imageLabel.setText("No Image Available");

        System.err.println("Failed to load image: " + imagePath);
        e.printStackTrace();
    }
}
}
