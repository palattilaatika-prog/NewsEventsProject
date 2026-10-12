/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.newseventsmanager.UI.dialogs;
import com.mycompany.newseventsmanager.newsevents.events;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
/**
 *
 * @author Denes
 */
public class EventsDialog extends JDialog {
    private final JTextField titleField = new JTextField(20);
    private final JTextArea descArea = new JTextArea(4, 20);
    private final JTextField locationField = new JTextField(20);
    private final JTextField dateField = new JTextField("2026-10-20 18:30:00", 20);
    private final JTextField sourceField = new JTextField(20);
    private final JTextField coverField = new JTextField(20);

    private events eventItem;
    private boolean saved = false;

    public EventsDialog(Frame owner, events existing) {
        super(owner, existing == null ? "Add Event" : "Edit Event", true);
        this.eventItem = existing != null ? existing : new events("", "", "", "", null, null, null, "", "","","",null,"");

        setLayout(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(6, 2, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        form.add(new JLabel("Title:")); form.add(titleField);
        form.add(new JLabel("Description:")); form.add(new JScrollPane(descArea));
        form.add(new JLabel("Location:")); form.add(locationField);
        form.add(new JLabel("Event DateTime (yyyy-MM-dd HH:mm:ss):")); form.add(dateField);
        form.add(new JLabel("Source:")); form.add(sourceField);
        form.add(new JLabel("Cover Image Path:")); form.add(coverField);

        if (existing != null) {
            titleField.setText(existing.gettitle());
            descArea.setText(existing.getdesc());
            locationField.setText(existing.getLocation());
            if (existing.geteventDate() != null) {
                dateField.setText(existing.geteventDate().toString());
            }
            sourceField.setText(existing.getsource());
            coverField.setText(existing.getcoverImage());
        }

        add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> onSave());
        cancelBtn.addActionListener(e -> dispose());

        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        add(btnPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }

    private void onSave() {
        try {
            Timestamp eventTime = Timestamp.valueOf(dateField.getText().trim());
            eventItem.settitle(titleField.getText().trim());
            eventItem.setdesc(descArea.getText().trim());
            eventItem.setLocation(locationField.getText().trim());
            eventItem.seteventDate(eventTime);
            eventItem.setsource(sourceField.getText().trim());
            eventItem.setcoverImage(coverField.getText().trim());

            Timestamp now = new Timestamp(System.currentTimeMillis());
            if (eventItem.getcreationDate() == null) eventItem.setcreationDate(now);
            eventItem.setlastModifiedDate(now);

            saved = true;
            dispose();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Invalid Date format! Use: yyyy-MM-dd HH:mm:ss", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() { return saved; }
    public events getEvent() { return eventItem; }
}
