package com.example.covidnews.kmeanview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class Event {
    private String _id;
    private LinkedHashMap<String, Integer> entities;
    private LinkedHashMap<String, Double> seg_text;
    private String title;
    private int total;

    public Event(){
        total = 0;
    }

    public void set_id(String _id){
        this._id = _id;
    }

    public void setSeg_text(LinkedHashMap<String, Double> seg_text) {
        this.seg_text = seg_text;
    }

    public void setEntities(LinkedHashMap<String, Integer> entities) {
        this.entities = entities;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String get_id() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public LinkedHashMap<String, Double> getSeg_text() {
        return seg_text;
    }

    public LinkedHashMap<String, Integer> getEntities() {
        return entities;
    }

    public int getTotal() {
        return total;
    }

    public void count(){
        total ++;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
