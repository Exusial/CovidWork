package com.example.covidnews.ui.home;

import android.os.Bundle;
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
import com.example.covidnews.MainActivity;
import com.example.covidnews.R;
import com.example.covidnews.listviews.NewsFragment;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.HashMap;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class HomeFragment extends Fragment {

    public static int max_item = 3;
    public static ArrayList<NewsFragment> save_fragments;
    public static ArrayList<NewsFragment> fragments;
    public static ArrayList<String> titles;
    public static HashMap<String,Integer> map;
    private static MainActivity mainActivity;
    public static Integer all[] = {0,0,0};
    public static Fadatper fadatper;

    public static void setData(){
        titles = new ArrayList<>();
        titles.add("All");
        titles.add("NEWS");
        titles.add("PAPER");
        fragments = new ArrayList<>();
        save_fragments = new ArrayList<>();
        save_fragments.add(NewsFragment.newInstance("ALL"));
        save_fragments.add(NewsFragment.newInstance("NEWS"));
        save_fragments.add(NewsFragment.newInstance("PAPER"));
        map = new HashMap<String, Integer>();
        map.put("ALL", 0);
        map.put("NEWS", 1);
        map.put("PAPER", 2);
        for (NewsFragment ff : save_fragments)
            fragments.add(ff);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();
        System.out.println(map);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        TabLayout layout = root.findViewById(R.id.tab_layout);
        ViewPager pager = root.findViewById(R.id.viewPager);
        fadatper = new Fadatper(getActivity().getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pager.setOffscreenPageLimit(2);
        pager.setAdapter(fadatper);
        layout.setupWithViewPager(pager);
        return root;
    }

    public class Fadatper extends FragmentStatePagerAdapter {

        public Fadatper(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            System.out.println(fragments.get(position).getTag());
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
            System.out.println(max_item);
            return titles.get(position);
        }

        public int getItemPosition(Object object){
            return PagerAdapter.POSITION_NONE;
        }
    }
}