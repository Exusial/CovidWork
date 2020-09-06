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

public class NewsAdapter extends BaseQuickAdapter<NewsItem, BaseViewHolder> {

    public NewsAdapter(int layid, ArrayList<NewsItem> list){
        super(layid,list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, NewsItem newsItem) {
        baseViewHolder.setText(R.id.ntitle1,newsItem.getTitle()).setText(R.id.ntitle2,newsItem.getTime());
        baseViewHolder.setText(R.id.kind,newsItem.getKind()).setText(R.id.res,newsItem.getDescription());
    }
}
