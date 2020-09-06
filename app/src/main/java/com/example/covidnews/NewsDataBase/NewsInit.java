package com.example.covidnews.NewsDataBase;

import android.os.Handler;
import android.util.Log;

import com.example.covidnews.NetParser.EventsParser;
import com.example.covidnews.listviews.NewsAdapter;
import com.example.covidnews.listviews.NewsItem;

import java.util.ArrayList;

public class NewsInit {

    private NewsAdapter adapter;
    private Handler mHandler = null;
    private String kind;

    public NewsInit(NewsAdapter adapter, String kind){
        this.adapter = adapter;
        mHandler = new Handler();
        this.kind = kind.toLowerCase();
    }

    public void InitPage(){
        new Thread(new ParseAndAdd()).start();
    }

    private class ParseAndAdd implements Runnable{
        @Override
        public void run() {
            NewsDataBase newsDataBase = NewsDataBase.getDataBase("NewsTest.db");

            EventsParser eventsParser = new EventsParser();

            ArrayList<News> newsArrayList = newsDataBase.GetTypes(kind,10,0);

            if(newsArrayList.size() < 10){
                newsArrayList = eventsParser.justGet(1,10, kind);
            }
            for(int i = 0; i <= newsArrayList.size() - 1; i ++){
                News news = newsArrayList.get(i);
                final NewsItem ni = new NewsItem(news.getTitle(), news.getTime(), null);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addData(ni);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }
}
