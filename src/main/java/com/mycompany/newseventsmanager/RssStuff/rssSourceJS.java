/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.newseventsmanager.RssStuff;

/**
 *
 * @author Denes
 */
public class rssSourceJS {
    private String name;
    private String URL;
    private String language;
    public rssSourceJS(String name, String URL, String language)
    {
        this.name=name;
        this.URL=URL;
        this.language=language;
    }
    public String getName(){return name; }
    public String getURL(){return URL;}
    public String getlanguage(){return language;}
    
    public void setname(String name){this.name=name;}
    public void seturl(String URL){this.URL=URL;}
    public void setlanguage(String language){this.language=language;}
}
