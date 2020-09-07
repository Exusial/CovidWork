package com.example.covidnews.ui.expert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.example.covidnews.NetParser.ImageLoader;
import com.example.covidnews.R;
import com.example.covidnews.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ExpertDetailActivity extends AppCompatActivity {

    RecyclerView myview;
    DetailAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_detail);
        Bundle bundle = getIntent().getExtras();
        Expert_detail now = (Expert_detail) bundle.getSerializable("item");
        //System.out.println(now);
        myview = findViewById(R.id.rview);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        ImageView iview = findViewById(R.id.de_avatar);
        ImageLoader loader = new ImageLoader(this);
        loader.display(iview,now.avatar);
        myview.setLayoutManager(manager);
        TextView name =  findViewById(R.id.Name);
        TextView position = findViewById(R.id.position);
        TextView inst = findViewById(R.id.inst);
        name.setText(now.name);
        if(now.professions!=null){
            position.setText(now.professions);
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
        for(int i=0;i<5;i++){
            ExpRootNode temp = null;
            if(i==0){
                if(now.description!=null){
                    String sdes = now.description.replaceAll("<br><br>","\n");
                    String ff = sdes.replaceAll("<br>","\n");
                    ExpDesNode des = new ExpDesNode(ff);
                    List<BaseNode> deslist = new ArrayList<>();
                    deslist.add(des);
                    temp = new ExpRootNode("Description",deslist);
                }
            }
            else if(i==1){
                if(now.experience!=null&&!now.experience.equals("")){
                    System.out.println(now.experience.toString());
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
                    ExpDesNode work = new ExpDesNode(now.edu.replaceAll("<br>",""));
                    Edulist.add(work);
                    temp = new ExpRootNode("Education Experience",Edulist);
                }
            }
            else if(i==2){
                if(now.tags!=null&&now.tags.size()>0){
                    List<BaseNode> frontlist = new ArrayList<>();
                    FrontNode ff = new FrontNode(now.tags);
                    frontlist.add(ff);
                    temp = new ExpRootNode("Research Fronts",frontlist);
                }
            }
            else if(i==4){
                if(now.emails!=null&&now.emails.size()!=0){
                    List<BaseNode> emaillist = new ArrayList<>();
                    Set<String> emails = new TreeSet<>();
                    for(String email:now.emails){
                        emails.add(email);
                    }
                    for(String email:emails){
                        emaillist.add(new EmailNode(email));
                    }
                    temp = new ExpRootNode("Contact Emails",emaillist);
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