package com.example.covidnews.newsviews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.example.covidnews.Fresher.LoadMore;
import com.example.covidnews.Fresher.LoadNew;
import com.example.covidnews.MainActivity;
import com.example.covidnews.NewsDataBase.News;
import com.example.covidnews.R;
import com.example.covidnews.SearchEngine.SearchEngine;
import com.example.covidnews.listviews.NewsAdapter;
import com.example.covidnews.listviews.NewsFragment;
import com.example.covidnews.listviews.NewsItem;
import com.example.covidnews.virusviews.VirusShowActivity;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class NewsItemActivity extends AppCompatActivity {
    RecyclerView myview;
    NewsAdapter adapter;
    SafeHandler handler;
    private RefreshLayout refreshLayout;
    private SearchEngine searchEngine;
    private static ArrayList<News> newsArrayList;

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
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));

        newsArrayList = new ArrayList<>();

        //得到搜索关键词
        String target = getIntent().getExtras().getString("key");

        searchEngine = new SearchEngine(target);

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                Thread ts = new Thread(){
                    public void run(){
                        handler.sendMessage(get_data());
                    }
                };
                refreshlayout.setDisableContentWhenLoading(true);
                ts.start();
            }
        });
        Thread thread = new Thread(){
            public void run(){
                handler.sendMessage(get_data());
            }
        };
        thread.start();
    }

    public Message get_data() {
        Message msg = new Message();

        newsArrayList.clear();
        newsArrayList = searchEngine.getResult();

        if(newsArrayList.size() != 0)
            msg.what = 1;
        else
            msg.what = 0;
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
                refreshLayout.finishLoadMore(false);
                NewsItemActivity activity = (NewsItemActivity) ref.get();
                if(activity!=null){
                    View view = getLayoutInflater().inflate(R.layout.nothing_layout,null);
                    adapter.setEmptyView(view);
                    adapter.notifyDataSetChanged();
                }
            } else {
                final NewsItemActivity activity = (NewsItemActivity)ref.get();
                if(activity!=null) {
                    for(int i = 0; i <= newsArrayList.size() - 1; i ++){
                        News news = newsArrayList.get(i);
                        NewsItem ni = new NewsItem(news.getTitle(), news.getTime(), null);
                        ni.setKind(news.getType());
                        ni.setDescription(news.getSource());
                        ni.setId(news.getId());
                        adapter.addData(ni);
                    }
                    adapter.notifyDataSetChanged();
                    adapter.addChildClickViewIds(R.id.ntitle1);
                    adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                        @Override
                        public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                            Intent intent = new Intent(NewsItemActivity.this, NewsDetailActivity.class);
                            NewsItem ni = (NewsItem) adapter.getItem(position);
                            String id = ni.getId();
                            Bundle bundle = new Bundle();
                            bundle.putString("id", id);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                    myview.setAdapter(adapter);
                    refreshLayout.finishLoadMore(true);
                }
            }
        }
    }

    public void freshNews(NewsAdapter adapter, NewsItem ni, int position){
        if(position == -1) {
            adapter.addData(ni);
        }
        else{
            adapter.addData(position, ni);
        }
        adapter.notifyDataSetChanged();
    }
}

