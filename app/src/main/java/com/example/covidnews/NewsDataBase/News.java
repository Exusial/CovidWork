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
    private String source;                  //代表来源
    private String language;                //代表语言
    private String tflag;                   //记录时间戳
    private String content;                 //记录内容，在点击进入后进行
    private String imgurls;                 //记录图的url,再点击后进行

    @Generated(hash = 1175507491)
    public News(String id, String type, String title, String time, String source,
            String language, String tflag, String content, String imgurls) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.time = time;
        this.source = source;
        this.language = language;
        this.tflag = tflag;
        this.content = content;
        this.imgurls = imgurls;
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

    public String getSource() {
        return this.source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public String getTflag() {
        return this.tflag;
    }
    public void setTflag(String tflag) {
        this.tflag = tflag;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getImgurls() {
        return this.imgurls;
    }
    public void setImgurls(String imgurls) {
        this.imgurls = imgurls;
    }
}
