package com.example.covidnews.virusviews;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.covidnews.NetParser.ImageLoader;
import com.example.covidnews.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashMap;


public class ItemAdapter extends BaseQuickAdapter<relay, BaseViewHolder> {

    ItemAdapter(int layid, ArrayList<relay> list){
        super(layid,list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, relay virusItem) {
        baseViewHolder.setText(R.id.ntitle1,virusItem.name).setText(R.id.hots,virusItem.hot);
    }
}
