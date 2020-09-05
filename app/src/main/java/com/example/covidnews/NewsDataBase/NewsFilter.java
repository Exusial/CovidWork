package com.example.covidnews.NewsDataBase;

import com.example.covidnews.NetParser.EventsParser;

import java.util.ArrayList;

public class NewsFilter {
    private static NewsFilter newsFilter = null;

    private NewsFilter(){}

    public NewsFilter getInstance(){
        if(newsFilter == null){
            synchronized (newsFilter){
                if(newsFilter == null)
                    newsFilter = new NewsFilter();
            }
        }
        return newsFilter;
    }

    public ArrayList<News> GetTypes(String[] Types){
        NewsDataBase newsDataBase = NewsDataBase.getDataBase("NewsTest.db");
        return newsDataBase.GetTypes(Types);
    }

    public ArrayList<News> GetTypes(ArrayList<String> Types){
        NewsDataBase newsDataBase = NewsDataBase.getDataBase("NewsTest.db");
        return newsDataBase.GetTypes(Types);
    }
}
