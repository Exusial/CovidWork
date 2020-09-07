package com.example.covidnews.virusviews;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.covidnews.R;
import com.example.covidnews.VirusSearchActivity;
import com.example.covidnews.listviews.NewsFragment;
import com.example.covidnews.ui.notifications.KindFragment;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

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

public class VirusFragment extends Fragment {

    public static Integer all[] = {0,0,0};
    public static HashMap<String,ArrayList<Integer>> prolist = null;
    public static View root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_virus_search, container, false);
        Button btn1 = (Button)root.findViewById(R.id.m_btn1);
        LinearLayout layout = (LinearLayout)root.findViewById(R.id.mainlay);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), VirusShowActivity.class);
                Bundle bundle = new Bundle();
                EditText item = (EditText)root.findViewById(R.id.items);
                bundle.putString("name",item.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return root;
    }
}
