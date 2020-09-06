package com.example.covidnews.listviews;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.example.covidnews.MainActivity;
import com.example.covidnews.NetParser.ImageLoader;
import com.example.covidnews.R;
import com.example.covidnews.expertview.DetailAdapter;
import org.w3c.dom.ls.LSException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import dalvik.system.DelegateLastClassLoader;

public class KindActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kind);
        final Button k_btn1 = findViewById(R.id.k_btn1);
        final Button k_btn2 = findViewById(R.id.k_btn2);
        final Button k_btn3 = findViewById(R.id.k_btn3);
        final Button k_btn4 = findViewById(R.id.k_btn4);
        final Button k_btn5 = findViewById(R.id.k_btn5);
        final Button k_btn6 = findViewById(R.id.k_btn6);
        if(MainActivity.all[0]==0)
            k_btn4.setVisibility(View.GONE);
        else
            k_btn1.setVisibility(View.GONE);
        if(MainActivity.all[1]==0)
            k_btn5.setVisibility(View.GONE);
        else
            k_btn2.setVisibility(View.GONE);
        if(MainActivity.all[2]==0)
            k_btn6.setVisibility(View.GONE);
        else
            k_btn3.setVisibility(View.GONE);
        k_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.max_item==1)
                    return;
                k_btn4.setVisibility(View.VISIBLE);
                k_btn1.setVisibility(View.GONE);
                MainActivity.max_item -= 1;
                MainActivity.all[0] = 1;
                int index = MainActivity.map.get("ALL");
                if(MainActivity.map.get("NEWS")!=null&&MainActivity.map.get("NEWS")>index)
                    MainActivity.map.put("NEWS",MainActivity.map.get("NEWS")-1);
                if(MainActivity.map.get("PAPER")!=null&&MainActivity.map.get("PAPER")>index)
                    MainActivity.map.put("PAPER",MainActivity.map.get("PAPER")-1);
                MainActivity.fragments.remove(index);
                MainActivity.titles.remove(index);
                MainActivity.map.put("ALL",null);
                MainActivity.fadatper.notifyDataSetChanged();
            }
        });
        k_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.max_item==1)
                    return;
                k_btn5.setVisibility(View.VISIBLE);
                k_btn2.setVisibility(View.GONE);
                MainActivity.max_item -= 1;
                MainActivity.all[1] = 1;
                int index = MainActivity.map.get("NEWS");
                if(MainActivity.map.get("PAPER")!=null&&MainActivity.map.get("PAPER")>index)
                    MainActivity.map.put("PAPER",MainActivity.map.get("PAPER")-1);
                if(MainActivity.map.get("ALL")!=null&&MainActivity.map.get("ALL")>index)
                    MainActivity.map.put("ALL",MainActivity.map.get("ALL")-1);
                MainActivity.fragments.remove(index);
                MainActivity.titles.remove(index);
                MainActivity.map.put("NEWS",null);
                MainActivity.fadatper.notifyDataSetChanged();
            }
        });
        k_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.max_item==1)
                    return;
                k_btn6.setVisibility(View.VISIBLE);
                k_btn3.setVisibility(View.GONE);
                MainActivity.max_item -= 1;
                MainActivity.all[2] = 1;
                int index = MainActivity.map.get("PAPER");
                if(MainActivity.map.get("ALL")!=null&&MainActivity.map.get("ALL")>index)
                    MainActivity.map.put("ALL",MainActivity.map.get("ALL")-1);
                if(MainActivity.map.get("NEWS")!=null&&MainActivity.map.get("NEWS")>index)
                    MainActivity.map.put("NEWS",MainActivity.map.get("NEWS")-1);
                MainActivity.fragments.remove(index);
                MainActivity.titles.remove(index);
                MainActivity.map.put("PAPER",null);
                MainActivity.fadatper.notifyDataSetChanged();
            }
        });
        k_btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                k_btn1.setVisibility(View.VISIBLE);
                k_btn4.setVisibility(View.GONE);
                MainActivity.max_item += 1;
                MainActivity.all[0] = 0;
                MainActivity.fragments.add(MainActivity.save_fragments.get(0));
                MainActivity.titles.add("ALL");
                MainActivity.map.put("ALL",MainActivity.fragments.size()-1);
                MainActivity.fadatper.notifyDataSetChanged();
            }
        });
        k_btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                k_btn2.setVisibility(View.VISIBLE);
                k_btn5.setVisibility(View.GONE);
                MainActivity.max_item += 1;
                MainActivity.all[1] = 0;
                MainActivity.fragments.add(MainActivity.save_fragments.get(1));
                MainActivity.titles.add("NEWS");
                MainActivity.map.put("NEWS",MainActivity.fragments.size()-1);
                MainActivity.fadatper.notifyDataSetChanged();
            }
        });
        k_btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                k_btn3.setVisibility(View.VISIBLE);
                k_btn6.setVisibility(View.GONE);
                MainActivity.max_item += 1;
                MainActivity.all[2] = 0;
                MainActivity.fragments.add(MainActivity.save_fragments.get(2));
                MainActivity.titles.add("PAPER");
                MainActivity.map.put("PAPER",MainActivity.fragments.size()-1);
                MainActivity.fadatper.notifyDataSetChanged();
            }
        });
    }
}