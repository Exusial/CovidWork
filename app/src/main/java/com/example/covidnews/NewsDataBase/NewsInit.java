package com.example.covidnews.NewsDataBase;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.covidnews.MainActivity;
import com.example.covidnews.NetParser.EventsParser;
import com.example.covidnews.NetParser.NewsParser;
import com.example.covidnews.R;
import com.example.covidnews.listviews.NewsAdapter;
import com.example.covidnews.listviews.NewsItem;

import java.util.ArrayList;
import java.util.Random;

public class NewsInit {
    private NewsAdapter adapter;
    private Handler mHandler = null;
    public NewsInit(NewsAdapter adapter){
        this.adapter = adapter;
        mHandler = new Handler();
    }

    public void InitPage(){
        new Thread(new ParseAndAdd()).start();
    }

    private class ParseAndAdd implements Runnable{
        @Override
        public void run() {
            NewsDataBase newsDataBase = NewsDataBase.getDataBase("NewsTest.db");
            EventsParser eventsParser = EventsParser.getInstance();
            eventsParser.ParseEvents();

            while(newsDataBase.getCount() < 20);

            ArrayList<News> newsArrayList = newsDataBase.getAll();
            Random random = new Random();
            NewsParser newsParser = new NewsParser(MainActivity.getMainActivity());
            for(int i =0; i <= 9; i++){
                News news = newsArrayList.get(random.nextInt(newsArrayList.size()));
                final NewsItem ni = new NewsItem(news.getTitle(), news.getTime(), null);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addData(ni);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    MainActivity activity = MainActivity.getMainActivity();
                    ImageView layout = activity.findViewById(R.id.entering);
                    layout.setVisibility(View.GONE);
                }
            });
        }
    }
}
