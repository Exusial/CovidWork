package com.example.covidnews.NetParser;

import android.os.Handler;
import android.util.Log;

import com.alibaba.fastjson.JSONReader;
import com.example.covidnews.NewsDataBase.News;
import com.example.covidnews.NewsDataBase.NewsDataBase;
import com.example.covidnews.listviews.NewsAdapter;
import com.example.covidnews.listviews.NewsItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 单例模式,后台运行
 * 用来更新数据库，从events中爬取信息，然后更新信息到数据库中
 * 赛高尼high铁鸭子哒
 */
public class EventsParser {
    //声明线程池，全局仅有一个，这里只需要1个线程来完成这个任务
    private static ExecutorService mPool;

    private static EventsParser eventsParser = null;

    private EventsParser(){
        if(mPool == null){
            //只能够也只需要由一个线程完成
            mPool = Executors.newFixedThreadPool(1);
        }
    }

    //单例模式构建
    public static EventsParser getInstance(){
        if(eventsParser == null){
            synchronized (EventsParser.class){
                if(eventsParser == null){
                    eventsParser = new EventsParser();
                }
            }
        }
        return eventsParser;
    }

    //从url获取新闻条目
    private Events GetJson(){
        Events event = new Events();
        try {
            //首先获取新闻条目的events
            long start = System.currentTimeMillis();
            Log.d("","开始读取");
            URL url = new URL("https://covid-dashboard.aminer.cn/api/dist/events.json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader cin = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            Log.d("OVER:", "阅读完成");
            JSONReader reader = new JSONReader(cin);
            int count = 0;
            reader.readObject(event);
            reader.close();
        } catch(IOException e){
            e.printStackTrace();
        }
        return event;
    }

    //更新数据
    private class dataParseEvents implements Runnable {
        public dataParseEvents(){}
        @Override
        public void run() {
            Events events = GetJson();
            ArrayList<Data> datas = events.getDatas();
            NewsDataBase newsDataBase = NewsDataBase.getDataBase("NewsTest.db");
            if (datas != null) {
                String startid = datas.get(0).get_id();
                News start = newsDataBase.getOneData(startid);
                boolean Already = (start == null)?false : true;
                int size = datas.size();
                for (int i = size - 1; i >=0; i--) {
                    Data data = datas.get(i);
                    News news = newsDataBase.getOneData(data.get_id());
                    //如果数据库中没有,则加入
                    if (news == null) {
                        news = new News();
                        news.setId(data.get_id());
                        news.setType(data.getType());
                        news.setTitle(data.getTitle());
                        news.setTime(data.getTime());
                        news.setLanguage(data.getLang());
                        //news.setInfluence(Double.parseDouble(data.getInfluence()));
                        newsDataBase.saveOneData(news);
                    }else if(Already == true){                                          //否则如果存在并且第一条不为空，结束
                        break ;
                    }
                }
            }
        }
    }

    public void ParseEvents(){
        mPool.execute(new dataParseEvents());
    }
}
