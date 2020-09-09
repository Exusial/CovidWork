package com.example.covidnews.ui.notifications;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.covidnews.MainActivity;
import com.example.covidnews.MainViewModel;
import com.example.covidnews.R;
import com.example.covidnews.globalviews.GlobalFragment;
import com.example.covidnews.listviews.NewsFragment;
import com.example.covidnews.provinceviews.ProvinceActivity;
import com.example.covidnews.provinceviews.ProvinceFragment;
import com.example.covidnews.ui.home.HomeFragment;
import com.google.android.material.tabs.TabLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.WatchEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class KindFragment extends Fragment {

    public static int max_item = 2;
    public static ArrayList<Fragment> fragments;
    public static ArrayList<String> titles;
    public static Fadatper fadatper;
    static MainViewModel vm;
    public static boolean hasfinished = false;
    private static View root;
    private MainActivity mainActivity;

    public KindFragment(MainActivity mainActivity){
        this.mainActivity = mainActivity;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(vm == null)
            vm = new ViewModelProvider(mainActivity).get(MainViewModel.class);
        titles = new ArrayList<>();
        titles.add("全国疫情");
        titles.add("全球疫情");
        final SafeHandler handler = new SafeHandler(getActivity());
        Thread thread = new Thread(){
            public void run(){
                handler.sendMessage(get_data());
            }
        };
        thread.start();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_kind, container, false);
        return root;
    }



    public class Fadatper extends FragmentStatePagerAdapter {

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
            return titles.get(position);
        }

        public int getItemPosition(Object object){
            return PagerAdapter.POSITION_NONE;
        }
    }

    public Message get_data()  {
        Message msg = new Message();
        try {
            if(vm.getKv().getValue().size()==0)
                hasfinished = false;
            if(hasfinished) {
                msg.what = 1;
                return msg;
            }
            URL url = new URL("https://covid-dashboard.aminer.cn/api/dist/epidemic.json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader cin = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer all = new StringBuffer();
            String line;
            while ((line = cin.readLine()) != null) {
                all.append(line);
            }
            String tt = all.toString();
            Map<String, JSONObject> map = JSONObject.parseObject(tt, Map.class);
            for (String cc : map.keySet()) {
                JSONObject obj = map.get(cc);
                single_data fdata = JSON.toJavaObject(obj, single_data.class);
                ArrayList<Integer> ndata = null;
                if (fdata.getData().size() > 0) {
                    ndata = fdata.getData().get(fdata.getData().size() - 1);
                    vm.getKv().getValue().put(cc,ndata);
                }
            }
            hasfinished = true;
            msg.what = 1;
        }
        catch (MalformedURLException e){
            System.out.println("MalForm");
        }
        catch (IOException e){
            System.out.println("IO error!");
        }
        return msg;
    }

    private class SafeHandler extends Handler {

        private WeakReference<Context> ref;

        SafeHandler(Context context){
            ref = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                Toast.makeText(getActivity(),"网络不佳！",Toast.LENGTH_LONG).show();
            }
            else if(msg.what==1){
                root.findViewById(R.id.set).setVisibility(View.GONE);
                GlobalFragment gfragment = new GlobalFragment(mainActivity);
                ProvinceFragment pfragment = new ProvinceFragment(mainActivity);
                gfragment.process_data();
                pfragment.process_data();
                fragments = new ArrayList<>();
                fragments.add(pfragment);
                fragments.add(gfragment);
                TabLayout layout = root.findViewById(R.id.tab_layout);
                ViewPager pager = root.findViewById(R.id.viewPager);
                fadatper = new Fadatper(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
                pager.setOffscreenPageLimit(2);
                pager.setAdapter(fadatper);
                pager.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return true;
                    }
                });
                fadatper.notifyDataSetChanged();
                layout.setupWithViewPager(pager);
            }
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