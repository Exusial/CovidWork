package com.example.covidnews;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.app.Activity;
import com.example.covidnews.listviews.NewsAdapter;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

public class MainActivity extends Activity {

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
        NewList = (ListView) findViewById(R.id.lv1);
        NewList.setAdapter(new NewsAdapter(MainActivity.this));
        search = (EditText)findViewById(R.id.sed1);
        search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    popwindows();
                    return true;
                }
                return false;
            }
        });

        Button m_btn1 = (Button)findViewById(R.id.m_btn1);
        Button m_btn2 = (Button)findViewById(R.id.m_btn2);
        Button m_btn3 = (Button)findViewById(R.id.m_btn3);
        m_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ShowDataActivity.class);
                startActivity(intent);
            }
        });
    }

    private void popwindows(){
        String[] list = {"123","rsdfsdf"};
        final ListPopupWindow lpw = new ListPopupWindow(MainActivity.this);
        lpw.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list));
        lpw.setAnchorView(search);
        lpw.setModal(true);
        ItemListener lis = new ItemListener(list,lpw);
        lpw.setOnItemClickListener(lis);
        lpw.show();
    }

    class ItemListener implements  AdapterView.OnItemClickListener{
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

