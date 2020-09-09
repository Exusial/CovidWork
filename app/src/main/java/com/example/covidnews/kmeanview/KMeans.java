package com.example.covidnews.kmeanview;

import com.ansj.vec.Word2VEC;
import com.ansj.vec.util.WordKmeans;

import java.io.IOException;
import java.util.*;

/**
 * 对词向量进行聚类
 */

public class KMeans{
    HashMap<String, float[]> wordMap = null;
    //迭代轮数
    private int iter;
    private KMeansClass[] Classes = null;

    public KMeans(HashMap<String, float[]> wordMap, int classesNum, int iter){
        this.iter = iter;
        this.wordMap = wordMap;
        Classes = new KMeansClass[classesNum];
    }

    public KMeansClass[] explain(){
        //取前classesNum个点作为初始的聚类中心
        Iterator<Map.Entry<String, float[]>> iterator = wordMap.entrySet().iterator();
        for (int i = 0; i <= Classes.length - 1; i++) {
            Map.Entry<String, float[]> next = iterator.next();
            Classes[i] = new KMeansClass(i, next.getValue());
        }
        //开始迭代聚类
        for(int i = 0; i <= iter - 1; i ++){
            System.out.println("开始第" + (i + 1) + "次迭代聚类");
            //首先将之前的类全部清空
            for(KMeansClass cl : Classes){
                cl.clear();
            }
            //然后开始迭代wordMap中的每一个单词，寻找最小距离的聚类中心
            for(Map.Entry<String, float[]> entry: wordMap.entrySet()){
                double minimumDistance = Double.MAX_VALUE;
                int best = 0;
                for(KMeansClass cl : Classes){
                    //计算距离
                    double distance = countEucDistance(entry.getValue(), cl.getCenter());
                    if(distance < minimumDistance){
                        minimumDistance = distance;
                        best = cl.getId();
                    }
                }
                Classes[best].PutValue(entry.getKey(), minimumDistance);
            }
            //重新计算中心点
            for(KMeansClass cl : Classes){
                cl.countNewCenter(wordMap);
            }
        }
        return Classes;
    }

    //欧几里得距离
    private double countEucDistance(float[] s1, float[] s2){
        double value = 0.0;
        for(int i = 0; i <= s1.length - 1; i ++){
            value += (s1[i] - s2[i]) * (s1[i] - s2[i]);
        }
        return value;
    }

    //余弦距离
}