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
import java.util.Random;

public class LoadMore implements Runnable{
    private Handler mHandler;
    private NewsAdapter adapter;
    private RefreshLayout refreshLayout;
    private NewsFragment fragment;
    public LoadMore(NewsAdapter adapter, NewsFragment fragment){
        this.adapter = adapter;
        mHandler = new Handler();
        this.fragment = fragment;
    }

    @Override
    public void run() {
        NewsDataBase newsDataBase1 = NewsDataBase.getDataBase("NewsTest.db");
        ArrayList<News> newsArrayList = newsDataBase1.getAll();
        Random random = new Random();
        for(int i =0; i <= 5; i++) {
            if (i < newsArrayList.size()) {
                News news = newsArrayList.get(random.nextInt(newsArrayList.size()));
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
}