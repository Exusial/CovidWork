package com.example.covidnews.virusviews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.example.covidnews.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class VirusDetailActivity extends Activity {
    private RecyclerView myview;
    private TreeAdapter adapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virus_detail);
        Intent intent = getIntent();
        relay temp = (relay) intent.getExtras().getSerializable("item");
        List<BaseNode> datasets = new ArrayList<>();
        RootNode n = null;
        for(int i=0;i<4;i++){
            List<BaseNode> seclist = new ArrayList<>();
            if(i==0){
                DesNode node = new DesNode(temp.description);
                seclist.add(node);
                n = new RootNode("Description",seclist);
            }
            else if(i==1){
                if(temp.image==null)
                    continue;;
                ImgNode node = new ImgNode(temp.image);
                seclist.add(node);
                n = new RootNode("Image",seclist);
            }
            else if(i==2){
                if(temp.properties.size()==0)
                    continue;
                for(Map.Entry<String,String> entry:temp.properties.entrySet()){
                    PropertyNode node = new PropertyNode(entry.getKey(),entry.getValue());
                    seclist.add(node);
                }
                n = new RootNode("Properties",seclist);
            }
            else{
                if(temp.relations.size()==0)
                    continue;
                for(relation entry:temp.relations){
                    RelationNode node = new RelationNode(entry.getRelation(),entry.isForward(),entry.getLabel());
                    seclist.add(node);
                }
                n = new RootNode("Properties",seclist);
            }
            datasets.add(n);
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        myview = findViewById(R.id.rview);
        TreeAdapter adapter = new TreeAdapter();
        adapter.setList(datasets);
        myview.setLayoutManager(manager);
        myview.setAdapter(adapter);
    }
}