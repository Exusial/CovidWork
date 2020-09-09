package com.example.covidnews.globalviews;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidnews.MainActivity;
import com.example.covidnews.MainViewModel;
import com.example.covidnews.R;
import com.example.covidnews.listviews.NewsFragment;
import com.example.covidnews.ui.notifications.KindFragment;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

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

public class GlobalFragment extends Fragment {

    public static Integer all[] = {0,0,0};
    public static HashMap<String,ArrayList<Integer>> map = null;
    public static ArrayList<ListItem> data;
    private static MainViewModel vm;
    private MainActivity mainActivity;

    public GlobalFragment(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(vm==null)
            vm = new ViewModelProvider(mainActivity).get(MainViewModel.class);
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_global, container, false);
        RecyclerView myview = root.findViewById(R.id.gview);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        myview.setLayoutManager(manager);
        GlobalAdapter adapter = new GlobalAdapter(R.layout.global_item_layout, vm.getData().getValue());
        Button btn_1 = root.findViewById(R.id.button1);
        final EditText text = root.findViewById(R.id.search_country);
        text.setImeOptions(EditorInfo.IME_ACTION_DONE);
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = text.getText().toString();
                ArrayList<Integer> target = map.get(query);
                if(target!=null) {
                    AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle(query).setMessage(
                            "感染人数  " + target.get(0) + "\n" + "死亡人数  " + target.get(1) + "\n" + "痊愈人数  " + target.get(2)
                    ).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).create();
                    dialog.setCancelable(true);
                    dialog.show();
                }
                else
                    Toast.makeText(getContext(),"没有找到国家，请输入国家英文名重试？",Toast.LENGTH_LONG).show();
            }
        });
        myview.setAdapter(adapter);
        return root;
    }

    public static void process_data(){
        if(map==null)
            map = new HashMap<>();
        if(data==null)
            data = new ArrayList<>();
        for(Map.Entry<String,ArrayList<Integer>> entry:vm.getKv().getValue().entrySet()){
            String[] countris = entry.getKey().split("\\|");
            if(countris.length>1)
                continue;
            String country = countris[0];
            ArrayList<Integer> nums = map.get(country);
            if(nums == null){
                nums = new ArrayList<Integer>();
                nums.add(entry.getValue().get(0));
                nums.add(entry.getValue().get(3));
                nums.add(entry.getValue().get(2));
            }
            else{
                nums.set(0,nums.get(0)+entry.getValue().get(0));
                nums.set(1,nums.get(1)+entry.getValue().get(3));
                nums.set(2,nums.get(2)+entry.getValue().get(2));
            }
            vm.getmap().getValue().put(country,nums);
        }
        List<Map.Entry<String,ArrayList<Integer>>> so = new ArrayList<Map.Entry<String,ArrayList<Integer>>>(vm.getmap().getValue().entrySet());
        Collections.sort(so, new Comparator<Map.Entry<String, ArrayList<Integer>>>() {
            @Override
            public int compare(Map.Entry<String, ArrayList<Integer>> stringArrayListEntry, Map.Entry<String, ArrayList<Integer>> t1) {
                return -stringArrayListEntry.getValue().get(0).compareTo(t1.getValue().get(0));
            }
        });
        for(Map.Entry<String,ArrayList<Integer>> entry:so) {
            ListItem temp = new ListItem(entry.getKey(), Integer.toString(entry.getValue().get(0)), Integer.toString(entry.getValue().get(1)), Integer.toString(entry.getValue().get(2)));
            vm.getData().getValue().add(temp);
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