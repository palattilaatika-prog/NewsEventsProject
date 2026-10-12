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
import com.mycompany.newseventsmanager.DataB.DatabaseManager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileReader;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * @author Denes
 */
public class rssReader {
   public static void fetchAndSaveAllFeeds() {
        List<rssSourceJS> sources = loadSourcesFromJSON("rssSources.json");
        for (rssSourceJS source : sources) {
            List<news> newsList = fetchNewsFromRSS(source);
            for (news item : newsList) {
                try {
                    DatabaseManager.insertNews(item);
                } catch (Exception e) {
                    System.err.println("Skipping item: " + e.getMessage());
                }
            }
        }
    }

    public static List<rssSourceJS> loadSourcesFromJSON(String filePath) {
        List<rssSourceJS> sources = new ArrayList<>();
        try (FileReader reader = new FileReader(filePath)) {
            JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);
            if (jsonObject != null && jsonObject.has("rssSources")) {
                JsonArray jsonArray = jsonObject.getAsJsonArray("rssSources");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject obj = jsonArray.get(i).getAsJsonObject();
                    String name = obj.has("name") ? obj.get("name").getAsString() : "Unknown";
                    String url = obj.has("url") ? obj.get("url").getAsString() : "";
                    String language = obj.has("language") ? obj.get("language").getAsString() : "RO";

                    if (!url.isEmpty()) {
                        sources.add(new rssSourceJS(name, url, language));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
        }
        return sources;
    }
    
    public static List<news> fetchNewsFromRSS(rssSourceJS source) {
        List<news> newsList = new ArrayList<>();
        int statusCode = Http.getStatusCode(source.getURL());

        if (statusCode != 200) {
            System.err.println("Failed to fetch RSS from " + source.getName() + ", status code: " + statusCode);
            return newsList;
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new URL(source.getURL()).openStream());
            doc.getDocumentElement().normalize();

            NodeList items = doc.getElementsByTagName("item");

            for (int i = 0; i < items.getLength(); i++) {
                Element item = (Element) items.item(i);

                String title = getTagValueByLocalName("title", item);
                String rawDescription = getTagValueByLocalName("description", item);
                String link = getTagValueByLocalName("link", item);
                String pubDateStr = getTagValueByLocalName("pubDate", item);
                String author = getTagValueByLocalName("author", item);
                if (author.isEmpty()) author = getTagValueByLocalName("creator", item);

                // Extract image URL
                String coverImage = extractImageUrl(item, rawDescription);
                
                // Debug log to NetBeans console to verify extracted image URLs
                System.out.println("[" + source.getName() + "] Extracted Image: " + coverImage);

                // Clean description HTML
                String cleanDescription = cleanHtml(rawDescription);

                Timestamp publishDate = parsePubDate(pubDateStr);
                Timestamp now = new Timestamp(System.currentTimeMillis());

                news news = new news(
                        title,
                        cleanDescription,
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
        } catch (Exception e) {
            System.err.println("Error parsing XML from " + source.getName() + ": " + e.getMessage());
        }

        return newsList;
    }
    // Helper: Find image URL in <enclosure>, <media:content>, or <img> inside description
   private static String extractImageUrl(Element item, String description) {
        // 1. Check <enclosure url="...">
        NodeList enclosureList = item.getElementsByTagName("enclosure");
        if (enclosureList.getLength() > 0) {
            Element enclosure = (Element) enclosureList.item(0);
            String url = enclosure.getAttribute("url");
            if (url != null && !url.trim().isEmpty()) return url.trim();
        }

        // 2. Check <media:content> or <media:thumbnail>
        NodeList mediaList = item.getElementsByTagNameNS("*", "content");
        if (mediaList.getLength() == 0) {
            mediaList = item.getElementsByTagNameNS("*", "thumbnail");
        }
        if (mediaList.getLength() > 0) {
            Element media = (Element) mediaList.item(0);
            String url = media.getAttribute("url");
            if (url != null && !url.trim().isEmpty()) return url.trim();
        }

        // 3. Get content:encoded (WordPress full HTML content)
        String contentEncoded = getTagValueByLocalName("encoded", item);

        // Combine HTML sources and unescape brackets
        String fullHtml = (description + " " + contentEncoded)
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replace("&#039;", "'");

        // 4. Regex matching src=, data-src=, or srcset=
        Pattern pattern = Pattern.compile("(?:src|data-src|srcset)=[\"'](https?://[^\"'\\s>]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fullHtml);
        if (matcher.find()) {
            String imgUrl = matcher.group(1);
            if (imgUrl.contains(" ")) {
                imgUrl = imgUrl.split("\\s+")[0];
            }
            if (imgUrl.matches("(?i).*\\.(jpg|jpeg|png|webp|jfif|ashx).*") || imgUrl.contains("/uploads/")) {
                return imgUrl;
            }
        }

        return "res/NewsEventsCoverImage.jfif";
    }
   
    // Helper: Strip HTML tags and decode common HTML entities
    private static String cleanHtml(String html) {
        if (html == null) return "";
        String clean = html.replaceAll("<[^>]*>", "");
        clean = clean.replace("&nbsp;", " ")
                     .replace("&#8230;", "...")
                     .replace("&hellip;", "...")
                     .replace("&amp;", "&")
                     .replace("&quot;", "\"")
                     .replace("&#039;", "'")
                     .replace("&lt;", "<")
                     .replace("&gt;", ">");
        return clean.trim();
    }
    
    private static String getTagValueByLocalName(String localName, Element element) {
        NodeList nodeList = element.getElementsByTagNameNS("*", localName);
        if (nodeList == null || nodeList.getLength() == 0) {
            nodeList = element.getElementsByTagName(localName);
        }
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


