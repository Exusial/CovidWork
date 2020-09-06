package com.example.covidnews.NetParser;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.covidnews.NewsDataBase.News;
import com.example.covidnews.NewsDataBase.NewsDataBase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import java.util.Map;

/**
 * 单例模式,后台运行
 * 用来更新数据库，从events中爬取信息，然后更新信息到数据库中
 * 赛高尼high铁鸭子哒
 */
public class EventsParser {
    private static int totalSize = 0;

    public EventsParser(){}

    //从url获取新闻条目
    private boolean GetJson(int page, int limit){
        String Root = "https://covid-dashboard.aminer.cn/api/events/list?type=all";
        StringBuilder sb = new StringBuilder(Root);
        sb.append("&page=");
        sb.append("" + page);
        sb.append("&size=");
        sb.append("" + limit);
        String U = sb.toString();
        NewsDataBase dataBase = NewsDataBase.getDataBase("NewsTest.db");
        ArrayList<News> ManyNews = new ArrayList<>();
        int addSize = 0;
        boolean res = true;
        try {
            //首先获取新闻条目的events
            URL url = new URL(U);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader cin = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder all = new StringBuilder();
            String line;
            while((line = cin.readLine()) != null){
                all.append(line);
            }
            cin.close();
            String AllEvents = all.toString();
            Map<String, JSON> map = JSONObject.parseObject(AllEvents, Map.class);
            JSONObject pagination = (JSONObject)(map.get("pagination"));
            int total = (Integer) pagination.get("total");
            JSONArray datas = (JSONArray) map.get("data");
            for(int i = 0; i<=datas.size() - 1; i ++){
                News news = new News();
                JSONObject object = datas.getJSONObject(i);
                news.setId((String)object.get("_id"));              //设置唯一id
                news.setTitle((String)object.get("title"));         //设置标题
                news.setType((String)object.get("type"));           //设置类
                news.setSource((String)object.get("source"));       //设置来源
                String time = (String)object.get("time");           //设置时间
                time = TimeChecker.CheckString(time);
                news.setTime(time);
                News alreadyHave = dataBase.getOneData(news.getId());
                if(alreadyHave == null) {
                    addSize ++;
                    ManyNews.add(news);
                }
            }
            if((dataBase.getCount() + addSize) == total)
                res = false;
            dataBase.saveData(ManyNews);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    //从url获取新闻条目
    private boolean GetJson(int page, int limit, ArrayList<News> anews){
        String Root = "https://covid-dashboard.aminer.cn/api/events/list?type=all";
        StringBuilder sb = new StringBuilder(Root);
        sb.append("&page=");
        sb.append("" + page);
        sb.append("&size=");
        sb.append("" + limit);
        String U = sb.toString();
        NewsDataBase dataBase = NewsDataBase.getDataBase("NewsTest.db");
        ArrayList<News> ManyNews = new ArrayList<>();
        int addSize = 0;
        boolean res = true;
        try {
            //首先获取新闻条目的events
            URL url = new URL(U);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader cin = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder all = new StringBuilder();
            String line;
            while((line = cin.readLine()) != null){
                all.append(line);
            }
            cin.close();
            String AllEvents = all.toString();
            Map<String, JSON> map = JSONObject.parseObject(AllEvents, Map.class);
            JSONObject pagination = (JSONObject)(map.get("pagination"));
            int total = (Integer) pagination.get("total");
            JSONArray datas = (JSONArray) map.get("data");
            for(int i = 0; i<=datas.size() - 1; i ++){
                News news = new News();
                JSONObject object = datas.getJSONObject(i);
                news.setId((String)object.get("_id"));              //设置唯一id
                news.setTitle((String)object.get("title"));         //设置标题
                news.setType((String)object.get("type"));           //设置类
                news.setSource((String)object.get("source"));       //设置来源
                String time = (String)object.get("time");           //设置时间
                time = TimeChecker.CheckString(time);
                news.setTime(time);
                News alreadyHave = dataBase.getOneData(news.getId());
                if(alreadyHave == null) {
                    addSize ++;
                    ManyNews.add(news);
                    anews.add(news);
                }
            }
            if((dataBase.getCount() + addSize) == total)
                res = false;
            dataBase.saveData(ManyNews);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    //从url获取新闻条目
    private boolean WithoutCheckGetJson(int page, int limit, ArrayList<News> anews, String type){
        String Root = "https://covid-dashboard.aminer.cn/api/events/list?type=" + type;
        StringBuilder sb = new StringBuilder(Root);
        sb.append("&page=");
        sb.append("" + page);
        sb.append("&size=");
        sb.append("" + limit);
        String U = sb.toString();
        NewsDataBase dataBase = NewsDataBase.getDataBase("NewsTest.db");
        Log.d("URL", U);
        ArrayList<News> ManyNews = new ArrayList<>();
        int addSize = 0;
        boolean res = true;
        try {
            //首先获取新闻条目的events
            URL url = new URL(U);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader cin = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder all = new StringBuilder();
            String line;
            while((line = cin.readLine()) != null){
                all.append(line);
            }
            cin.close();
            String AllEvents = all.toString();
            Map<String, JSON> map = JSONObject.parseObject(AllEvents, Map.class);
            JSONObject pagination = (JSONObject)(map.get("pagination"));
            int total = (Integer) pagination.get("total");
            JSONArray datas = (JSONArray) map.get("data");
            for(int i = 0; i<=datas.size() - 1; i ++){
                News news = new News();
                JSONObject object = datas.getJSONObject(i);
                news.setId((String)object.get("_id"));              //设置唯一id
                news.setTitle((String)object.get("title"));         //设置标题
                news.setType((String)object.get("type"));           //设置类
                news.setSource((String)object.get("source"));       //设置来源
                String time = (String)object.get("time");           //设置时间
                time = TimeChecker.CheckString(time);
                news.setTime(time);
                News alreadyHave = dataBase.getOneData(news.getId());
                if(alreadyHave == null) {
                    addSize ++;
                    ManyNews.add(news);
                }
                anews.add(news);
            }
            if((dataBase.getCount() + addSize) == total)
                res = false;
            dataBase.saveData(ManyNews);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    //更新数据
    public void ParseEvents(){
        int page = 1;
        int limit = 200;
        boolean go = true;
        while(go){
            go = GetJson(page, limit);
            page ++;
        }
    }

    public ArrayList<News> ParseNewEvents(){
        ArrayList<News> news = new ArrayList<>();
        int page = 1;
        int limit = 5;
        boolean go = true;
        while(go){
            go = GetJson(page, limit, news);
            page ++;
        }
        return news;
    }

    public ArrayList<News> ParseNewEvents(int page, int limit, String type){
        ArrayList<News> news = new ArrayList<>();
        WithoutCheckGetJson(page, limit, news, type);
        return news;
    }
}
