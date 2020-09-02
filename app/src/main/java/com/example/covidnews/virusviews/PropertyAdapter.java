package com.example.covidnews.virusviews;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.covidnews.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashMap;

class PropertyItem{
    public String pros;
    public String detail;
    PropertyItem(String pros,String detail){
        this.pros = pros;
        this.detail = detail;
    }
}

public class PropertyAdapter extends BaseQuickAdapter<PropertyItem, BaseViewHolder> {

    PropertyAdapter(int layid, ArrayList<PropertyItem> list){
        super(layid,list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, PropertyItem virusItem) {
        baseViewHolder.setText(R.id.ptitle,virusItem.pros).setText(R.id.pcontent,virusItem.detail);
    }
}
