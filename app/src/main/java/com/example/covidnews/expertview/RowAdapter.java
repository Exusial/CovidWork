package com.example.covidnews.expertview;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.covidnews.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RowAdapter extends BaseQuickAdapter<Expert_row, BaseViewHolder> {

    public RowAdapter(int layid, List<Expert_row> list){
        super(layid,list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Expert_row expert_row) {
        baseViewHolder.setText(R.id.ntitle1,expert_row.name).setText(R.id.ntitle2,expert_row.professions).setText(R.id.ntitle3,expert_row.inst);
    }
}
