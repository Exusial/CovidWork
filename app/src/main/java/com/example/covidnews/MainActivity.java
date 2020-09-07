package com.example.covidnews;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.covidnews.NetParser.ImageLoader;
import com.example.covidnews.NewsDataBase.ImgDataBase;
import com.example.covidnews.NewsDataBase.NewsDataBase;
import com.example.covidnews.listviews.NewsAdapter;
import com.example.covidnews.listviews.NewsItem;
import com.example.covidnews.newsviews.NewsItemActivity;
import com.example.covidnews.ui.dashboard.DashboardFragment;
import com.example.covidnews.ui.expert.ExpertFragment;
import com.example.covidnews.ui.expert.MainExpertFragment;
import com.example.covidnews.ui.home.HomeFragment;
import com.example.covidnews.ui.notifications.KindFragment;
import com.example.covidnews.ui.notifications.NotificationsFragment;
import com.example.covidnews.virusviews.VirusFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

import lecho.lib.hellocharts.view.ColumnChartView;

public class MainActivity extends AppCompatActivity {

    private int sel_frag;
    private static MainActivity mainActivity;
    private Fragment[] fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = MainActivity.this;
        ImageLoader imageLoader = ImageLoader.getInstance();

        new Thread(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }).start();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:{
                        if(sel_frag!=0)
                        {
                            switchFragment(sel_frag,0);
                            sel_frag=0;
                        }
                        return true;
                    }
                    case R.id.navigation_dashboard:{
                        if(sel_frag!=1)
                        {
                            switchFragment(sel_frag,1);
                            sel_frag=1;
                        }
                        return true;
                    }
                    case R.id.navigation_notifications:{
                        if(sel_frag!=2)
                        {
                            switchFragment(sel_frag,2);
                            sel_frag=2;
                        }
                        return true;
                    }
                    case R.id.navigation_expert:{
                        if(sel_frag!=3)
                        {
                            switchFragment(sel_frag,3);
                            sel_frag=3;
                        }
                        return true;
                    }
                    case R.id.navigation_knowledge:{
                        if(sel_frag!=4)
                        {
                            switchFragment(sel_frag,4);
                            sel_frag=4;
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void init(){
        NewsDataBase newsDataBase = NewsDataBase.getDataBase("NewsTest.db");
        newsDataBase.DeleteByTflag();
        ImgDataBase imgDataBase = ImgDataBase.getDataBase("ImgTest.db");
        imgDataBase.DeleteByTflag();
        HomeFragment.setData();
        HomeFragment homeFragment = new HomeFragment();
        KindFragment notificationsFragment = new KindFragment();
        DashboardFragment dashboardFragment = new DashboardFragment();
        MainExpertFragment expertFragment = new MainExpertFragment();
        VirusFragment virusFragment = new VirusFragment();
        fragments = new Fragment[]{homeFragment,dashboardFragment,notificationsFragment,expertFragment,virusFragment};
        getSupportFragmentManager().beginTransaction().add(R.id.layout,homeFragment).show(homeFragment).commitAllowingStateLoss();;
        sel_frag = 0;

    }

    @SuppressLint("RestrictedApi")
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.first_main_menu,menu);
        final MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView view = (SearchView) searchItem.getActionView();
        view.setQueryHint("搜索新闻");
        view.setSubmitButtonEnabled(true);
        view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MainActivity.this, NewsItemActivity.class);
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
        textview.setBackgroundColor(Color.parseColor("#FFFFFF"));
        textview.setTextColor(Color.parseColor("#000000"));
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

    public static MainActivity getMainActivity(){
        return mainActivity;
    }

    private void switchFragment(int lastfragment,int index)
    {
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastfragment]);//隐藏上个Fragment
        if(fragments[index].isAdded()==false)
        {
            transaction.add(R.id.layout,fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();


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
