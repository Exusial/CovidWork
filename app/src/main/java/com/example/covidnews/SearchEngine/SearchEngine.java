package com.example.covidnews.SearchEngine;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.covidnews.NetParser.TimeChecker;
import com.example.covidnews.NewsDataBase.News;
import com.example.covidnews.NewsDataBase.NewsDataBase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

public class SearchEngine {
    String target;
    LinkedList<News> newsArrayList;

    int page;
    boolean notAll;

    volatile boolean recall;

    public SearchEngine(String target){
        this.target = target;
        newsArrayList = new LinkedList<>();
        page = 1;
        notAll = true;
        recall = false;
    }

    public ArrayList<News> getResult(){
        //如果已经加载的结果中没有符合要求的结果
        while(newsArrayList.size() <= 10 && notAll){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    URLGet();
                }
            }).start();
            while(!recall);
            recall = false;
            page ++;
        }
        //开始加载最前面的10个
        ArrayList<News> result = new ArrayList<>();
        //获取数据库
        NewsDataBase newsDataBase = NewsDataBase.getDataBase("NewsTest.db");
        int i = 0;
        while(!newsArrayList.isEmpty() && i <= 9){
            News news = newsArrayList.remove();
            //加入进数据库里面
            newsDataBase.saveOneData(news);
            result.add(news);
        }
        return result;
    }

    //获取数据,如果已经取得了所有的数据，直接结束并且将notAll设置为false
    private void URLGet(){
        //设置需要访问的URL
        String Root = "https://covid-dashboard.aminer.cn/api/events/list?type=all";
        StringBuilder sb = new StringBuilder(Root);
        sb.append("&page=");
        sb.append("" + page);
        sb.append("&size=");
        //每次查询3000条
        sb.append("" + 3000);
        String U = sb.toString();
        try {
            //首先获取新闻条目的events
            URL url = new URL(U);
            long start = System.currentTimeMillis();
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
            long end = System.currentTimeMillis();
            Log.d("CONNECT USE TIME:", (end - start) + "");
            Map<String, JSON> map = JSONObject.parseObject(AllEvents, Map.class);
            JSONObject pagination = (JSONObject)(map.get("pagination"));
            int total = (Integer) pagination.get("total");
            JSONArray datas = (JSONArray) map.get("data");
            start = System.currentTimeMillis();
            for(int i = 0; i<=datas.size() - 1; i ++){
                News news = new News();
                JSONObject object = datas.getJSONObject(i);
                news.setTitle((String)object.get("title"));         //设置标题
                //检查标题，如果标题里没有结果，返回false，不进行接下来的访问
                if(!KMP.Match(news.getTitle(), target))
                    continue;
                news.setId((String)object.get("_id"));              //设置唯一id
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
                }
                long date = System.currentTimeMillis();
                news.setTflag(date);
                newsArrayList.push(news);
            }
            if(page * 3000 >= total)
                notAll = false;
            end = System.currentTimeMillis();
            Log.d("FENXI:" , (end - start) +"");
        } catch(IOException e) {
            e.printStackTrace();
        }

        URLReCall();
    }

    private void URLReCall(){
        recall = true;
    }
}
