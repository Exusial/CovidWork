package com.example.covidnews.virusviews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Map;


public class VirusDetailActivity extends Activity {

    private RecyclerView proview,relview;
    private PropertyAdapter padapter;
    private RelationAdapter radapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virus_detail);
        Intent intent = getIntent();
        relay temp = (relay) intent.getExtras().getSerializable("item");
        TextView des = findViewById(R.id.description);
        des.setText(temp.description);
        ArrayList<PropertyItem> Pdata = new ArrayList<PropertyItem>();
        ArrayList<RelationItem> Rdata = new ArrayList<RelationItem>();
        for(Map.Entry<String,String> entry:temp.properties.entrySet()){
            Pdata.add(new PropertyItem(entry.getKey(),entry.getValue()));
        }
        for(relation rel:temp.relations){
            Rdata.add(new RelationItem(rel.getRelation(),rel.isForward(),rel.getLabel()));
        }
        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        manager1.setOrientation(LinearLayoutManager.VERTICAL);
        LinearLayoutManager manager2 = new LinearLayoutManager(this);
        manager2.setOrientation(LinearLayoutManager.VERTICAL);
        padapter = new PropertyAdapter(R.layout.property_item_layout,Pdata);
        radapter = new RelationAdapter(R.layout.relation_item_layout,Rdata);
        proview = findViewById(R.id.pview);
        relview = findViewById(R.id.rview);
        proview.setLayoutManager(manager1);
        relview.setLayoutManager(manager2);
        proview.setAdapter(padapter);
        relview.setAdapter(radapter);
    }
}