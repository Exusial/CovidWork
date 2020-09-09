package com.example.covidnews.kmeanview;

import android.media.MediaParser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

//从网络上提取URL的信息
public class GetData {
    private ArrayList<Event> eventArrayList;
    private ArrayList<String> stopWords;
    private LinkedHashMap<String, Integer> entites;

    public GetData(KmeansActivity activity){
        stopWords = new ArrayList<>();
        String temp = null;
        try {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(activity.getAssets().open("StopWords.txt")));
            while((temp = bufferedReader.readLine()) != null) {
                stopWords.add(temp.trim());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LinkedHashMap<String, Integer> getEntites(){
        entites = new LinkedHashMap<>();
        for(int i = 0; i <= eventArrayList.size() - 1; i++){
            LinkedHashMap<String, Integer> en = eventArrayList.get(i).getEntities();
            for(String key: en.keySet()){
                if(entites.containsKey(key)){
                    int count = entites.get(key);
                    int nes = en.get(key);
                    count += nes;
                    entites.put(key, count);
                }else{
                    entites.put(key, en.get(key));
                }
            }
        }
        return entites;
    }

    public ArrayList<Event> URLParser(){
        eventArrayList = new ArrayList<>();
        String Root = "https://covid-dashboard.aminer.cn/api/events/list?type=event";
        StringBuilder sb = new StringBuilder(Root);
        sb.append("&page=");
        sb.append("" + 1);
        sb.append("&size=");
        sb.append("" + 699);
        String U = sb.toString();
        try{
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
            JSONArray datas = (JSONArray) map.get("data");
            for(int i = 0; i <= datas.size() - 1; i ++){
                Event event = new Event();
                JSONObject object = datas.getJSONObject(i);
                //加入id
                event.set_id((String)object.get("_id"));
                //加入标题
                event.setTitle((String)object.get("title"));
                //加入关键词
                JSONArray entities = object.getJSONArray("entities");
                LinkedHashMap<String , Integer> entitiesArray = new LinkedHashMap<>();
                for(int j = 0; j <= entities.size() - 1; j++){
                    JSONObject entity = entities.getJSONObject(j);
                    String key = (String) entity.get("label");
                    if(entitiesArray.containsKey(key)){
                        int old = entitiesArray.remove(key);
                        old ++;
                        entitiesArray.put(key, old);
                    }else{
                        entitiesArray.put(key, 1);
                    }
                }
                event.setEntities(entitiesArray);

                //加入分词
                String seg_text = (String)object.get("seg_text");
                String[] segs = seg_text.split(" ");

                //去掉停用词
                ArrayList<String> segArray = RemoveOfStopWords(segs);

                LinkedHashMap<String, Double> trueSeg = new LinkedHashMap<>();

                int total = 0;

                for(int j = 0; j <= segArray.size() - 1; j++){
                    String key = segArray.get(j);
                    total += 1;
                    if(trueSeg.containsKey(key)){
                        double old = (double)trueSeg.remove(key);
                        old += 1.0;
                        trueSeg.put(key,old);
                    }else{
                        trueSeg.put(key, 1.0);
                    }
                }

                event.setSeg_text(trueSeg);

                event.setTotal(total);

                //四个东西都搞完之后
                eventArrayList.add(event);
            }
        } catch (MalformedURLException | ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return eventArrayList;
    }

    private ArrayList<String> RemoveOfStopWords(String[] oldString){
        ArrayList<String> answer = new ArrayList<>();
        for(int i = 0; i <= oldString.length - 1; i ++) {
            String p = oldString[i];
            if(p.getBytes().length != p.length())
                answer.add(oldString[i]);
        }
        answer.removeAll(stopWords);
        return answer;
    }

    public ArrayList<Event> getEventArrayList() {
        return eventArrayList;
    }
}
