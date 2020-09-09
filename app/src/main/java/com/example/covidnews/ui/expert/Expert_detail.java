package com.example.covidnews.ui.expert;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

class Expert_row implements Serializable {
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

public class Expert_detail extends Expert_row implements Serializable {
    public String description;
    public ArrayList<String> emails;
    public String homepage;
    public String[] experience;
    public String edu;
    TreeMap<String,Float> tags;
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