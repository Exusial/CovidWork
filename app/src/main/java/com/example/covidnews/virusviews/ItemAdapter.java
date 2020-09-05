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
        baseViewHolder.setText(R.id.ntitle1,virusItem.name);
        baseViewHolder.setText(R.id.ntitle3,virusItem.description);
        ImageLoader loader = new ImageLoader(getContext());
        ImageView view = baseViewHolder.getView(R.id.avatar);
        if(virusItem.image!=null&&!virusItem.image.equals(""))
            loader.display(view,virusItem.image);
        else {
            view.setVisibility(View.GONE);
            if(virusItem.description==null||virusItem.description.equals("")){
                TextView textView = baseViewHolder.getView(R.id.ntitle3);
                textView.setVisibility(View.GONE);
                LinearLayout layout = baseViewHolder.getView(R.id.outline1);
                ViewGroup.LayoutParams lp = layout.getLayoutParams();
                lp.height = 100;
                layout.setLayoutParams(lp);
        }}
    }
}
