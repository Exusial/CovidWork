package com.example.covidnews.globalviews;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.covidnews.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalActivity extends AppCompatActivity {

    public static HashMap<String,ArrayList<Integer>> kv;
    private static Handler handler;
    public static ArrayList<ListItem> data;
    public static RecyclerView myview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global);
        myview = (RecyclerView)findViewById(R.id.gview);
        kv = new HashMap<String,ArrayList<Integer>>();
        data = new ArrayList<ListItem>();
        handler = new SafeHandler(this);
        Thread thread = new Thread(){
            @Override
            public void run(){
                handler.sendMessage(get_data());
            }
        };
        thread.start();
        findViewById(R.id.fitem).setVisibility(View.GONE);
    }

    public Message get_data()  {
        Message msg = new Message();
        try {
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
                single_data fdata = JSON.toJavaObject(obj,single_data.class);
                ArrayList<Integer> ndata = null;
                if (fdata.getData().size() > 0) {
                    ndata = fdata.getData().get(fdata.getData().size() - 1);
                    kv.put(cc, ndata);
                }
            }
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

    private static class SafeHandler extends Handler{

        private WeakReference<Context> ref;

        SafeHandler(Context context){
            ref = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            HashMap<String,ArrayList<Integer>> map = new HashMap<String,ArrayList<Integer>>();
            for(Map.Entry<String,ArrayList<Integer>> entry:kv.entrySet()){
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
            GlobalActivity activity = (GlobalActivity)ref.get();
            if(activity!=null) {
                activity.findViewById(R.id.loading).setVisibility(View.GONE);
                activity.findViewById(R.id.fitem).setVisibility(View.VISIBLE);
                LinearLayoutManager manager = new LinearLayoutManager(activity);
                manager.setOrientation(LinearLayoutManager.VERTICAL);
                myview.setLayoutManager(manager);
                GlobalAdapter adapter = new GlobalAdapter(R.layout.global_item_layout, data);
                myview.setAdapter(adapter);
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
