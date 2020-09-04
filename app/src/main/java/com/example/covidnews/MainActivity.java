package com.example.covidnews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private static MainActivity mainActivity;
    private static int newewst = 10;

    private ListView NewList = null;
    private RefreshLayout refreshLayout;
    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        ArrayList<NewsItem> datasets = new ArrayList<>();
        datasets.add(new NewsItem("11231","#123123","zyp是废物"));
        final NewsAdapter adapter = new NewsAdapter(R.layout.news_item_layout,datasets);
        RecyclerView myview = (RecyclerView)findViewById(R.id.newsview);
        myview.setLayoutManager(manager);
        myview.setAdapter(adapter);
        search = (EditText)findViewById(R.id.sed1);
        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    popwindows();
                }
            }
        });
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

        NewsDataBase newsDataBase = NewsDataBase.getDataBase("NewsTest.db");
        EventsParser eventsParser = EventsParser.getInstance();
        eventsParser.ParseEvents();

        while(newsDataBase.getCount() < 20);

        ArrayList<News> newsArrayList = newsDataBase.getAll();
        Random random = new Random();
        NewsParser newsParser = new NewsParser(this);
        for(int i =0; i <= 9; i++){
            News news = newsArrayList.get(random.nextInt(newsArrayList.size()));
            NewsItem ni = new NewsItem(news.getTitle(), news.getTime(), null);
            adapter.addData(ni);
            adapter.notifyDataSetChanged();
        }

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
                //refreshlayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
                Thread ts = new Thread(new LoadMore(adapter));
                refreshlayout.finishLoadMore(!ts.isAlive());//传入false表示刷新失败
                refreshlayout.setDisableContentWhenLoading(true);
                ts.start();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        if(ev.getAction()==MotionEvent.ACTION_DOWN){
            View now = getCurrentFocus();
            if(now==null)
                return super.dispatchTouchEvent(ev);
            int pos[] = {0,0};
            now.getLocationInWindow(pos);
            if(now instanceof EditText){
                if(ev.getX()<pos[0]||ev.getX()>pos[0]+now.getWidth()) {
                    if (ev.getY()<pos[1]||ev.getY()>pos[1]+now.getHeight()){
                        search.clearFocus();
                        IBinder tok = now.getWindowToken();
                        if(tok!=null){
                            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            if(manager!=null)
                                manager.hideSoftInputFromWindow(tok,InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void popwindows(){

        String[] list = {"123","rsdfsdf"};
        /*
        ListView view = (ListView) getLayoutInflater().inflate(R.layout.search_item_layout,null);
        view.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list));
        PopupWindow pw = new PopupWindow(view,search.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        pw.showAsDropDown(search);
        pw.setFocusable(false);

         */

        final ListPopupWindow lpw = new ListPopupWindow(MainActivity.this);
        lpw.setModal(true);
        lpw.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list));
        lpw.setAnchorView(search);
        lpw.setModal(true);
        ItemListener lis = new ItemListener(list,lpw);
        lpw.setOnItemClickListener(lis);
        lpw.show();
        search.setFocusable(true);
        //lpw.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        //lpw.setSoftInputMode(SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    class ItemListener implements AdapterView.OnItemClickListener{
        ItemListener(String[] mlist_,ListPopupWindow lpw){
            mlist = mlist_;
            temp = lpw;
        }
        private String[] mlist;
        private ListPopupWindow temp;
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            search.setText(mlist[i]);
            temp.dismiss();
        }
    }

    public static MainActivity getMainActivity(){
        return mainActivity;
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

class LoadMore implements Runnable{
    private Handler mHandler;
    private NewsAdapter adapter;
    private RefreshLayout refreshLayout;

    LoadMore(NewsAdapter adapter){
        this.adapter = adapter;
        mHandler = new Handler();
    }

    @Override
    public void run() {
        NewsDataBase newsDataBase1 = NewsDataBase.getDataBase("NewsTest.db");
        ArrayList<News> newsArrayList = newsDataBase1.getAll();
        Random random = new Random();
        for(int i =0; i <= 5; i++){
            News news = newsArrayList.get(random.nextInt(newsArrayList.size()));
            final NewsItem ni = new NewsItem(news.getTitle(), news.getTime(), null);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    MainActivity.getMainActivity().freshNews(adapter, ni,-1);
                }
            });
        }

    }
}

class LoadNew implements Runnable{
    private Handler mHandler;
    private NewsAdapter adapter;
    private int newest;
    private RefreshLayout refreshLayout;

    LoadNew(NewsAdapter adapter, int newest){
        this.adapter = adapter;
        this.newest = newest;
        mHandler = new Handler();
    }

    @Override
    public void run() {
        NewsDataBase newsDataBase = NewsDataBase.getDataBase("NewsTest.db");
        ArrayList<News> newsArrayList = newsDataBase.getAll();
        int size = newsArrayList.size();
        for(int i = newest; i <= newest + 4; i ++){
            final News news = newsArrayList.get(i);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    MainActivity.getMainActivity().freshNews(adapter,new NewsItem(news.getTitle(), news.getTime(), null),0);
                }
            });
        }
    }
}
