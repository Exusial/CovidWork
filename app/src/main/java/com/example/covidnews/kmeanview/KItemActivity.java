package com.example.covidnews.kmeanview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.example.covidnews.NewsDataBase.News;
import com.example.covidnews.R;
import com.example.covidnews.SearchEngine.SearchEngine;
import com.example.covidnews.listviews.NewsAdapter;
import com.example.covidnews.listviews.NewsItem;
import com.example.covidnews.newsviews.NewsDetailActivity;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class KItemActivity extends AppCompatActivity {
    RecyclerView myview;
    NewsAdapter adapter;
    private RefreshLayout refreshLayout;
    private SearchEngine searchEngine;
    private static ArrayList<Event> eventsArrayList;
    private ArrayList<NewsItem> shows;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_item);
        shows = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        myview = findViewById(R.id.rview);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        myview.setLayoutManager(manager);
        adapter = new NewsAdapter(R.layout.news_item_layout,null);
        eventsArrayList = KmeansActivity.getmap().get(id);
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        for(int i=0;i<(15>eventsArrayList.size()?eventsArrayList.size():15);i++){
            shows.add(new NewsItem());//查询数据库
        }
        eventsArrayList = new ArrayList<>();

        //得到搜索关键词
        String target = getIntent().getExtras().getString("key");

        searchEngine = new SearchEngine(target);

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                for(int i=0;i<(15>eventsArrayList.size()?eventsArrayList.size():15);i++){
                    shows.add(new NewsItem());//查询数据库
                }
            }
        });

    }


}

