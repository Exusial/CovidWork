package com.example.covidnews.NewsDataBase;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.ArrayList;

@Entity
public class News {
    //info
    @Id
    private String id;                      //保存唯一编号event_id
    private String type;                    //类别，新闻或者paper
    private String title;                   //新闻标题
    private String time;                    //代表时间
    private String language;                //语种
    private double influence;               //影响力
    private String imgUrl;                  //图片的url

    //具体的新闻信息
    private String content;                 //正文
    /*
        此条较长
        格式为
        来源, 时间 -- 正文
        其中正文每一个自然段用','隔开
        即： 自然段1,自然段2,自然段3
    */
    private String entities;              //关键词集合
    @Generated(hash = 2039345805)
    public News(String id, String type, String title, String time, String language,
            double influence, String imgUrl, String content, String entities) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.time = time;
        this.language = language;
        this.influence = influence;
        this.imgUrl = imgUrl;
        this.content = content;
        this.entities = entities;
    }
    @Generated(hash = 1579685679)
    public News() {
    }

    private String ListToString(ArrayList<String> strings){
        if(strings == null)
            return null;
        StringBuilder sb = new StringBuilder();
        for(String string : strings){
            sb.append(string);
            sb.append(',');
        }
        return sb.toString();
    }

    private ArrayList<String> StringToList(String str){
        if(str == null)
            return null;
        ArrayList<String> strings = new ArrayList<>();
        String[] strs = str.split(",");
        for(String st : strs){
            strings.add(st);
        }
        return strings;
    }

    private String ListToString(String[] strings){
        if(strings == null)
            return null;
        StringBuilder sb = new StringBuilder();
        for(String string : strings){
            sb.append(string);
            sb.append(',');
        }
        return sb.toString();
    }

    //关于id
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //关于种类
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    //关于文章标题
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //关于时间
    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    //关于语种
    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    //关于影响力
    public double getInfluence() {
        return this.influence;
    }

    public void setInfluence(double influence) {
        this.influence = influence;
    }

    //关于正文内容
    public String getContent() {
        return this.content;
    }

    public ArrayList<String> getContentList(){
        return StringToList(this.content);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setContent(String[] content){
        this.content = ListToString(content);
    }

    public void setContent(ArrayList<String> content){
        this.content = ListToString(content);
    }

    //关于关键词
    public String getEntities() {
        return this.entities;
    }

    public ArrayList<String> getEntitiesList(){
        return StringToList(this.entities);
    }

    public void setEntities(String entities) {
        this.entities = entities;
    }

    public void setEntities(String[] entities){
        this.entities = ListToString(entities);
    }

    public void setEntities(ArrayList<String> entities){
        this.entities = ListToString(entities);
    }
    public String getImgUrl() {
        return this.imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}