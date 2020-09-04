package com.example.covidnews.expertview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.example.covidnews.R;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.*;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;

public class ExpertDetailActivity extends AppCompatActivity {

    RecyclerView myview;
    DetailAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_detail);
        Bundle bundle = getIntent().getExtras();
        Expert_detail now = (Expert_detail) bundle.getSerializable("item");
        System.out.println(now);
        myview = findViewById(R.id.rview);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        myview.setLayoutManager(manager);
        TextView name =  findViewById(R.id.Name);
        TextView position = findViewById(R.id.position);
        TextView inst = findViewById(R.id.inst);
        name.setText(now.name);
        ImageView img = findViewById(R.id.de_avatar);
        
        if(now.position!=null){
            position.setText(now.position);
        }
        else
            position.setVisibility(View.GONE);
        if(now.inst!=null){
            inst.setText(now.inst);
        }
        else
            inst.setVisibility(View.GONE);
        if(now.params.size()!=0)
            updata_data(now);
        else{
            LinearLayout chart = findViewById(R.id.chart);
            chart.setVisibility(View.GONE);
        }
        adapter = new DetailAdapter();
        ArrayList<ExpRootNode> list = new ArrayList<>();
        for(int i=0;i<4;i++){
            ExpRootNode temp = null;
            if(i==0){
                if(now.description!=null){
                    ExpDesNode des = new ExpDesNode(now.description.replaceAll("<br><br>","\n"));
                    List<BaseNode> deslist = new ArrayList<>();
                    deslist.add(des);
                    temp = new ExpRootNode("Description",deslist);
                }
            }
            else if(i==1){
                if(now.experience!=null){
                    List<BaseNode> worklist = new ArrayList<>();
                    for(String place:now.experience){
                        ExpDesNode work = new ExpDesNode(place);
                        worklist.add(work);
                    }
                    temp = new ExpRootNode("Work Experience",worklist);
                }
            }
            else if(i==3){
                if(now.edu!=null){
                    List<BaseNode> Edulist = new ArrayList<>();
                    ExpDesNode work = new ExpDesNode(now.edu);
                    Edulist.add(work);
                    temp = new ExpRootNode("Education Experience",Edulist);
                }
            }
            else if(i==2){
                if(now.tags!=null){
                    List<BaseNode> frontlist = new ArrayList<>();
                    FrontNode ff = new FrontNode(now.tags);
                    frontlist.add(ff);
                    temp = new ExpRootNode("Research Fronts",frontlist);
                }
            }
            if(temp!=null)
                list.add(temp);
        }
        adapter.setList(list);
        myview.setAdapter(adapter);
    }

    private void updata_data(Expert_detail now){
        TextView temp = (TextView)findViewById(R.id.H_number);
        temp.setText(now.params.get("hindex").toString());
        temp = findViewById(R.id.A_number);
        temp.setText(now.params.get("activity").toString());
        temp = findViewById(R.id.S_number);
        temp.setText(now.params.get("sociability").toString());
        temp = findViewById(R.id.C_number);
        temp.setText(now.params.get("citations").toString());
        temp = findViewById(R.id.P_number);
        temp.setText(now.params.get("pubs").toString());
    }

    private void generate_graph(Expert_detail now){
        ColumnChartView colview = findViewById(R.id.col1);
        int subcol = 1;
        List<Column> cols = new ArrayList<Column>();
        List<SubcolumnValue> subcols;
        ArrayList<String> names = new ArrayList<String>();
        float count = 0.f;
        ArrayList<Float> maps = new ArrayList<>();
        for(Map.Entry<String,Integer> entry:now.tags.entrySet()){
            subcols = new ArrayList<SubcolumnValue>();
            maps.add(count);
            count++;
            names.add(entry.getKey());
            subcols.add(new SubcolumnValue(entry.getValue(),Color.parseColor("#6495ED")));
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
        Y.setName("");
        data.setAxisXBottom(X);
        data.setAxisYLeft(Y);
        colview.setColumnChartData(data);
        Viewport viewport = new Viewport(1,colview.getMaximumViewport().height()*1.25f,names.size()>5?5:names.size(),0);
        colview.setCurrentViewport(viewport);
        colview.moveTo(1,0);
    }
}