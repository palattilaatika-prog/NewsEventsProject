/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.newseventsmanager.newsevents;

import java.sql.Timestamp;

/**
 *
 * @author Denes
 */
public class events extends news {
    private Timestamp eventDate;
    private String Location;
    public events(String title, String description, String source, String author,
                Timestamp publishDate,Timestamp creationDate,
                Timestamp lastModifiedDate, String URL, String publisher, String language, String coverImage,
                Timestamp eventDate, String Location)
    {
        super(title, description,source,author,publishDate,creationDate,lastModifiedDate,
                URL, publisher, language, coverImage);
        this.eventDate=eventDate;
        this.Location=Location;
    }
    //get-ers, set-ers
    public Timestamp geteventDate(){return eventDate;}
    public void seteventDate(Timestamp eventDate){this.eventDate=eventDate;}
    
    public String getLocation(){return Location;}
    public void setLocation(String Location){this.Location=Location;}
    @Override
    public String toString()
    {
        return "title: " + gettitle()+" date: "+ geteventDate();
    }
}
