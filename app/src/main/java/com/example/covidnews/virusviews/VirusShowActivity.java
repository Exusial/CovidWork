package com.example.covidnews.virusviews;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.covidnews.R;
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

public class VirusShowActivity extends AppCompatActivity {

    public static String name;
    public static HashMap<String,relay> datasets;
    public static ArrayList<String> names;
    private SafeHandler handler;
    public static RecyclerView myview;
    private static ItemAdapter adapter;
    private static LinearLayoutManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virus_show);
        Bundle bundle = getIntent().getExtras();
        name = (String) bundle.get("name");
        names = new ArrayList<String>();
        datasets = new HashMap<String,relay>();
        handler = new SafeHandler(this);
        myview = (RecyclerView)findViewById(R.id.gview);
        Thread thread = new Thread() {
            public void run() {
                handler.sendMessage(get_data());
            }
        };
        thread.start();
        View emp =getLayoutInflater().inflate(R.layout.loading_layout,null);
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        myview.setLayoutManager(manager);
        adapter = new ItemAdapter(R.layout.virus_item_layout, null);
        //adapter.setEmptyView(emp);
        myview.setAdapter(adapter);
    }

    public Message get_data() {
        Message msg = new Message();
        try {
            String target = "https://innovaapi.aminer.cn/covid/api/v1/pneumonia/entityquery" +"?entity="+name;
            URL url = new URL(target);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader cin = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            //connection.setDoOutput(true);
            StringBuffer all = new StringBuffer();
            String line;
            while ((line = cin.readLine()) != null) {
                all.append(line);
            }
            String tt = all.toString();
            Map<String, JSONArray> map = JSONObject.parseObject(tt, Map.class);
            JSONArray nested = map.get("data");
            for (int i = 0; i < nested.size(); i++) {
                JSONObject eobj = nested.getJSONObject(i);
                names.add(eobj.get("label").toString());
                AbsInfo temp = JSON.toJavaObject((JSONObject)eobj.get("abstractInfo"), AbsInfo.class);
                relay item = new relay();
                if (temp.getBaidu() == "") {
                    if (temp.getEnwiki() == "") {
                        item.description = temp.getZhwiki();
                    } else {
                        item.description = temp.getEnwiki();
                    }
                } else {
                    item.description = temp.getBaidu();
                }
                if (eobj.get("img") == null)
                    item.image = null;
                else
                    item.image = eobj.get("img").toString();
                covid wrappr = JSON.toJavaObject(temp.getCOVID(), covid.class);
                //System.out.println(wrappr.getProperties());
                item.properties = JSONObject.parseObject(wrappr.getProperties().toString(), new TypeReference<HashMap<String, String>>(){});
                for (int j = 0; j < wrappr.getRelations().size(); j++) {
                    item.relations.add(JSON.toJavaObject(wrappr.getRelations().getJSONObject(j), relation.class));
                }
                datasets.put(eobj.get("label").toString(),item);
            }
            if(names.size()!=0)
                msg.what = 1;
            else
                msg.what = 0;
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

        SafeHandler(Context context) {
            ref = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                VirusShowActivity activity = (VirusShowActivity) ref.get();
                if(activity!=null){
                    View view = getLayoutInflater().inflate(R.layout.nothing_layout,null);
                    adapter.setEmptyView(view);
                    adapter.notifyDataSetChanged();
                    //myview.setAdapter(adapter);
                }
                //System.out.println("Failed!");
            } else {
                //System.out.println("Success!");
                final VirusShowActivity activity = (VirusShowActivity)ref.get();
                if(activity!=null) {
                    //activity.findViewById(R.id.layout_emp).setVisibility(View.GONE);
                    adapter.setList(names);
                    adapter.notifyDataSetChanged();
                    adapter.addChildClickViewIds(R.id.virus_btn1);
                    adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                        @Override
                        public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                            String temp = (String) adapter.getData().get(position);
                            //System.out.println(">>??");
                            relay send = datasets.get(temp);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("item",send);
                            bundle.putString("name",temp);
                            System.out.println(send.image);
                            Intent intent = new Intent(activity,VirusDetailActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                    //myview.setAdapter(adapter);
                }
            }
        }
    }
}

class relay implements Serializable {
    public HashMap<String,String> properties;
    public String image;
    public ArrayList<relation> relations;
    public String description;

    relay(){
        relations = new ArrayList<relation>();
    }
}

class AbsInfo{
    private String enwiki;
    private String baidu;
    private String zhwiki;
    private JSONObject COVID;

    public String getEnwiki() {
        return enwiki;
    }

    public String getBaidu() {
        return baidu;
    }

    public JSONObject getCOVID() {
        return COVID;
    }

    public String getZhwiki() {
        return zhwiki;
    }

    public void setBaidu(String baidu) {
        this.baidu = baidu;
    }

    public void setCOVID(JSONObject COVID) {
        this.COVID = COVID;
    }

    public void setEnwiki(String enwiki) {
        this.enwiki = enwiki;
    }

    public void setZhwiki(String zhwiki) {
        this.zhwiki = zhwiki;
    }

}

class covid{
    private JSONObject properties;
    private JSONArray relations;

    public JSONArray getRelations() {
        return relations;
    }

    public JSONObject getProperties() {
        return properties;
    }

    public void setProperties(JSONObject properties) {
        this.properties = properties;
    }

    public void setRelations(JSONArray relations) {
        this.relations = relations;
    }
}

class relation implements Serializable{
    private String relation;
    private String url;
    private String label;
    private boolean forward;

    public boolean isForward() {
        return forward;
    }

    public String getLabel() {
        return label;
    }

    public String getUrl() {
        return url;
    }

    public void setForward(boolean forward) {
        this.forward = forward;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}