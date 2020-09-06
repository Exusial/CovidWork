package com.example.covidnews.Fresher;

import android.os.Handler;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.covidnews.MainActivity;
import com.example.covidnews.NetParser.EventsParser;
import com.example.covidnews.NewsDataBase.News;
import com.example.covidnews.NewsDataBase.NewsDataBase;
import com.example.covidnews.listviews.NewsAdapter;
import com.example.covidnews.listviews.NewsFragment;
import com.example.covidnews.listviews.NewsItem;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.Random;

public class LoadMore implements Runnable{
    private Handler mHandler;
    private NewsAdapter adapter;
    private RefreshLayout refreshLayout;
    private NewsFragment fragment;
    private String kind;
    public LoadMore(NewsAdapter adapter, NewsFragment fragment, String kind){
        this.adapter = adapter;
        mHandler = new Handler();
        this.fragment = fragment;
        this.kind = kind.toLowerCase();
    }

    @Override
    public void run() {
        NewsDataBase newsDataBase = NewsDataBase.getDataBase("NewsTest.db");
        int size = (int)newsDataBase.getCount();
        Log.d("KIND:", kind);
        int limit = 100;
        Random random = new Random();
        EventsParser eventsParser = new EventsParser();
        Log.d("GO TO PARSE", "");
        ArrayList<News> newsArrayList = eventsParser.ParseNewEvents(1, limit, kind);
        for(int i = 0; i <= 5; i ++){
            int posi = random.nextInt(100);
            News news = newsArrayList.get(posi);
            Log.d("",news.getTitle());
            Log.d("",news.getId());
            final NewsItem ni = new NewsItem(news.getTitle(), news.getTime(), null);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    fragment.freshNews(adapter, ni, -1);
                }
            });
        }

    }
}
