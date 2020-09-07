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
import com.example.covidnews.newsviews.NewsItemActivity;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.Random;

public class LoadMore implements Runnable{
    private Handler mHandler;
    private NewsAdapter adapter;
    private RefreshLayout refreshLayout;
    private NewsFragment fragment;
    private String kind;
    private NewsItemActivity mContext;

    public LoadMore(NewsAdapter adapter, NewsFragment fragment, String kind) {
        this.adapter = adapter;
        mHandler = new Handler();
        this.fragment = fragment;
        this.kind = kind.toLowerCase();
    }

    @Override
    public void run() {
        NewsDataBase newsDataBase = NewsDataBase.getDataBase("NewsTest.db");
        EventsParser eventsParser = new EventsParser();
        //先看一下已经加入了多少了
        int size = newsDataBase.getCount(kind);
        int page = size/10 + 1;

        Log.d("KIND:", kind);
        Log.d("SIZE:", size + "");
        Log.d("PAGE:", page + "");

        //获取新的东西
        ArrayList<News> newsArrayList = eventsParser.justGet(page, 20, kind);
        Log.d("FINAL:", newsArrayList.size() + "");
        if(newsArrayList.size() == 0){
            fragment.LoadReCall(2); //失败回调
        }else{
            int s = newsArrayList.size();
            for(int i = 0; i <= s-1; i ++){
                News news = newsArrayList.get(i);
                final NewsItem ni = new NewsItem(news.getTitle(), news.getTime(), null);
                ni.setKind(news.getType());
                ni.setDescription(news.getSource());
                ni.setId(news.getId());
                mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            fragment.freshNews(adapter, ni, -1);
                        }
                    });
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    fragment.LoadReCall(1);                 //正确回调
                }
            });
        }
    }
}
