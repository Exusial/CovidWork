package com.example.covidnews.kmeanview;

import android.content.res.AssetManager;

import com.ansj.vec.Word2VEC;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class KMeansController {
    private ArrayList<ArrayList<Event>> EventClasses;
    private ArrayList<LinkedHashMap<String, Integer>> labels;
    private ArrayList<String> ClassLabel;
    private KmeansActivity activity;

    public KMeansController(KmeansActivity activity){
        EventClasses = new ArrayList<>();
        labels = new ArrayList<>();
        ClassLabel = new ArrayList<>();
        this.activity = activity;
    }

    public void explain(){
        ArrayList<ArrayList<String>> WordsClass = new ArrayList<>();
        Word2VEC w2v = new Word2VEC();
        try {
            //加载词向量
            w2v.loadJavaModel(activity.getAssets().open("vector.mod"));

            //获得词向量
            HashMap<String, float[]> wordsMap = w2v.getWordMap();

            //创建聚类
            KMeans kmeans = new KMeans(wordsMap, 10, 50);

            //得到聚类结果
            KMeansClass[] classes = kmeans.explain();

            //开始对每一篇文章进行聚类
            for(KMeansClass cla : classes){
                Map<String, Double> cs = cla.getValues();
                ArrayList<String> clas = new ArrayList<>();
                for(Entry entry: cs.entrySet()){
                    clas.add((String)entry.getKey());
                }
                WordsClass.add(clas);
            }
            GetData getData = new GetData(activity);

            ArrayList<Event> events = getData.URLParser();


            int s = WordsClass.size();

            for(int i = 0; i <= WordsClass.size() - 1; i++){
                ArrayList<Event> events1 = new ArrayList<>();
                LinkedHashMap<String, Integer> label = new LinkedHashMap<>();
                labels.add(label);
                EventClasses.add(events1);
            }

            for(Event event : events){
                LinkedHashMap<String, Double> seg = event.getSeg_text();
                int[] labs = new int[s];
                ArrayList<LinkedHashMap<String, Integer>> whyThis = new ArrayList<>();
                for(int i = 0; i <= s- 1; i++){
                    LinkedHashMap<String, Integer> in = new LinkedHashMap<>();
                    whyThis.add(in);
                }
                for(String key : seg.keySet()){
                    for(int i = 0; i <= s - 1; i++){
                        if(WordsClass.get(i).contains(key)){
                            //加到第i类
                            labs[i] += seg.get(key);
                            setOrPlus(whyThis.get(i), key, 1);
                            break;
                        }
                    }
                }
                //找到最大的，归类
                int maxId = 0;
                int max = 0;
                for(int i = 0; i <= s- 1; i++) {
                    if (max < labs[i]) {
                        max = labs[i];
                        maxId = i;
                    }
                }
                EventClasses.get(maxId).add(event);
                for(String key : whyThis.get(maxId).keySet()){
                    int now = whyThis.get(maxId).get(key);
                    setOrPlus(labels.get(maxId), key, now);
                }
            }

            for(int i = 0; i <= labels.size() - 1; i++){
                //找到最大者
                int max = 0;
                String label = null;
                LinkedHashMap<String, Integer> h = labels.get(i);
                for(String key : h.keySet()){
                    if(h.get(key) > max){
                        max = h.get(key);
                        label = key;
                    }
                }
                ClassLabel.add(label);
                int size = EventClasses.get(i).size();
                System.out.println(label + ": " + size);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setOrPlus(LinkedHashMap<String, Integer> map, String key, int num){
        if(map.containsKey(key)){
            int now = map.get(key);
            map.put(key, now + num);
        }
        else{
            map.put(key, num);
        }
    }

    public ArrayList<ArrayList<Event>> getEventClasses() {
        return EventClasses;
    }

    public ArrayList<String> getClassLabel() {
        return ClassLabel;
    }

}
