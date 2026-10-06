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
public class news {
    private String title;
    private String description;
    private String source;
    private String author;
    private Timestamp publishDate;
    private Timestamp creationDate;
    private Timestamp lastModifiedDate;
    private String URL;
    private String publisher;
    private String language;
    private String coverImage;
    public news(String title, String description, String source, String author,
                Timestamp publishDate,Timestamp creationDate,
                Timestamp lastModifiedDate, String URL, String publisher, String language, String coverImage )
    {
        this.title=title;
        this.description=description;
        this.source=source;
        this.author=author;
        this.publishDate=publishDate;
        this.URL=URL;
        this.publisher=publisher;
        this.language=language;
        this.creationDate=creationDate;
        this.lastModifiedDate=lastModifiedDate;
        this.coverImage=coverImage;
    }
    //get-ers , set-ers
    public Timestamp getcreationDate(){return creationDate;}
    public void setcreationDate(Timestamp creationDate){this.creationDate=creationDate;}
    
    public Timestamp getlastModifiedDate(){return lastModifiedDate;}
    public void setlastModifiedDate(Timestamp lastModifiedDate){this.lastModifiedDate=lastModifiedDate;}
    
    public String getcoverImage(){return coverImage;}
    public void setcoverImage(String CoverImage){this.coverImage=CoverImage;}
    
    public String gettitle(){return title;}
    public void settitle(String title){this.title=title;}
    
    public String getdesc(){return description;}
    public void setdesc(String description){this.description=description;}
    
    public String getsource(){return source;}
    public void setsource(String source){this.source=source;}
    
    public String getauthor(){return author;}
    public void setauthor(String author){this.author=author;}
    
    public Timestamp getpDate(){return publishDate;}
    public void setpDate(Timestamp publishDate){this.publishDate=publishDate;}
    
    public String getURL() {return URL;}
    public void setURL(String URL){this.URL=URL;}
    
    public String getpub(){return publisher;}
    public void setpub(String publisher){this.publisher=publisher;}
    
    public String getlanguage(){return language;}
    public void setlanguage(String language)
    {
        this.language=language;
    }
    @Override
    public String toString()
    {
        return "title: " +title+" source: "+source;
    }
}
