package com.example.covidnews.NetParser;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
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

import java.util.Date;
import java.util.Map;

/**
 * 用来更新数据库，从events中爬取信息，然后更新信息到数据库中
 * 赛高尼high铁鸭子哒
 */

public class EventsParser {
    private static int totalSize = 0;

    public EventsParser(){}

    //从url获取新闻条目
    //把没有的全部加进去
    private int GetJson(int page, int limit, String type, ArrayList<News> newsForAdd){
        String Root = "https://covid-dashboard.aminer.cn/api/events/list?type=" + type;
        StringBuilder sb = new StringBuilder(Root);
        sb.append("&page=");
        sb.append("" + page);
        sb.append("&size=");
        sb.append("" + limit);
        String U = sb.toString();
        NewsDataBase dataBase = NewsDataBase.getDataBase("NewsTest.db");
        ArrayList<News> ManyNews = new ArrayList<>();
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
                news.setContent((String)object.get("content"));     //设置内容
                news.setLanguage((String)object.get("lang"));       //设置语言
                time = time.split(" ")[0];
                time = TimeChecker.CheckString(time);
                news.setTime(time);
                if(news.getType().equals("news")){                  //设置新闻图片
                    JSONArray urls = object.getJSONArray("urls");
                    if(urls.size() != 0){
                        String u = (String)urls.get(0);
                        news.setSourceUrl(u);
                    }
                }else if(news.getType().equals("paper")){           //设置来源pdf
                    JSONArray urls = object.getJSONArray("urls");
                    if(urls.size() != 0){
                        StringBuilder img = new StringBuilder();
                        for(int j = 0; j <= urls.size() - 1; j ++){
                            img.append((String)urls.get(j));
                            img.append(",");
                        }
                        String ImgUrls = img.toString();
                        news.setSourceUrl(ImgUrls);
                    }
                    //设置作者名字
                    JSONArray names = object.getJSONArray("authors");
                    StringBuilder nameBuilder = new StringBuilder();
                    for(int j = 0; j <= names.size() - 1; j++){
                        JSONObject author = names.getJSONObject(j);
                        String name = (String)author.get("name");
                        nameBuilder.append(name);
                        nameBuilder.append(",");
                    }
                    String nm = nameBuilder.toString();
                    news.setAuthorName(nm);

                    changeType(type, news);
                }

                long date = System.currentTimeMillis();
                news.setTflag(date);

                News alreadyHave = dataBase.getOneData(news.getId());//记录时间戳，定时清楚记录
                if(alreadyHave == null) {                            //如果没有才加入
                    ManyNews.add(news);
                    newsForAdd.add(news);
                }else{
                    //否则判断是否已经加入在该栏目中了
                    if(!getType(type, alreadyHave)){
                        changeType(type, alreadyHave);
                        ManyNews.add(alreadyHave);
                        newsForAdd.add(news);
                    }
                }
            }
            dataBase.saveData(ManyNews);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return ManyNews.size();
    }

    private void changeType(String type, News news){
        switch (type){
            case "paper":
                news.setAddPaper(true);
                break;
            case "news":
                news.setAddNews(true);
                break;
            default:
                news.setAddAll(true);
                break;
        }
    }

    private boolean getType(String type, News news){
        boolean res = false;
        switch (type){
            case "paper":
                res = news.getAddPaper();
                break;
            case "news":
                res = news.getAddNews();
                break;
            default:
                res = news.getAddAll();
                break;
        }
        return res;
    }

    //添加最新的进去
    public ArrayList<News> getNews(String type){
        int page = 1;
        int limit = 5;
        ArrayList<News> news = new ArrayList<>();
        while(true) {
            int size = GetJson(page, limit, type, news);
            if(size != limit)
                break;
        }
        return news;
    }

    //Load更多的，通常
    public ArrayList<News> justGet(int page, int limit, String type){
        ArrayList<News> news = new ArrayList<>();
        GetJson(page, limit, type, news);
        return news;
    }
}
