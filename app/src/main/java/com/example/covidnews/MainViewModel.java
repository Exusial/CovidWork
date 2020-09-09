package com.example.covidnews;
import android.view.View;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.covidnews.ui.expert.Expert_detail;

import java.util.ArrayList;
import java.util.HashMap;

public class MainViewModel extends ViewModel {
    private static MutableLiveData<HashMap<String, ArrayList<Integer>>> kv;
    private static MutableLiveData<HashMap<String,ArrayList<Integer>>> map;
    private static MutableLiveData<HashMap<String,ArrayList<Integer>>> prolist;
    private static MutableLiveData<ArrayList<Expert_detail>> live_experts;
    private static MutableLiveData<ArrayList<Expert_detail>> passed_experts;
    private static MutableLiveData<ArrayList> data;
    public MainViewModel() {
        kv = new MutableLiveData<>();
        kv.setValue(new HashMap<String, ArrayList<Integer>>());
        map  = new MutableLiveData<>();
        map.setValue(new HashMap<String, ArrayList<Integer>>());
        prolist = new MutableLiveData<>();
        prolist.setValue(new HashMap<String, ArrayList<Integer>>());
        live_experts = new MutableLiveData<>();
        live_experts.setValue(new ArrayList<Expert_detail>());
        passed_experts = new MutableLiveData<>();
        passed_experts.setValue(new ArrayList<Expert_detail>());
        data = new MutableLiveData<>();
        data.setValue(new ArrayList());
    }

    public static LiveData<HashMap<String, ArrayList<Integer>>> getKv() {
        return kv;
    }

    public static void setKv(MutableLiveData<HashMap<String, ArrayList<Integer>>> kv) {
        MainViewModel.kv = kv;
    }

    public static LiveData<HashMap<String, ArrayList<Integer>>> getmap() {
        return map;
    }

    public static LiveData<HashMap<String, ArrayList<Integer>>> getProlist() {
        return prolist;
    }

    public static void setMap(MutableLiveData<HashMap<String, ArrayList<Integer>>> map) {
        MainViewModel.map = map;
    }

    public static void setProlist(MutableLiveData<HashMap<String, ArrayList<Integer>>> prolist) {
        MainViewModel.prolist = prolist;
    }

    public static LiveData<ArrayList<Expert_detail>> getLive_experts() {
        return live_experts;
    }

    public static LiveData<ArrayList<Expert_detail>> getPassed_experts() {
        return passed_experts;
    }

    public static void setLive_experts(MutableLiveData<ArrayList<Expert_detail>> live_experts) {
        MainViewModel.live_experts = live_experts;
    }

    public static void setPassed_experts(MutableLiveData<ArrayList<Expert_detail>> passed_experts) {
        MainViewModel.passed_experts = passed_experts;
    }

    public static void setData(MutableLiveData<ArrayList> data) {
        MainViewModel.data = data;
    }


    public static MutableLiveData<ArrayList> getData() {
        return data;
    }
}
