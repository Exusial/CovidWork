package com.example.covidnews.newsviews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.example.covidnews.R;
import com.example.covidnews.listviews.NewsAdapter;
import com.example.covidnews.virusviews.VirusShowActivity;
import java.lang.ref.WeakReference;


public class NewsItemActivity extends AppCompatActivity {

    RecyclerView myview;
    NewsAdapter adapter;
    SafeHandler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_item);
        myview = findViewById(R.id.rview);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        myview.setLayoutManager(manager);
        adapter = new NewsAdapter(R.layout.news_item_layout,null);
        handler = new SafeHandler(this);
        Thread thread = new Thread(){
            public void run(){
                handler.sendMessage(get_data());
            }
        };
        thread.start();
    }

    public Message get_data() {
        Message msg = new Message();
        //TODO
        return msg;
    }

    private class SafeHandler extends Handler {

        private WeakReference<Context> ref;

        SafeHandler(Context context) {
            ref = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                NewsItemActivity activity = (NewsItemActivity) ref.get();
                if(activity!=null){
                    View view = getLayoutInflater().inflate(R.layout.nothing_layout,null);
                    adapter.setEmptyView(view);
                    adapter.notifyDataSetChanged();
                    //myview.setAdapter(adapter);
                }
                //System.out.println("Failed!");
            } else {
                //System.out.println("Success!");
                final VirusShowActivity activity = (VirusShowActivity)ref.get();
                if(activity!=null) {
                    //activity.findViewById(R.id.layout_emp).setVisibility(View.GONE);
                    adapter.setList(null);//数据集
                    adapter.notifyDataSetChanged();
                    adapter.addChildClickViewIds(R.id.ntitle1);
                    adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                        @Override
                        public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                            //前往详细地址

                        }
                    });
                    //myview.setAdapter(adapter);
                }
            }
        }
    }
}

