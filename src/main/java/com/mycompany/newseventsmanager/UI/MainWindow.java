/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.newseventsmanager.UI;
import com.mycompany.newseventsmanager.DataB.DatabaseManager;
import com.mycompany.newseventsmanager.RssStuff.rssReader;
import com.mycompany.newseventsmanager.UI.comps.DetailPanel;
import com.mycompany.newseventsmanager.UI.comps.MasterPanel;
import com.mycompany.newseventsmanager.UI.comps.ToolBar;
import com.mycompany.newseventsmanager.UI.dialogs.EventsDialog;
import com.mycompany.newseventsmanager.UI.dialogs.NewsDialog;
import com.mycompany.newseventsmanager.newsevents.events;
import com.mycompany.newseventsmanager.newsevents.news;

import javax.swing.*;
import java.awt.*;
import java.util.List;
/**
 *
 * @author Denes
 */
public class MainWindow extends JFrame {
    private final ToolBar toolBar;
    private final MasterPanel masterPanel;
    private final DetailPanel detailPanel;

    private boolean isNewsMode = true; // Mode flag: true = News, false = Events

    public MainWindow() {
        setTitle("News & Events Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        // 1. Top Bar
        toolBar = new ToolBar(this::filterData, e -> performAsyncRefresh());
        add(toolBar, BorderLayout.NORTH);

        // 2. Center Gmail-like Master-Detail View (1/3 to 2/3 ratio)
        masterPanel = new MasterPanel(e -> onItemSelected());
        detailPanel = new DetailPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, masterPanel, detailPanel);
        splitPane.setResizeWeight(0.33); // ~1/3 width for Master list
        add(splitPane, BorderLayout.CENTER);

        // 3. Bottom Action Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JButton toggleModeBtn = new JButton("Switch to Events");
        JButton clearDbBtn = new JButton("Clear DB");

        addBtn.addActionListener(e -> onAdd());
        editBtn.addActionListener(e -> onEdit());
        deleteBtn.addActionListener(e -> onDelete());

        toggleModeBtn.addActionListener(e -> {
            isNewsMode = !isNewsMode;
            toggleModeBtn.setText(isNewsMode ? "Switch to Events" : "Switch to News");
            loadDataFromDb();
        });

        clearDbBtn.addActionListener(e -> onClearDatabase());

        bottomPanel.add(addBtn);
        bottomPanel.add(editBtn);
        bottomPanel.add(deleteBtn);
        bottomPanel.add(toggleModeBtn);
        bottomPanel.add(clearDbBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        // Load persisted SQLite data at startup
        loadDataFromDb();
    }

    private void loadDataFromDb() {
        masterPanel.clearSelection();
        detailPanel.clear();
        filterData();
    }

    private void filterData() {
        String query = toolBar.getSearchQuery();
        if (isNewsMode) {
            List<news> newsList = DatabaseManager.searchNews(query);
            masterPanel.updateNewsList(newsList);
        } else {
            List<events> eventsList = DatabaseManager.searchEevents(query);
            masterPanel.updateEventsList(eventsList);
        }
    }

    private void onItemSelected() {
    Object selected = masterPanel.getSelectedItem();
    masterPanel.markAsRead(selected);          
    if (selected instanceof events ev) {    
        detailPanel.displayEvent(ev);
    } else if (selected instanceof news n) {
        detailPanel.displayNews(n);
    } else {
        detailPanel.clear();
    }
}

    private void performAsyncRefresh() {
        toolBar.setLoadingState(true);

        // Background task via SwingWorker to avoid freezing UI EDT
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                // Fetch & parse RSS feeds, saving new records into SQLite
                rssReader.fetchAndSaveAllFeeds();
                return null;
            }

            @Override
            protected void done() {
                toolBar.setLoadingState(false);
                loadDataFromDb();
            }
        }.execute();
    }

    private void onAdd() {
        if (isNewsMode) {
            NewsDialog dialog = new NewsDialog(this, null);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                DatabaseManager.insertNews(dialog.getNews());
                loadDataFromDb();
            }
        } else {
            EventsDialog dialog = new EventsDialog(this, null);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                DatabaseManager.insertEvent(dialog.getEvent());
                loadDataFromDb();
            }
        }
    }

    private void onEdit() {
    Object selected = masterPanel.getSelectedItem();
    if (selected == null) {
        JOptionPane.showMessageDialog(this, "Please select an item to edit.", "Selection Required", JOptionPane.WARNING_MESSAGE);
        return;
    }

    if (selected instanceof news n) {
        NewsDialog dialog = new NewsDialog(this, n);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            DatabaseManager.updateNews(dialog.getNews());
            loadDataFromDb();
        }
    } else if (selected instanceof events ev) {
        // Store old values required for the WHERE clause in updateEvent
        String oldTitle = ev.gettitle();
        java.sql.Timestamp oldDate = ev.geteventDate();
        String oldLocation = ev.getLocation();

        EventsDialog dialog = new EventsDialog(this, ev);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            DatabaseManager.updateEvent(dialog.getEvent(), oldTitle, oldDate, oldLocation);
            loadDataFromDb();
        }
    }
}

    private void onDelete() {
    Object selected = masterPanel.getSelectedItem();
    if (selected == null) {
        JOptionPane.showMessageDialog(this, "Please select an item to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this item?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        if (selected instanceof news n) {
            // Fixed getters: getsource() and getURL()
            DatabaseManager.deleteNews(n.getsource(), n.getURL());
        } else if (selected instanceof events ev) {
            // Fixed getters: gettitle() and geteventDate()
            DatabaseManager.deleteEvent(ev.gettitle(), ev.geteventDate(), ev.getLocation());
        }
        loadDataFromDb();
    }
}

    private void onClearDatabase() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to clear the entire database?",
                "Clear Database",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            DatabaseManager.clearDatabase();
            loadDataFromDb();
        }
    }
}
