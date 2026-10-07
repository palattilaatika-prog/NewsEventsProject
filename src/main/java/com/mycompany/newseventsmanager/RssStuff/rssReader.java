/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.newseventsmanager.RssStuff;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mycompany.newseventsmanager.newsevents.news;
import com.mycompany.newseventsmanager.Connection.Http;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
/**
 *
 * @author Denes
 */
public class rssReader {
    // Load sources from JSON
    public static List<rssSourceJS> loadSourcesFromJSON(String filePath) {
        List<rssSourceJS> sources = new ArrayList<>();
        try (FileReader reader = new FileReader(filePath)) {
            JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);
            JsonArray jsonArray = jsonObject.getAsJsonArray("rssSources");
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject obj = jsonArray.get(i).getAsJsonObject();
                sources.add(new rssSourceJS(
                        obj.get("name").getAsString(),
                        obj.get("url").getAsString(),
                        obj.get("language").getAsString()
                ));
            }
        } catch (Exception e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
        }
        return sources;
    }

    // Fetch and parse news from RSS
    public static List<news> fetchNewsFromRSS(rssSourceJS source) {
        List<news> newsList = new ArrayList<>();
        int statusCode = Http.getStatusCode(source.getURL());
        
        if (statusCode != 200) {
            System.err.println("Failed to fetch RSS from " + source.getName() + ", status code: " + statusCode);
            return newsList;
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new URL(source.getURL()).openStream());
            doc.getDocumentElement().normalize();

            NodeList items = doc.getElementsByTagName("item");

            for (int i = 0; i < items.getLength(); i++) {
                Element item = (Element) items.item(i);

                String title = getTagValue("title", item);
                String description = getTagValue("description", item);
                String link = getTagValue("link", item);
                String pubDateStr = getTagValue("pubDate", item);
                String author = getTagValue("author", item);
                if (author.isEmpty()) author = getTagValue("dc:creator", item);

                Timestamp publishDate = parsePubDate(pubDateStr);
                Timestamp now = new Timestamp(System.currentTimeMillis());

                // Default cover image check
                String coverImage = "res/images/default.jpg";
                if (!new File(coverImage).exists()) {
                    coverImage = "";
                }

                news news = new news(
                        title,
                        description,
                        source.getName(),
                        author.isEmpty() ? "Unknown" : author,
                        publishDate,
                        now,
                        now,
                        link,
                        source.getName(),
                        source.getlanguage(),
                        coverImage
                );
                newsList.add(news);
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            System.err.println("Error parsing XML from " + source.getName() + ": " + e.getMessage());
        }

        return newsList;
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList != null && nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }

    private static Timestamp parsePubDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return new Timestamp(System.currentTimeMillis());
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
            Date parsedDate = dateFormat.parse(dateStr);
            return new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            return new Timestamp(System.currentTimeMillis());
        }
    }
}
