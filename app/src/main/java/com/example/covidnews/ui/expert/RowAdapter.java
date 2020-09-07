package com.example.covidnews.ui.expert;

import android.graphics.Typeface;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.covidnews.NetParser.ImageLoader;
import com.example.covidnews.R;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RowAdapter extends BaseQuickAdapter<Expert_row, BaseViewHolder> {

    public RowAdapter(int layid, List<Expert_row> list){
        super(layid,list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Expert_row expert_row) {
        baseViewHolder.setText(R.id.ntitle1,expert_row.name).setText(R.id.ntitle2,expert_row.professions).setText(R.id.ntitle3,expert_row.inst);
        //Typeface fk = Typeface.createFromAsset(getContext().getAssets(),"fangkai.TTF");
        //TextView title = baseViewHolder.getView(R.id.ntitle1);
        //title.setTypeface(fk);
        ImageView avatar = baseViewHolder.getView(R.id.avatar);
        ImageLoader loader = new ImageLoader(getContext());
        avatar.setTag(expert_row.avatar);
        //avatar.setTag(R.id.avatar);
        System.out.println(expert_row.avatar);;
        loader.display(avatar,expert_row.avatar);
    }

}
