package com.example.covidnews;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidnews.expertview.ExpertActivity;
import com.example.covidnews.Fresher.LoadMore;
import com.example.covidnews.Fresher.LoadNew;
import com.example.covidnews.NetParser.EventsParser;
import com.example.covidnews.NetParser.NewsParser;
import com.example.covidnews.NewsDataBase.News;
import com.example.covidnews.NewsDataBase.NewsDataBase;
import com.example.covidnews.listviews.NewsAdapter;
import com.example.covidnews.listviews.NewsItem;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private RefreshLayout refreshLayout;
    private static int newewst = 10;
    private static MainActivity mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        ArrayList<NewsItem> datasets = new ArrayList<>();
        RecyclerView myview = (RecyclerView)findViewById(R.id.newsview);
        myview.setLayoutManager(manager);
        datasets.clear();
        final NewsAdapter adapter = new NewsAdapter(R.layout.news_item_layout,datasets);
        adapter.setUseEmpty(true);
        datasets.clear();
        adapter.notifyDataSetChanged();
        View emp = getLayoutInflater().inflate(R.layout.loading_layout,null);
        adapter.setEmptyView(emp);
        myview.setAdapter(adapter);
        Button m_btn1 = (Button)findViewById(R.id.m_btn1);
        Button m_btn2 = (Button)findViewById(R.id.m_btn2);
        Button m_btn3 = (Button)findViewById(R.id.m_btn3);
        m_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ShowDataActivity.class);
                startActivity(intent);
            }
        });
        m_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ExpertActivity.class);
                startActivity(intent);
            }
        });

        Thread thread1 = new Thread(){
            public void run(){
                NewsDataBase newsDataBase = NewsDataBase.getDataBase("NewsTest.db");
                EventsParser eventsParser = EventsParser.getInstance();
                eventsParser.ParseEvents();

                while(newsDataBase.getCount() < 20);

                ArrayList<News> newsArrayList = newsDataBase.getAll();
                Random random = new Random();
                NewsParser newsParser = new NewsParser(MainActivity.this);
                for(int i =0; i <= 9; i++){
                    News news = newsArrayList.get(random.nextInt(newsArrayList.size()));
                    NewsItem ni = new NewsItem(news.getTitle(), news.getTime(), null);
                    adapter.addData(ni);
                    adapter.notifyDataSetChanged();
                }
                Looper.prepare();
                Looper.loop();
            }
        };
        thread1.start();
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(RefreshLayout refreshlayout) {
                    Thread ts = new Thread(new LoadNew(adapter, newewst));
                    refreshlayout.finishRefresh(!ts.isAlive());//传入false表示刷新失败
                    refreshlayout.setDisableContentWhenRefresh(true);
                    ts.start();
                    newewst +=5;
                }
            });
            refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(RefreshLayout refreshlayout) {
                    Thread ts = new Thread(new LoadMore(adapter));
                    refreshlayout.finishLoadMore(!ts.isAlive());//传入false表示刷新失败
                    refreshlayout.setDisableContentWhenLoading(true);
                    ts.start();
                }
            });
    }

    @SuppressLint("RestrictedApi")
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        final MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView view = (SearchView) searchItem.getActionView();
        view.setQueryHint("搜索新闻");
        view.setSubmitButtonEnabled(true);
        view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        final SearchView.SearchAutoComplete textview = view.findViewById(R.id.search_src_text);
        textview.setThreshold(0);
        final ArrayList<String> history = new ArrayList<>();
        history.add("?????");
        history.add("!!!!");
        ArrayAdapter<String> temp = new ArrayAdapter<String>(this,R.layout.search_item_layout,history);
        textview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                textview.setText(history.get(i));
            }
        });
        textview.setAdapter(temp);
        return super.onCreateOptionsMenu(menu);
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
    
    public static MainActivity getMainActivity(){
        return mainActivity;
    }
}

