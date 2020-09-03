package com.example.covidnews.provinceviews;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.covidnews.R;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

public class ProvinceActivity extends AppCompatActivity {

    public static HashMap<String,ArrayList<Integer>> kv;
    private static Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province);
        kv = new HashMap<String, ArrayList<Integer>>();
        kv.clear();
        ColumnChartView colview = (ColumnChartView)findViewById(R.id.col1);
        handler = new SafeHandler(this);
        Thread thread = new Thread(){
            public void run(){
                handler.sendMessage(get_data());
            }
        };
        thread.start();
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
                single_data fdata = JSON.toJavaObject(obj, single_data.class);
                ArrayList<Integer> ndata = null;
                if (fdata.getData().size() > 0) {
                    ndata = fdata.getData().get(fdata.getData().size() - 1);
                    String[] countris = cc.split("\\|");
                    if(countris.length!=2||!countris[0].equals("China"))
                        continue;
                    String province = countris[1];
                    ArrayList<Integer> nums = kv.get(province);
                    if(nums == null){
                        nums = new ArrayList<Integer>();
                        nums.add(ndata.get(0));
                        nums.add(ndata.get(3));
                        nums.add(ndata.get(2));
                    }
                    else{
                        nums.set(0,nums.get(0)+ndata.get(0));
                        nums.set(1,nums.get(1)+ndata.get(3));
                        nums.set(2,nums.get(2)+ndata.get(2));
                    }
                    kv.put(province,nums);
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

    private class SafeHandler extends Handler {

        private WeakReference<Context> ref;

        SafeHandler(Context context){
            ref = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            ProvinceActivity activity = (ProvinceActivity)ref.get();
            if(activity!=null) {
                activity.findViewById(R.id.loading).setVisibility(View.GONE);
                activity.findViewById(R.id.refreshLayout).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.explain).setVisibility(View.VISIBLE);
                RefreshLayout refreshlayout = (RefreshLayout)activity.findViewById(R.id.refreshLayout);
                refreshlayout.setEnableRefresh(false);
                refreshlayout.setEnableLoadMore(false);
                ColumnChartView colview = (ColumnChartView)findViewById(R.id.col1);
                int subcol = 3;
                List<Column> cols = new ArrayList<Column>();
                List<SubcolumnValue> subcols;
                ArrayList<String> names = new ArrayList<String>();
                float count = 0.f;
                ArrayList<Float> maps = new ArrayList<>();
                for(Map.Entry<String,ArrayList<Integer>> entry:kv.entrySet()){
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