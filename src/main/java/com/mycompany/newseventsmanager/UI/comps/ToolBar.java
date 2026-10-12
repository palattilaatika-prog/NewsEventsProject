/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.newseventsmanager.UI.comps;


import javax.swing.*;
import java.awt.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionListener;

/**
 *
 * @author Denes
 */
public class ToolBar extends JToolBar{
    private final JTextField searchField;
    private final JButton refreshButton;
    private final JLabel statusLabel;

    public ToolBar(Runnable onSearchChanged, ActionListener onRefreshClicked) {
        setFloatable(false);
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

        add(new JLabel("Search:"));
        searchField = new JTextField(25);
        add(searchField);

        // Real-time search filter trigger on typing
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { onSearchChanged.run(); }
            @Override public void removeUpdate(DocumentEvent e) { onSearchChanged.run(); }
            @Override public void changedUpdate(DocumentEvent e) { onSearchChanged.run(); }
        });

        refreshButton = new JButton("Refresh RSS");
        refreshButton.addActionListener(onRefreshClicked);
        add(refreshButton);

        statusLabel = new JLabel("");
        statusLabel.setForeground(Color.GRAY);
        add(statusLabel);
    }

    public String getSearchQuery() {
        return searchField.getText().trim();
    }

    public void setLoadingState(boolean isLoading) {
        refreshButton.setEnabled(!isLoading);
        searchField.setEnabled(!isLoading);
        statusLabel.setText(isLoading ? " Loading feeds..." : "");
    }
}
