package com.example.covidnews.globalviews;
import android.content.Intent;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import com.example.covidnews.R;
import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;

class ListItem{
    private String name;
    private String Infected;
    private String Dead;
    private String Cured;
    ListItem(){}
    ListItem(String name_,String Infected,String Dead,String Cured){
        this.name =name_;
        this.Infected = Infected;
        this.Dead = Dead;
        this.Cured = Cured;
    }

    String getName(){return name;}
    String getInfected(){return Infected;}
    String getDead(){return Dead;}
    String getCured(){return Cured;}
}

public class GlobalAdapter extends BaseQuickAdapter<ListItem, BaseViewHolder> {

    GlobalAdapter(int layid, ArrayList<ListItem> list){
        super(layid,list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ListItem listItem) {
        int pos = baseViewHolder.getLayoutPosition();
        baseViewHolder.setText(R.id.country, listItem.getName()).setText(R.id.infected, listItem.getInfected()).setText(R.id.dead, listItem.getDead()).setText(R.id.cured, listItem.getCured());
    }
}
