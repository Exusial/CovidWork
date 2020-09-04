package com.example.covidnews.listviews;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.covidnews.R;
import com.example.covidnews.ShowDataActivity;
import com.example.covidnews.expertview.ExpertActivity;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView NewList = null;
    private RefreshLayout refreshLayout;
    private EditText search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(RefreshLayout refreshlayout) {
                    refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
                }
            });
            refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(RefreshLayout refreshlayout) {
                    refreshlayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
                }
            });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        ArrayList<NewsItem> datasets = new ArrayList<>();
        datasets.add(new NewsItem("11231","#123123","zyp是大佬"));
        NewsAdapter adapter = new NewsAdapter(R.layout.news_item_layout,datasets);
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
        m_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ExpertActivity.class);
                startActivity(intent);
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
}
