package com.example.covidnews.listviews;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.covidnews.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class NewsItem{
    private String title;
    private String time;
    private String description;

    public NewsItem(String title,String time,String description){
        this.title = title;
        this.time = time;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }
}
public class NewsAdapter extends BaseQuickAdapter<NewsItem, BaseViewHolder> {

    NewsAdapter(int layid, ArrayList<NewsItem> list){
        super(layid,list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, NewsItem newsItem) {
        baseViewHolder.setText(R.id.ntitle1,newsItem.getTitle()).setText(R.id.ntitle2,newsItem.getTime()).setText(R.id.ntitle3,newsItem.getDescription());
    }
}
