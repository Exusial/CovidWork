package com.example.covidnews.newsviews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.covidnews.R;
import com.example.covidnews.listviews.NewsItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NewsItemActivity extends AppCompatActivity {

    RecyclerView myview;
    NewsItemAdapter adpater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_item);
        myview = findViewById(R.id.rview);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        myview.setLayoutManager(manager);
    }
}

class NewsItemAdapter extends BaseQuickAdapter<NewsItem, BaseViewHolder>{

    public NewsItemAdapter(int layid, List<NewsItem> list){
        super(layid,list);
    }
    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, NewsItem newsItem) {

    }
}
