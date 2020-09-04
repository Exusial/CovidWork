package com.example.covidnews.listviews;
public class NewsItem{
    private String title;
    private String time;
    private String description;

    public NewsItem(String title,String time,String description){
        this.title = title;
        this.time = time;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}