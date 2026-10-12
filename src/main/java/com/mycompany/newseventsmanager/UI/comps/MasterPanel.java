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
/**
 *
 * @author Denes
 */
public class MasterPanel extends JPanel{
    private final DefaultListModel<Object> listModel;
    private final JList<Object> itemList;

    public MasterPanel(ListSelectionListener selectionListener) {
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        itemList = new JList<>(listModel);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemList.addListSelectionListener(selectionListener);

        // Custom Cell Renderer to display Title & Sub-info
        itemList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
                if (value instanceof news n) {
                    label.setText("<html><b>[News]</b> " + n.gettitle() + "<br/><small style='color:gray;'>" + n.getsource() + "</small></html>");
                } else if (value instanceof events e) {
                    label.setText("<html><b>[Event]</b> " + e.gettitle() + "<br/><small style='color:gray;'>" + e.getLocation() + " • " + e.geteventDate() + "</small></html>");
                }
                return label;
            }
        });

        add(new JScrollPane(itemList), BorderLayout.CENTER);
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
