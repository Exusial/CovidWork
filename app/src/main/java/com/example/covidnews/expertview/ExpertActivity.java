package com.example.covidnews.expertview;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.covidnews.R;
import com.example.covidnews.virusviews.VirusShowActivity;
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

class Expert_row implements Serializable{
    public String name;
    public String professions;
    public Map<String,Object> params;
    public String inst;
    public boolean ispassed;
    public String avatar;
    public String position;
    Expert_row(String name,String professions,Map<String,Object> params,String inst){
        this.name = name;
        this.params = params;
        this.professions = professions;
        this.inst = inst;
        avatar = null;
        ispassed = false;
        params = new HashMap<>();
    }

    Expert_row(){
        params = new HashMap<>();
    }
}

class Expert_detail extends Expert_row implements Serializable {
    public String description;
    public ArrayList<String> emails;
    public String homepage;
    public String[] experience;
    public String edu;
    TreeMap<String,Integer> tags;
    Expert_detail(String name,String professions,Map<String,Object> params,String inst){
        super(name,professions,params,inst);
        emails = new ArrayList<>();
        tags = new TreeMap<>();
    }

    Expert_detail(){
        super();
        emails = new ArrayList<>();
        tags = new TreeMap<>();
    };

    public String toString(){
        return name+" "+position+" "+inst;
    }
}

public class ExpertActivity extends AppCompatActivity {

    static RecyclerView myview;
    static RowAdapter adapter;
    static ArrayList<Expert_detail> experts;
    static SafeHandler safeHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        experts = new ArrayList<>();
        adapter = new RowAdapter(R.layout.row_expert_layout,null);
        adapter.notifyDataSetChanged();
        myview = (RecyclerView)findViewById(R.id.rview);
        myview.setLayoutManager(manager);
        myview.setAdapter(adapter);
        View view = getLayoutInflater().inflate(R.layout.loading_layout,null);
        adapter.setEmptyView(view);
        safeHandler = new SafeHandler(this);
        Thread thread = new Thread(){
            public void run(){
                safeHandler.sendMessage(get_data());
            }
        };
        thread.start();
    }

    public Message get_data()  {
        Message msg = new Message();
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
            System.out.println(nested.size());
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
                        parse_email t = JSON.toJavaObject((JSONObject) eml.get(j),parse_email.class);
                        temp.emails.add(t.getAddress());
                    }
                }
                if(profile.get("position")!=null)
                    temp.professions = profile.get("position").toString();
                if(profile.get("work")!=null)
                    temp.experience = profile.get("work").toString().split("\n");
                if(profile.get("homepage")!=null)
                    temp.homepage = profile.get("homepage").toString();
                ArrayList<String> tags = JSON.toJavaObject((JSONArray) eobj.get("tags"),ArrayList.class);
                ArrayList<Integer> counts = JSON.toJavaObject((JSONArray) eobj.get("tags_score"),ArrayList.class);
                if(tags!=null) {
                    for (int j = 0; j < tags.size(); j++) {
                        temp.tags.put(tags.get(j), counts.get(j));
                    }
                }
                experts.add(temp);
            }
            if(experts.size()!=0)
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

        SafeHandler(Context context){
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
                final ExpertActivity activity = (ExpertActivity)ref.get();
                if(activity!=null) {
                    //activity.findViewById(R.id.layout_emp).setVisibility(View.GONE);
                    adapter.setList(experts);
                    adapter.notifyDataSetChanged();
                    adapter.addChildClickViewIds(R.id.ntitle1);
                    adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                        @Override
                        public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                            Bundle bundle = new Bundle();
                            Expert_detail to_send = (Expert_detail) adapter.getData().get(position);
                            bundle.putSerializable("item",to_send);
                            System.out.println(to_send);
                            Intent intent = new Intent(activity, ExpertDetailActivity.class);
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

class parse_email{
    private String address;
    private String src;
    private Double weight;

    public Double getWeight() {
        return weight;
    }

    public String getAddress() {
        return address;
    }

    public String getSrc() {
        return src;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}