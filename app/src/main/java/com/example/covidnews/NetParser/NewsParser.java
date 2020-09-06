package com.example.covidnews.NetParser;

import android.content.Context;
import android.os.Handler;
import android.util.LruCache;

import com.example.covidnews.NewsDataBase.News;
import com.example.covidnews.NewsDataBase.NewsDataBase;
import com.example.covidnews.listviews.NewsAdapter;
import com.example.covidnews.listviews.NewsItem;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import objectexplorer.MemoryMeasurer;

public class NewsParser {
    //定义一个缓存空间
    private static LruCache<String, NData> NewsCaches;

    //声明线程池，全局仅有一个，用来控制访问的空间
    private static ExecutorService mPool;

    //定义上下文对象
    private Context mContext;

    //解决错位问题
    private Map<NewsItem, String> mNewsInfoTags = new LinkedHashMap<>();

    public NewsParser(Context context){
        this.mContext = context;
        if(NewsCaches == null){
            //申请一个空间
            int maxSize = (int)(Runtime.getRuntime().freeMemory() / 4);
            maxSize = maxSize <= 0? 100 : maxSize;
            //实例化
            NewsCaches = new LruCache<String, NData>(maxSize){
                @Override
                protected int sizeOf(String key, NData data){
                    long memory = MemoryMeasurer.measureBytes(data);
                    return (int)memory;
                }
            };
        }
        if(mPool == null)
            mPool = Executors.newFixedThreadPool(3);
    }
}
