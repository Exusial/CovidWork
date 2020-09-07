package com.example.covidnews.provinceviews;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.covidnews.R;
import com.example.covidnews.listviews.NewsFragment;
import com.example.covidnews.ui.notifications.KindFragment;
import com.google.android.material.tabs.TabLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class ProvinceFragment extends Fragment {

    public static int max_item = 3;
    public static ArrayList<NewsFragment> fragments;
    public static ArrayList<String> titles;
    public static Integer all[] = {0,0,0};
    public static HashMap<String,ArrayList<Integer>> prolist = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_province, container, false);
        RefreshLayout refreshlayout = root.findViewById(R.id.refreshLayout);
        refreshlayout.setEnableRefresh(false);
        refreshlayout.setEnableLoadMore(false);
        ColumnChartView colview = root.findViewById(R.id.col1);
        int subcol = 3;
        List<Column> cols = new ArrayList<Column>();
        List<SubcolumnValue> subcols;
        ArrayList<String> names = new ArrayList<String>();
        float count = 0.f;
        ArrayList<Float> maps = new ArrayList<>();
        for(Map.Entry<String,ArrayList<Integer>> entry:prolist.entrySet()){
            subcols = new ArrayList<SubcolumnValue>();
            maps.add(count);
            count++;
            names.add(entry.getKey());
            for(int j=0;j<subcol;j++){
                if(j==0) {
                    subcols.add(new SubcolumnValue(entry.getValue().get(j), Color.parseColor("#DC143C")));
                }
                else if(j==1){
                    subcols.add(new SubcolumnValue(entry.getValue().get(j),Color.parseColor("#708090")));
                }
                else{
                    subcols.add(new SubcolumnValue(entry.getValue().get(j),Color.parseColor("#6495ED")));
                }

            }
            Column col = new Column(subcols);
            col.setHasLabels(true);
            col.setHasLabelsOnlyForSelected(true);
            cols.add(col);
        }
        ColumnChartData data = new ColumnChartData(cols);
        Axis X,Y;
        X = Axis.generateAxisFromCollection(maps,names);
        Y = new Axis();
        Y.setHasLines(true);
        //X.setName("省份");
        Y.setName("");
        data.setAxisXBottom(X);
        data.setAxisYLeft(Y);
        colview.setColumnChartData(data);
        Viewport viewport = new Viewport(1,colview.getMaximumViewport().height()*1.25f,names.size()>5?5:names.size(),0);
        colview.setCurrentViewport(viewport);
        colview.moveTo(1,0);
        return root;
    }

    public static void process_data(){
        if(prolist == null)
            prolist = new HashMap<>();
        for(Map.Entry<String, ArrayList<Integer>> entry:KindFragment.kv.entrySet()) {
            String[] countris = entry.getKey().split("\\|");
            ArrayList<Integer> ndata = entry.getValue();
            if (countris.length != 2 || !countris[0].equals("China"))
                continue;
            String province = countris[1];
            ArrayList<Integer> nums = KindFragment.kv.get(province);
            if (nums == null) {
                nums = new ArrayList<Integer>();
                nums.add(ndata.get(0));
                nums.add(ndata.get(3));
                nums.add(ndata.get(2));
            } else {
                nums.set(0, nums.get(0) + ndata.get(0));
                nums.set(1, nums.get(1) + ndata.get(3));
                nums.set(2, nums.get(2) + ndata.get(2));
            }
            prolist.put(province, nums);
        }
    }



}

class single_data{

    private String begin;
    private ArrayList<ArrayList<Integer>> data;
    public String getBegin(){
        return this.begin;
    }

    public ArrayList<ArrayList<Integer>> getData() {
        return data;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public void setData(ArrayList<ArrayList<Integer>> data) {
        this.data = data;
    }
}