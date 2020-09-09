package com.example.covidnews.kmeanview;

import java.util.HashMap;
import java.util.Map;

public class KMeansClass{
    private int id;
    private float[] center;
    Map<String, Double> values = new HashMap();

    KMeansClass(int id, float[] center){
        this.id = id;
        this.center = center;
    }

    public float[] getCenter(){
        return center;
    }

    public void PutValue(String key, Double value){
        values.put(key, value);
    }

    void clear(){
        values.clear();
    }

    public void countNewCenter(HashMap<String, float[]> wordMap){
        //消去
        for(int i = 0; i <= center.length - 1; i ++)
            center[i] = (float) 0.0;
        //计算
        for(String key : values.keySet()){
            float[] word = wordMap.get(key);
            for(int i = 0; i <= center.length - 1; i ++)
                center[i] += word[i];
        }
        //平均值
        for(int i = 0; i <= center.length - 1; i ++)
            center[i] = center[i]/ values.size();
    }

    public int getId() {
        return id;
    }

    public Map<String, Double> getValues() {
        return values;
    }
}