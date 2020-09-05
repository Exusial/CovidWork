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
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.covidnews.NewsDataBase.NewsInit;
import com.example.covidnews.expertview.ExpertActivity;
import com.example.covidnews.Fresher.LoadMore;
import com.example.covidnews.Fresher.LoadNew;
import com.example.covidnews.NetParser.EventsParser;
import com.example.covidnews.NetParser.NewsParser;
import com.example.covidnews.NewsDataBase.News;
import com.example.covidnews.NewsDataBase.NewsDataBase;
import com.example.covidnews.listviews.KindActivity;
import com.example.covidnews.listviews.NewsAdapter;
import com.example.covidnews.listviews.NewsFragment;
import com.example.covidnews.listviews.NewsItem;
import com.example.covidnews.newsviews.NewsItemActivity;
import com.google.android.material.tabs.TabLayout;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT;

public class MainActivity extends AppCompatActivity {

    public static int max_item = 3;
    public static ArrayList<NewsFragment> save_fragments;
    public static ArrayList<NewsFragment> fragments;
    public static ArrayList<String> titles;
    public static HashMap<String,Integer> map;
    private RefreshLayout refreshLayout;
    private static MainActivity mainActivity;
    public static Integer all[] = {0,0,0};
    public static Fadatper fadatper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        titles = new ArrayList<>();
        titles.add("All");
        titles.add("NEWS");
        titles.add("PAPER");
        fragments = new ArrayList<>();
        save_fragments = new ArrayList<>();
        save_fragments.add(NewsFragment.newInstance("ALL"));
        save_fragments.add(NewsFragment.newInstance("NEWS"));
        save_fragments.add(NewsFragment.newInstance("PAPER"));
        map = new HashMap<String,Integer>();
        map.put("ALL",0);
        map.put("NEWS",1);
        map.put("PAPER",2);
        for(NewsFragment ff:save_fragments)
            fragments.add(ff);
        Button m_btn1 = findViewById(R.id.m_btn1);
        Button m_btn2 = findViewById(R.id.m_btn2);
        Button m_btn3 = findViewById(R.id.m_btn3);
        m_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, KindActivity.class);
                startActivity(intent);
            }
        });
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
        TabLayout layout = findViewById(R.id.tab_layout);
        ViewPager pager = findViewById(R.id.viewPager);
        fadatper = new Fadatper(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pager.setAdapter(fadatper);
        layout.setupWithViewPager(pager);
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
                Intent intent = new Intent(MainActivity.this,NewsItemActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("key",query);
                intent.putExtras(bundle);
                startActivity(intent);
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

    public class Fadatper extends FragmentPagerAdapter{

        public Fadatper(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return max_item;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            System.out.println(titles);
            return titles.get(position);
        }
    }
}

