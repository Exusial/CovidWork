package com.example.covidnews.Fresher;

import android.os.Handler;

import androidx.fragment.app.Fragment;

import com.example.covidnews.MainActivity;
import com.example.covidnews.NewsDataBase.News;
import com.example.covidnews.NewsDataBase.NewsDataBase;
import com.example.covidnews.listviews.NewsAdapter;
import com.example.covidnews.listviews.NewsFragment;
import com.example.covidnews.listviews.NewsItem;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;

public class LoadNew implements Runnable{
    private Handler mHandler;
    private NewsAdapter adapter;
    private int newest;
    private RefreshLayout refreshLayout;
    private NewsFragment fragment;

    public LoadNew(NewsAdapter adapter, int newest, NewsFragment fragment){
        this.adapter = adapter;
        this.newest = newest;
        mHandler = new Handler();
        this.fragment = fragment;
    }

    @Override
    public void run() {
        NewsDataBase newsDataBase = NewsDataBase.getDataBase("NewsTest.db");
        ArrayList<News> newsArrayList = newsDataBase.getAll();
        int size = newsArrayList.size();
        for(int i = newest; i <= newest + 4; i ++){
            if(i<newsArrayList.size()) {
                final News news = newsArrayList.get(i);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                       fragment.freshNews(adapter, new NewsItem(news.getTitle(), news.getTime(), null), 0);
                    }
                });
            }
        }
    }
}