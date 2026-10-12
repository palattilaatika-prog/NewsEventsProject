/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.newseventsmanager.validation;
import com.mycompany.newseventsmanager.newsevents.events;
import com.mycompany.newseventsmanager.newsevents.news;
import java.net.URL;
/**
 *
 * @author Denes
 */
public class ValidatorLogic {
    public static validation validateNews(news news) {
        validation result = new validation();

        if (news == null) {
            result.addError("News record cannot be null.");
            return result;
        }
        if (isEmpty(news.gettitle())) {
            result.addError("Title is mandatory.");
        }
        if (isEmpty(news.getsource())) {
            result.addError("Source is mandatory (part of primary key).");
        }
        if (isEmpty(news.getURL())) {
            result.addError("URL is mandatory (part of primary key).");
        } else if (!isValidUrl(news.getURL())) {
            result.addError("URL format is invalid.");
        }
        if (isEmpty(news.getlanguage())) {
            result.addError("Language is mandatory.");
        } else if (!news.getlanguage().equalsIgnoreCase("HU") && !news.getlanguage().equalsIgnoreCase("RO")) {
            result.addError("Language must be either 'HU' or 'RO'.");
        }

        return result;
    }
    public static validation validateEvent(events event) {
        validation result = validateNews(event);

        if (event.geteventDate() == null) {
            result.addError("Event Date/Time is mandatory.");
        }
        if (isEmpty(event.getLocation())) {
            result.addError("Location is mandatory.");
        }

        return result;
    }

    private static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private static boolean isValidUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
