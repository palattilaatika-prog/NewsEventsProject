/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.newseventsmanager.UI.comps;
import com.mycompany.newseventsmanager.newsevents.news;
import com.mycompany.newseventsmanager.newsevents.events;

import javax.swing.*;
import java.awt.*;
import java.io.File;
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
        String pathToUse = (imagePath != null && !imagePath.trim().isEmpty() && new File(imagePath).exists())
                ? imagePath : DEFAULT_IMAGE;

        if (new File(pathToUse).exists()) {
            ImageIcon icon = new ImageIcon(pathToUse);
            Image scaled = icon.getImage().getScaledInstance(220, 140, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
        } else {
            imageLabel.setIcon(null);
            imageLabel.setText("No Image Available");
        }
    }
}
