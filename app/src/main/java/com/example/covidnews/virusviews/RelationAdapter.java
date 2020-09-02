package com.example.covidnews.virusviews;

import android.graphics.drawable.Drawable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.covidnews.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

class RelationItem{
    public String rel;
    public boolean forward;
    public String rival;
    RelationItem(String rel,boolean forward,String rival){
        this.rel = rel;
        this.forward = forward;
        this.rival = rival;
    }
}

public class RelationAdapter extends BaseQuickAdapter<RelationItem, BaseViewHolder> {

    RelationAdapter(int layid, ArrayList<RelationItem> list){
        super(layid,list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, RelationItem item) {
        baseViewHolder.setText(R.id.ptitle,item.rel);
        if(item.forward){
            baseViewHolder.setImageResource(R.id.arrow,R.drawable.right);
        }
        baseViewHolder.setText(R.id.pcontent,item.rival);
    }
}
