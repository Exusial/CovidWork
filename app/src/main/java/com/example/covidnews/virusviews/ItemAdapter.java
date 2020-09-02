package com.example.covidnews.virusviews;
import android.view.View;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.covidnews.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashMap;


public class ItemAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    ItemAdapter(int layid, ArrayList<String> list){
        super(layid,list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String virusItem) {
        baseViewHolder.setText(R.id.virus_btn1,virusItem);
    }
}
