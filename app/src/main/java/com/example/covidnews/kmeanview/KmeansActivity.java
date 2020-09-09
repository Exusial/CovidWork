package com.example.covidnews.kmeanview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.covidnews.R;
import com.example.covidnews.newsviews.NewsItemActivity;
import com.example.covidnews.provinceviews.ProvinceActivity;
import com.example.covidnews.ui.notifications.KindFragment;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.lang.ref.WeakReference;
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

public class KmeansActivity extends AppCompatActivity {

    private static HashMap<String,ArrayList<Event>> map;
    private ArrayList<String> titles;
    private SafeHandler handler;
    private KmeanAdapter adapter;
    private static RecyclerView myview;
    private static ArrayList<KRootNode> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kmeans);
        handler = new SafeHandler(this);
        Thread thread = new Thread(){
            public void run(){
                System.out.println(Thread.currentThread());
                handler.sendMessage(get_data());
            }
        };
        adapter = new KmeanAdapter();
        myview = findViewById(R.id.rview);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        myview.setLayoutManager(manager);
        map = new HashMap<>();
        if(list!=null)
            list.clear();
        list = new ArrayList<>();
        thread.start();
    }

    private HashMap<String,ArrayList<String>> get_map(){

        return new HashMap<>();
    }

    public Message get_data(){
        Message msg = new Message();
        KMeansController controller = new KMeansController(KmeansActivity.this);
        controller.explain();
        titles = controller.getClassLabel();
        ArrayList<ArrayList<Event>> events;
        events = controller.getEventClasses();
        for(int i=0;i<titles.size();i++){
            map.put(titles.get(i),events.get(i));
        }
        if(map.size()==0)
        {
            msg.what = 0;
        }
        else
            msg.what = 1;
        return msg;
    }

    public static HashMap<String, ArrayList<Event>> getmap() {
        return map;
    }

    private class SafeHandler extends Handler {

        private WeakReference<Context> ref;

        SafeHandler(Context context){
            ref = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            KmeansActivity activity = (KmeansActivity)ref.get();
            if(activity!=null){
                if(msg.what==0){
                    //....
                }
                else{
                    int num_kind = map.size();
                    HashMap<String,Integer> datamap = new HashMap<>();
                    for(Map.Entry<String,ArrayList<Event>> entry:map.entrySet()){
                        datamap.put(entry.getKey(),entry.getValue().size());
                    }
                    FrontNode gg = new FrontNode(datamap);
                    ArrayList<BaseNode> temp = new ArrayList<>();
                    temp.add(gg);
                    KRootNode gnode = new KRootNode("图表",temp);
                    list.add(gnode);
                    for(Map.Entry<String,ArrayList<Event>> entry:map.entrySet()){
                        KRootNode tnode = new KRootNode (entry.getKey(),null);
                        list.add(tnode);
                    }
                    adapter.setList(list);
                    adapter.notifyDataSetChanged();
                    myview.setAdapter(adapter);
                    adapter.addChildClickViewIds(R.id.text);

                    adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                        @Override
                        public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                            KRootNode node = (KRootNode) adapter.getItem(position);
                            String id = node.getTitle();
                            if(!id.equals("图表")){
                                Intent intent = new Intent(KmeansActivity.this, KItemActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("sets",id);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        }

                    });

                }


            }
        }
    }


}