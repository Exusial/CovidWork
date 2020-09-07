package com.example.covidnews.NewsDataBase;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class Img {
    @Id
    private String url;
    private String path;
    private long tflag;
    @Generated(hash = 1696369942)
    public Img(String url, String path, long tflag) {
        this.url = url;
        this.path = path;
        this.tflag = tflag;
    }
    @Generated(hash = 2004440555)
    public Img() {
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getPath() {
        return this.path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public long getTflag() {
        return this.tflag;
    }
    public void setTflag(long tflag) {
        this.tflag = tflag;
    }
}
