package com.example.covidnews.listviews;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.covidnews.R;

public class NewsAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;


    public NewsAdapter(Context context){
        super();
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    static class ViewHolder{
        public ImageView Img1;
        public TextView Title,Date,Contents;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null){
            view = mLayoutInflater.inflate(R.layout.news_item_layout,null);
            holder = new ViewHolder();
            holder.Img1 = (ImageView) view.findViewById(R.id.iv1);
            holder.Title = (TextView) view.findViewById(R.id.ntitle1);
            holder.Date = (TextView) view.findViewById(R.id.ntitle2);
            holder.Contents = (TextView) view.findViewById(R.id.ntitle3);
            view.setTag(holder);
        }
        else{
            holder = (ViewHolder) view.getTag();
        }
        holder.Title.setText("这是标题");
        holder.Date.setText("2088.8.8");
        holder.Contents.setText("Content");
        return view;
    }
}
