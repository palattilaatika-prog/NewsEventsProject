/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.newseventsmanager.Connection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 *
 * @author Denes
 */
public class Http {
    public static int getStatusCode(String urlAddress) {
        try {
            URL url = new URL(urlAddress);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
            int statusCode = connection.getResponseCode();
            connection.disconnect();
            return statusCode;
        } catch (IOException e) {
            return -1; // Connection failed or timed out
        }
    }
}
