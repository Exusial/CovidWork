package com.example.covidnews.globalviews;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidnews.R;
import com.example.covidnews.listviews.NewsFragment;
import com.example.covidnews.ui.notifications.KindFragment;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;

public class GlobalFragment extends Fragment {

    public static int max_item = 3;
    public static ArrayList<NewsFragment> fragments;
    public static ArrayList<String> titles;
    public static Integer all[] = {0,0,0};
    public static HashMap<String,ArrayList<Integer>> map = null;
    public static ArrayList<ListItem> data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_global, container, false);
        RecyclerView myview = root.findViewById(R.id.gview);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        myview.setLayoutManager(manager);
        GlobalAdapter adapter = new GlobalAdapter(R.layout.global_item_layout, data);
        myview.setAdapter(adapter);
        return root;
    }

    public static void process_data(){
        if(map==null)
            map = new HashMap<>();
        if(data==null)
            data = new ArrayList<>();
        for(Map.Entry<String,ArrayList<Integer>> entry:KindFragment.kv.entrySet()){
            String[] countris = entry.getKey().split("\\|");
            if(countris.length>1)
                continue;
            String country = countris[0];
            ArrayList<Integer> nums = map.get(country);
            if(nums == null){
                nums = new ArrayList<Integer>();
                nums.add(entry.getValue().get(0));
                nums.add(entry.getValue().get(3));
                nums.add(entry.getValue().get(2));
            }
            else{
                nums.set(0,nums.get(0)+entry.getValue().get(0));
                nums.set(1,nums.get(1)+entry.getValue().get(3));
                nums.set(2,nums.get(2)+entry.getValue().get(2));
            }
            map.put(country,nums);
        }
        List<Map.Entry<String,ArrayList<Integer>>> so = new ArrayList<Map.Entry<String,ArrayList<Integer>>>(map.entrySet());
        Collections.sort(so, new Comparator<Map.Entry<String, ArrayList<Integer>>>() {
            @Override
            public int compare(Map.Entry<String, ArrayList<Integer>> stringArrayListEntry, Map.Entry<String, ArrayList<Integer>> t1) {
                return -stringArrayListEntry.getValue().get(0).compareTo(t1.getValue().get(0));
            }
        });
        for(Map.Entry<String,ArrayList<Integer>> entry:so) {
            ListItem temp = new ListItem(entry.getKey(), Integer.toString(entry.getValue().get(0)), Integer.toString(entry.getValue().get(1)), Integer.toString(entry.getValue().get(2)));
            data.add(temp);
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