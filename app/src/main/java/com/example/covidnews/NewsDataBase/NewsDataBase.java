package com.example.covidnews.NewsDataBase;

import android.database.sqlite.SQLiteDatabase;

import com.example.covidnews.MainActivity;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//采用单例模式
public class NewsDataBase {
    private volatile static NewsDataBase dataBase = null;
    private DaoSession daoSession = null;
    private DaoMaster daoMaster = null;
    private NewsDao newsDao = null;
    private SQLiteDatabase db = null;
    private String DB_NAME = null;

    private static ExecutorService mPool = null;

    /*
        @params
            name : 数据库名字，如果拥有就是打开；如果没有就是创建
     */

    private NewsDataBase(String name){
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
        newsDao = daoSession.getNewsDao();
    }

    //双重校验机制
    //单例模式，通常情况下，name是没有作用的
    public static NewsDataBase getDataBase(String name){
        if(dataBase == null){
            synchronized (NewsDataBase.class){
                if(dataBase == null){
                    dataBase = new NewsDataBase(name);
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

    //保存一条新闻到数据库
    private synchronized void saveOneDataPRI(News news){
        newsDao.insertOrReplace(news);
    }

    //保存多条新闻到数据库
    private synchronized void saveDataPRI(ArrayList<News> news){
        newsDao.insertOrReplaceInTx(news);
    }

    public void saveData(final ArrayList<News> news){
        mPool.execute(new Runnable() {
            @Override
            public void run() {
                saveDataPRI(news);
            }
        });
    }

    public void saveOneData(News news){
        mPool.execute(new DataBaseSaver(news));
    }

    private class DataBaseSaver implements Runnable{
        private News news;
        @Override
        public void run() {
            saveOneDataPRI(news);
        }

        public DataBaseSaver(News n){
            news = n;
        }
    }

    //删除一条新闻
    private synchronized void deleteOneDataPRI(String id){
        newsDao.deleteByKey(id);
    }

    public void deleteOneData(News news){
        String id = news.getId();
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
    public synchronized ArrayList<News> getAll(){
        return (ArrayList<News>) newsDao.loadAll();
    }

    //根据id查询一条新闻
    public synchronized News getOneData(String id){
        News news = newsDao.load(id);
        return news;
    }

    //查询有多少数据
    public long getCount(){
        return newsDao.count();
    }

    public int getCount(String type){
        switch (type) {
            case "paper":
            case "news":
                ArrayList<News> newsArrayList = (ArrayList<News>) newsDao.queryBuilder().where(NewsDao.Properties.Type.eq(type)).list();
                return newsArrayList.size();
            default:
                return (int)getCount();
        }
    }

    //条件查询——类别
    public ArrayList<News> GetTypes(String[] types, int limit, int offset){
        ArrayList<News> news = new ArrayList<>();
        if(types == null){
            return (ArrayList<News>) newsDao.queryBuilder().limit(limit).offset(offset).orderAsc(NewsDao.Properties.Time).list();
        }
        int count = types.length;
        switch(count) {
            case 1:
                news = (ArrayList<News>) newsDao.queryBuilder().where(NewsDao.Properties.Type.eq(types[0]))
                        .orderAsc(NewsDao.Properties.Time).limit(limit).offset(offset).list();
                break;
            default:
                news = (ArrayList<News>) newsDao.queryBuilder().limit(limit).offset(offset).orderAsc(NewsDao.Properties.Time).list();
                break;
        }
        return news;
    }

    //查询类别
    public ArrayList<News> GetTypes(ArrayList<String> types, int limit, int offset){
        ArrayList<News> news = new ArrayList<>();
        if(types == null)
            return (ArrayList<News>) newsDao.queryBuilder().limit(limit).offset(offset).orderAsc(NewsDao.Properties.Time).list();
        switch(types.size()) {
            case 1:
                news = (ArrayList<News>) newsDao.queryBuilder().where(NewsDao.Properties.Type.eq(types.get(0)))
                    .orderAsc(NewsDao.Properties.Time).limit(limit).offset(offset).list();
                break;
            default:
                news = (ArrayList<News>) newsDao.queryBuilder().limit(limit).offset(offset).orderAsc(NewsDao.Properties.Time).list();
                break;
        }
        return news;
    }

    public ArrayList<News> GetTypes(String types, int limit, int offset){
        ArrayList<News> news = new ArrayList<>();
        switch(types){
            case "paper":
            case "news":
                news = (ArrayList<News>) newsDao.queryBuilder().where(NewsDao.Properties.Type.eq(types)).limit(limit)
                        .offset(offset).orderDesc(NewsDao.Properties.Time).list();
                break;
            default:
                news = (ArrayList<News>) newsDao.queryBuilder().limit(limit).offset(offset).orderDesc(NewsDao.Properties.Time).list();
                break;

        }
        return news;
    }
}
