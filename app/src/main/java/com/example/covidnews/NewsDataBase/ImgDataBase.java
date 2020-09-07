package com.example.covidnews.NewsDataBase;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.covidnews.MainActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//采用单例模式
public class ImgDataBase {
    private volatile static ImgDataBase dataBase = null;
    private DaoSession daoSession = null;
    private DaoMaster daoMaster = null;
    private ImgDao imgDao = null;
    private SQLiteDatabase db = null;
    private String DB_NAME = null;

    private static ExecutorService mPool = null;

    /*
        @params
            name : 数据库名字，如果拥有就是打开；如果没有就是创建
     */

    private ImgDataBase(String name){
        initGreenDao(name);
        DB_NAME = name;
        if(mPool == null){
            mPool = Executors.newFixedThreadPool(1);
        }
    }

    private void closeDaoSession(){
        if(null != daoSession){
            daoSession.clear();
            daoSession = null;
        }
    }

    private void closeHelper(){
        if(null != db){
            db.close();
        }
    }

    private void initGreenDao(String name){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(MainActivity.getMainActivity(), name);
        SQLiteDatabase db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        imgDao = daoSession.getImgDao();
    }

    //双重校验机制
    //单例模式，通常情况下，name是没有作用的
    public static ImgDataBase getDataBase(String name){
        if(dataBase == null){
            synchronized (ImgDataBase.class){
                if(dataBase == null){
                    dataBase = new ImgDataBase(name);
                }
            }
        }
        return dataBase;
    }

    //其实没啥用
    public synchronized DaoSession getDaoSession(){
        if(daoSession == null)
            daoSession = daoMaster.newSession();
        return daoSession;
    }

    //结束时关闭
    public synchronized void closeDataBase(){
        closeHelper();
        closeDaoSession();
    }

    //保存一条图片info到数据库
    private synchronized void saveOneDataPRI(Img img){
        imgDao.insertOrReplace(img);
    }

    //保存多条新闻到数据库
    private synchronized void saveDataPRI(ArrayList<Img> imgs){
        int size = imgs.size();
        Log.d("REPLACE:", "" + size);
        imgDao.insertOrReplaceInTx(imgs);
        imgs.clear();
    }

    public void saveData(final ArrayList<Img> imgs){
        mPool.execute(new Runnable() {
            @Override
            public void run() {
                saveDataPRI(imgs);
            }
        });
    }

    public void saveOneData(Img imgs){
        mPool.execute(new DataBaseSaver(imgs));
    }

    private class DataBaseSaver implements Runnable{
        private Img imgs;
        @Override
        public void run() {
            saveOneDataPRI(imgs);
        }

        public DataBaseSaver(Img n){
            imgs = n;
        }
    }

    //删除一条新闻
    private synchronized void deleteOneDataPRI(String id){
        imgDao.deleteByKey(id);
    }

    public void deleteOneData(Img img){
        String id = img.getUrl();
        mPool.execute(new DataDeleter(id));
    }

    public void deleteOneData(String id){
        mPool.execute(new DataDeleter(id));
    }

    private class DataDeleter implements Runnable{
        @Override
        public void run() {
            deleteOneDataPRI(id);
        }
        String id = null;
        public DataDeleter(String id){
            this.id = id;
        }
    }

    //得到数据库中的所有新闻资料
    public synchronized ArrayList<Img> getAll(){
        return (ArrayList<Img>) imgDao.loadAll();
    }

    //根据id查询一条新闻
    public synchronized Img getOneData(String id){
        Img imgs = imgDao.load(id);
        return imgs;
    }

    //查询有多少数据
    public long getCount(){
        return imgDao.count();
    }

    //更新时间戳
    public void uploadTFlag(String id, long tflag){
        //Log.d("WHATUPLOAD:", id);
        Img imgs = imgDao.load(id);
        if(imgs != null)
            imgs.setTflag(tflag);
        else{
            imgs = new Img();
            imgs.setUrl(id);
            imgs.setTflag(tflag);
        }
        imgDao.insertOrReplace(imgs);
    }

    public synchronized void DeleteByTflag(){
        long tflag = System.currentTimeMillis();
        tflag = tflag - 600000;
        ArrayList<Img> newsArrayList = (ArrayList<Img>) imgDao.queryBuilder().where(ImgDao.Properties.Tflag.le(tflag)).orderAsc(ImgDao.Properties.Tflag).list();
        int size = newsArrayList.size();
        for(int i = 0;i <= size - 1; i ++){
            Img news = newsArrayList.get(i);
            //删除文件，同时从数据库中移除
            String file = news.getPath();
            if(file != null) {
                File f = new File(file);
                f.delete();
                imgDao.delete(news);
            }
        }
    }
}
