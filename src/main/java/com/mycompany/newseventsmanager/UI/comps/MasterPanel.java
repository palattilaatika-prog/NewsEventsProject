/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.newseventsmanager.UI.comps;
import com.mycompany.newseventsmanager.newsevents.news;
import com.mycompany.newseventsmanager.newsevents.events;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
/**
 *
 * @author Denes
 */
public class MasterPanel extends JPanel{
    private final DefaultListModel<Object> listModel;
    private final JList<Object> itemList;
    private final Set<String> readKeys = new HashSet<>();

    public MasterPanel(ListSelectionListener selectionListener) {
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        itemList = new JList<>(listModel);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemList.addListSelectionListener(selectionListener);

        // Custom Cell Renderer to display Title & Sub-info
        itemList.setCellRenderer(new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

            boolean isRead = readKeys.contains(keyOf(value));

            label.setFont(label.getFont().deriveFont(isRead ? Font.PLAIN : Font.BOLD));
            if (!isSelected) {
                label.setForeground(isRead ? Color.GRAY : Color.BLACK);
            }

            if (value instanceof events e) {
                label.setText("<html>[Event] " + e.gettitle() + "<br/><small>" + e.getLocation() + " • " + e.geteventDate() + "</small></html>");
            } else if (value instanceof news n) {
                label.setText("<html>[News] " + n.gettitle() + "<br/><small>" + n.getsource() + "</small></html>");
            }
            return label;
        }
    });

    add(new JScrollPane(itemList), BorderLayout.CENTER);
}
    private String keyOf(Object o) {
        if (o instanceof events e) {
            return "E|" + e.gettitle() + "|" + e.geteventDate() + "|" + e.getLocation();
        } else if (o instanceof news n) {
            return "N|" + n.getsource() + "|" + n.getURL();
        }
        return null;
    }
    public void markAsRead(Object item) {
        String key = keyOf(item);
        if (key != null && readKeys.add(key)) {   // add() returns true if it was new
            itemList.repaint();                   // redraw the rows
        }
    }

    public void updateNewsList(List<news> newsList) {
        listModel.clear();
        if (newsList != null) {
            newsList.forEach(listModel::addElement);
        }
    }

    public void updateEventsList(List<events> eventsList) {
        listModel.clear();
        if (eventsList != null) {
            eventsList.forEach(listModel::addElement);
        }
    }

    public Object getSelectedItem() {
        return itemList.getSelectedValue();
    }

    public void clearSelection() {
        itemList.clearSelection();
    }
}
