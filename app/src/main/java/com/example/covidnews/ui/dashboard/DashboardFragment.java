package com.example.covidnews.ui.dashboard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.example.covidnews.R;
import com.example.covidnews.listviews.NewsAdapter;
import com.example.covidnews.listviews.NewsItem;
import com.example.covidnews.newsviews.NewsDetailActivity;
import com.example.covidnews.ui.home.HomeFragment;

public class DashboardFragment extends Fragment {

    private View root;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final Button k_btn1 = root.findViewById(R.id.k_btn1);
        final Button k_btn2 = root.findViewById(R.id.k_btn2);
        final Button k_btn3 = root.findViewById(R.id.k_btn3);
        final Button k_btn4 = root.findViewById(R.id.k_btn4);
        final Button k_btn5 = root.findViewById(R.id.k_btn5);
        final Button k_btn6 = root.findViewById(R.id.k_btn6);
        if(HomeFragment.all[0]==0)
            k_btn4.setVisibility(View.GONE);
        else
            k_btn1.setVisibility(View.GONE);
        if(HomeFragment.all[1]==0)
            k_btn5.setVisibility(View.GONE);
        else
            k_btn2.setVisibility(View.GONE);
        if(HomeFragment.all[2]==0)
            k_btn6.setVisibility(View.GONE);
        else
            k_btn3.setVisibility(View.GONE);
        k_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(HomeFragment.max_item==1)
                    return;
                k_btn4.setVisibility(View.VISIBLE);
                k_btn1.setVisibility(View.GONE);
                HomeFragment.max_item -= 1;
                HomeFragment.all[0] = 1;
                int index = HomeFragment.map.get("ALL");
                if(HomeFragment.map.get("NEWS")!=null&&HomeFragment.map.get("NEWS")>index)
                    HomeFragment.map.put("NEWS",HomeFragment.map.get("NEWS")-1);
                if(HomeFragment.map.get("PAPER")!=null&&HomeFragment.map.get("PAPER")>index)
                    HomeFragment.map.put("PAPER",HomeFragment.map.get("PAPER")-1);
                HomeFragment.fragments.remove(index);
                HomeFragment.titles.remove(index);
                HomeFragment.map.put("ALL",null);
                HomeFragment.fadatper.notifyDataSetChanged();
            }
        });
        k_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(HomeFragment.max_item==1)
                    return;
                k_btn5.setVisibility(View.VISIBLE);
                k_btn2.setVisibility(View.GONE);
                HomeFragment.max_item -= 1;
                HomeFragment.all[1] = 1;
                int index = HomeFragment.map.get("NEWS");
                if(HomeFragment.map.get("PAPER")!=null&&HomeFragment.map.get("PAPER")>index)
                    HomeFragment.map.put("PAPER",HomeFragment.map.get("PAPER")-1);
                if(HomeFragment.map.get("ALL")!=null&&HomeFragment.map.get("ALL")>index)
                    HomeFragment.map.put("ALL",HomeFragment.map.get("ALL")-1);
                HomeFragment.fragments.remove(index);
                HomeFragment.titles.remove(index);
                HomeFragment.map.put("NEWS",null);
                HomeFragment.fadatper.notifyDataSetChanged();
            }
        });
        k_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(HomeFragment.max_item==1)
                    return;
                k_btn6.setVisibility(View.VISIBLE);
                k_btn3.setVisibility(View.GONE);
                HomeFragment.max_item -= 1;
                HomeFragment.all[2] = 1;
                int index = HomeFragment.map.get("PAPER");
                if(HomeFragment.map.get("ALL")!=null&&HomeFragment.map.get("ALL")>index)
                    HomeFragment.map.put("ALL",HomeFragment.map.get("ALL")-1);
                if(HomeFragment.map.get("NEWS")!=null&&HomeFragment.map.get("NEWS")>index)
                    HomeFragment.map.put("NEWS",HomeFragment.map.get("NEWS")-1);
                HomeFragment.fragments.remove(index);
                HomeFragment.titles.remove(index);
                HomeFragment.map.put("PAPER",null);
                HomeFragment.fadatper.notifyDataSetChanged();
            }
        });
        k_btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                k_btn1.setVisibility(View.VISIBLE);
                k_btn4.setVisibility(View.GONE);
                HomeFragment.max_item += 1;
                HomeFragment.all[0] = 0;
                System.out.println("save "+HomeFragment.save_fragments.get(0).getTag());
                HomeFragment.fragments.add(HomeFragment.save_fragments.get(0));
                HomeFragment.titles.add("ALL");
                HomeFragment.map.put("ALL",HomeFragment.fragments.size()-1);
                HomeFragment.fadatper.notifyDataSetChanged();
            }
        });
        k_btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                k_btn2.setVisibility(View.VISIBLE);
                k_btn5.setVisibility(View.GONE);
                HomeFragment.max_item += 1;
                HomeFragment.all[1] = 0;
                System.out.println("save "+HomeFragment.save_fragments.get(1).getTag());
                HomeFragment.fragments.add(HomeFragment.save_fragments.get(1));
                HomeFragment.titles.add("NEWS");
                HomeFragment.map.put("NEWS",HomeFragment.fragments.size()-1);
                HomeFragment.fadatper.notifyDataSetChanged();
            }
        });
        k_btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                k_btn3.setVisibility(View.VISIBLE);
                k_btn6.setVisibility(View.GONE);
                System.out.println("save "+HomeFragment.save_fragments.get(2).getTag());
                HomeFragment.max_item += 1;
                HomeFragment.all[2] = 0;
                HomeFragment.fragments.add(HomeFragment.save_fragments.get(2));
                HomeFragment.titles.add("PAPER");
                HomeFragment.map.put("PAPER",HomeFragment.fragments.size()-1);
                HomeFragment.fadatper.notifyDataSetChanged();
            }
        });
        return root;
    }
}