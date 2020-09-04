package com.example.covidnews.NetParser;

import java.util.ArrayList;

public class NData {
    String _id;
    String category;
    String content;
    String date;
    ArrayList<Entity> entities;
    ArrayList<NewsGeo> geoInfo;
    String id;
    String influence;
    String lang;
    ArrayList<RelatedEvent> related_events;
    String seg_text;
    String source;
    String tflag;
    String time;
    String title;
    String type;
    ArrayList<String> urls;

    public long getSize(){
        long size = 12;

        return size;
    }

    public String getTflag() {
        return tflag;
    }

    public String get_id(){
        return _id;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getLang() {
        return lang;
    }

    public String getInfluence() {
        return influence;
    }

    public String getCategory() {
        return category;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public String getSeg_text() {
        return seg_text;
    }

    public String getSource() {
        return source;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public ArrayList<String> getUrls() {
        return urls;
    }

    public ArrayList<RelatedEvent> getRelated_events() {
        return related_events;
    }

    public void setTflag(String tflag) {
        this.tflag = tflag;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setInfluence(String influence) {
        this.influence = influence;
    }

    public void setGeoInfo(ArrayList<NewsGeo> geoInfo) {
        this.geoInfo = geoInfo;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRelated_events(ArrayList<RelatedEvent> related_events) {
        this.related_events = related_events;
    }

    public void setSeg_text(String seg_text) {
        this.seg_text = seg_text;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setUrls(ArrayList<String> urls) {
        this.urls = urls;
    }
}


class Entity{
    String label;
    String url;

    public String getLabel() {
        return label;
    }

    public String getUrl() {
        return url;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

class NewsGeo{
    String geoName;
    String latitude;
    String longitude;
    String originText;

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getOriginText() {
        return originText;
    }

    public String getGeoName() {
        return geoName;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setGeoName(String geoName) {
        this.geoName = geoName;
    }

    public void setOriginText(String originText) {
        this.originText = originText;
    }
}

class RelatedEvent{
    private String id;
    private String score;

    public void setId(String id) {
        this.id = id;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public String getScore() {
        return score;
    }
}