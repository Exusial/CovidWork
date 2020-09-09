package com.example.covidnews.ui.expert;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.example.covidnews.MainActivity;
import com.example.covidnews.MainViewModel;
import com.example.covidnews.R;
import com.example.covidnews.ui.notifications.KindFragment;
import com.google.android.material.tabs.TabLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MainExpertFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    static int max_item = 2;
    static Fadatper fadatper;
    static RecyclerView myview;
    static RowAdapter adapter;
    static ArrayList<Expert_detail> experts;
    static SafeHandler safeHandler;
    static ArrayList<ExpertFragment> fragments;
    static ArrayList<String> titles;
    private View root;
    MainActivity mainActivity;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static MainViewModel vm;

    public MainExpertFragment(MainActivity mainActivity) {
        // Required empty public constructor
        this.mainActivity = mainActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        safeHandler = new SafeHandler(getContext());
        titles = new ArrayList<>();
        titles.add("知疫学者");
        titles.add("追忆学者");
        fragments = new ArrayList<>();
        if(experts==null) {
            experts = new ArrayList<>();
            Thread thread = new Thread() {
                public void run() {
                    safeHandler.sendMessage(get_data());
                }
            };
            thread.start();
        }
        if(vm==null){
            vm = new ViewModelProvider(mainActivity).get(MainViewModel.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_main_expert, container, false);
        return root;
    }

    public Message get_data()  {
        Message msg = new Message();
        if(vm.getPassed_experts().getValue().size()!=0&&vm.getLive_experts().getValue().size()!=0)
        {
            msg.what = 1;
            return msg;
        }
        try {
            String target = "https://innovaapi.aminer.cn/predictor/api/v1/valhalla/highlight/get_ncov_expers_list?v=2";
            URL url = new URL(target);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader cin = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer all = new StringBuffer();
            String line;
            while ((line = cin.readLine()) != null) {
                all.append(line);
            }
            String tt = all.toString();
            Map<String, JSONArray> map = JSONObject.parseObject(tt, Map.class);
            JSONArray nested = map.get("data");
            for (int i = 0; i < nested.size(); i++) {
                Expert_detail temp = new Expert_detail();
                JSONObject eobj = nested.getJSONObject(i);
                temp.name = eobj.get("name_zh").toString();
                if(temp.name == null||temp.name.equals(""))
                    temp.name = eobj.get("name").toString();
                temp.avatar = eobj.get("avatar").toString();
                temp.params = JSON.toJavaObject((JSONObject) eobj.get("indices"),Map.class);
                JSONObject profile = (JSONObject) eobj.get("profile");
                temp.inst = profile.get("affiliation").toString();
                temp.description = profile.get("bio").toString();
                if(profile.get("edu")!=null)
                    temp.edu = profile.get("edu").toString();
                if(profile.get("email")!=null)
                    temp.emails.add(profile.get("email").toString());
                JSONArray eml = (JSONArray) profile.get("emails_u");
                if(eml!=null){
                    for(int j=0;j<eml.size();j++){
                        parse_email t = JSON.toJavaObject((JSONObject) eml.get(j), parse_email.class);
                        temp.emails.add(t.getAddress());
                    }
                }
                if(profile.get("position")!=null)
                    temp.professions = profile.get("position").toString();
                if(profile.get("work")!=null&&profile.get("work").toString().length()!=0)
                    temp.experience = profile.get("work").toString().split("\n");
                if(profile.get("homepage")!=null)
                    temp.homepage = profile.get("homepage").toString();
                ArrayList<String> tags = JSON.toJavaObject((JSONArray) eobj.get("tags"),ArrayList.class);
                ArrayList<Object> counts = JSON.toJavaObject((JSONArray) eobj.get("tags_score"),ArrayList.class);
                if(tags!=null) {
                    for (int j = 0; j < tags.size(); j++) {
                        temp.tags.put(tags.get(j), Float.parseFloat(counts.get(j).toString()));
                    }
                }
                temp.ispassed = (Boolean) eobj.get("is_passedaway");
                if(temp.ispassed){
                    vm.getLive_experts().getValue().add(temp);
                }
                else
                    vm.getPassed_experts().getValue().add(temp);
            }
            msg.what = 1;
            //System.out.println(vm.getPassed_experts().getValue().size());
            //System.out.println(vm.getLive_experts().getValue().size());
        } catch (MalformedURLException e) {
            System.out.println("MalForm");
            msg.what = 0;
        } catch (IOException e) {
            System.out.println("IO error!");
            msg.what = 0;
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
            if (msg.what == 0) {
                MainActivity activity = (MainActivity) ref.get();
                if(activity!=null){
                    View view = getLayoutInflater().inflate(R.layout.nothing_layout,null);
                    adapter.setEmptyView(view);
                    adapter.notifyDataSetChanged();
                }
            } else {
                final MainActivity activity = (MainActivity) ref.get();
                root.findViewById(R.id.set).setVisibility(View.GONE);
                if (activity != null) {
                    ExpertFragment kfragment = new ExpertFragment(0,mainActivity);
                    ExpertFragment pfragment = new ExpertFragment(1,mainActivity);
                    fragments.add(kfragment);
                    fragments.add(pfragment);
                    TabLayout layout = root.findViewById(R.id.tab_layout);
                    ViewPager pager = root.findViewById(R.id.viewPager);
                    fadatper = new Fadatper(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
                    pager.setOffscreenPageLimit(2);
                    pager.setAdapter(fadatper);
                    fadatper.notifyDataSetChanged();
                    layout.setupWithViewPager(pager);
                    kfragment.changes();
                    pfragment.changes();
                }
            }
        }
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
}



