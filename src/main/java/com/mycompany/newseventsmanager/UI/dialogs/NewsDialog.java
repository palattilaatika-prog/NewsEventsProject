/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.newseventsmanager.UI.dialogs;
import com.mycompany.newseventsmanager.newsevents.news;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
/**
 *
 * @author Denes
 */
public class NewsDialog extends JDialog{
    private final JTextField titleField = new JTextField(20);
    private final JTextArea descArea = new JTextArea(4, 20);
    private final JTextField sourceField = new JTextField(20);
    private final JTextField authorField = new JTextField(20);
    private final JTextField publisherField = new JTextField(20);
    private final JTextField urlField = new JTextField(20);
    private final JTextField langField = new JTextField(20);
    private final JTextField coverField = new JTextField(20);

    private news newsItem;
    private boolean saved = false;

    public NewsDialog(Frame owner, news existing) {
        super(owner, existing == null ? "Add News" : "Edit News", true);
        this.newsItem = existing != null ? existing : new news("", "", "", "", null, null, null, "", "","","");

        setLayout(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(8, 2, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        form.add(new JLabel("Title:")); form.add(titleField);
        form.add(new JLabel("Description:")); form.add(new JScrollPane(descArea));
        form.add(new JLabel("Source:")); form.add(sourceField);
        form.add(new JLabel("Author:")); form.add(authorField);
        form.add(new JLabel("Publisher:")); form.add(publisherField);
        form.add(new JLabel("URL:")); form.add(urlField);
        form.add(new JLabel("Language (HU/RO):")); form.add(langField);
        form.add(new JLabel("Cover Image Path:")); form.add(coverField);

        if (existing != null) {
            titleField.setText(existing.gettitle());
            descArea.setText(existing.getdesc());
            sourceField.setText(existing.getsource());
            authorField.setText(existing.getauthor());
            publisherField.setText(existing.getpub());
            urlField.setText(existing.getURL());
            langField.setText(existing.getlanguage());
            coverField.setText(existing.getcoverImage());
            // PK fields are read-only during edit
            sourceField.setEditable(false);
            urlField.setEditable(false);
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
        if (titleField.getText().trim().isEmpty() || urlField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title and URL are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        newsItem.settitle(titleField.getText().trim());
        newsItem.setdesc(descArea.getText().trim());
        newsItem.setsource(sourceField.getText().trim());
        newsItem.setauthor(authorField.getText().trim());
        newsItem.setpub(publisherField.getText().trim());
        newsItem.setURL(urlField.getText().trim());
        newsItem.setlanguage(langField.getText().trim());
        newsItem.setcoverImage(coverField.getText().trim());

        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (newsItem.getcreationDate() == null) {
            newsItem.setcreationDate(now);
            newsItem.setpDate(now);
        }
        newsItem.setlastModifiedDate(now);

        saved = true;
        dispose();
    }

    public boolean isSaved() { return saved; }
    public news getNews() { return newsItem; }
}
