package com.example.covidnews.Fresher;

import android.os.Handler;

import com.example.covidnews.MainActivity;
import com.example.covidnews.NewsDataBase.News;
import com.example.covidnews.NewsDataBase.NewsDataBase;
import com.example.covidnews.listviews.NewsAdapter;
import com.example.covidnews.listviews.NewsItem;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.Random;

public class LoadMore implements Runnable{
    private Handler mHandler;
    private NewsAdapter adapter;
    private RefreshLayout refreshLayout;

    public LoadMore(NewsAdapter adapter){
        this.adapter = adapter;
        mHandler = new Handler();
    }

    @Override
    public void run() {
        NewsDataBase newsDataBase1 = NewsDataBase.getDataBase("NewsTest.db");
        ArrayList<News> newsArrayList = newsDataBase1.getAll();
        Random random = new Random();
        for(int i =0; i <= 5; i++){
            News news = newsArrayList.get(random.nextInt(newsArrayList.size()));
            final NewsItem ni = new NewsItem(news.getTitle(), news.getTime(), null);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    MainActivity.getMainActivity().freshNews(adapter, ni,-1);
                }
            });
        }

    }
}