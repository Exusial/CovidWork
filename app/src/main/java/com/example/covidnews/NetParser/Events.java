package com.example.covidnews.NetParser;

import android.util.Log;

import java.util.ArrayList;

public class Events {
    String tflag;
    ArrayList<Data> datas = new ArrayList<>();

    public String getTflag() {
        return tflag;
    }

    public ArrayList<Data> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<Data> datas) {
        this.datas = datas;
    }

    public void setTflag(String tflag) {
        this.tflag = tflag;
    }

    public void show(){
        Log.d("SHOW ONE TIME:", "=============");
        Log.d("tflag:",tflag + "\n");
        for(int i = 0; i <= datas.size() - 1; i ++) {
            datas.get(i).show();
        }
    }
}

class Data{
    String _id;
    String type;
    String title;
    String category;
    String time;
    String lang;
    ArrayList<Geo> geoInfo = new ArrayList<>();
    String influence;

    public void show(){
        Log.d("_id:", _id + "\n");
        Log.d("type:", type + "\n");
        Log.d("title:", title + "\n");
        Log.d("category:", category + "\n");
        Log.d("time:", time + "\n");
        Log.d("lang:", lang + "\n");
        for(int i = 0; i <= geoInfo.size() - 1; i++){
            geoInfo.get(i).show();
        }
        Log.d("influence:",influence + "\n");

    }

    public String get_id() {
        return _id;
    }

    public String getCategory() {
        return category;
    }

    public String getInfluence() {
        return influence;
    }

    public String getLang() {
        return lang;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public ArrayList<Geo> getGeoInfo() {
        return geoInfo;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setGeoInfo(ArrayList<Geo> geoInfo) {
        this.geoInfo = geoInfo;
    }

    public void setInfluence(String influence) {
        this.influence = influence;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

}


class Geo {
    String originText;
    String geoName;
    String latitude;
    String longitude;

    public void show() {
        Log.d("originText:", originText + "\n");
        Log.d("geoName:", geoName + "\n");
        Log.d("latitude:", latitude + "\n");
        Log.d("longitude:", longitude + "\n");
    }

    public String getOriginText() {
        return originText;
    }

    public void setOriginText(String originText) {
        this.originText = originText;
    }

    public String getGeoName() {
        return geoName;
    }

    public void setGeoName(String geoName) {
        this.geoName = geoName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}