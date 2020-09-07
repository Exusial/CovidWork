package com.example.covidnews.NetParser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.RequiresApi;

import com.example.covidnews.MainActivity;
import com.example.covidnews.NewsDataBase.Img;
import com.example.covidnews.NewsDataBase.ImgDataBase;
import com.example.covidnews.NewsDataBase.News;
import com.example.covidnews.NewsDataBase.NewsDataBase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 三级缓存的图片工具
 * 用来对图片进行加载，方式为
 * 在内存中寻找   -（找到了）->展示
 *              -（没找到）->从本地缓存中获取  -（找到了）->显示
 *                                         -（没找到）->从网络中获取
 */

public class ImageLoader {
    private static ImageLoader imageLoader;

    //定义一个缓存空间
    private static LruCache<String, Bitmap> imageCaches;

    //定义上下文对象
    private Context mContext;

    private static Handler mHandler;

    private static ExecutorService mPool;

    private Map<ImageView, String> mTags = new LinkedHashMap<>();


    private ImageLoader(Context context){
        this.mContext = context;
        if(imageCaches == null){
            //申请一个内存空间
            int maxSize = (int)(Runtime.getRuntime().freeMemory() / 8);
            //实例化
            imageCaches = new LruCache<String, Bitmap>(maxSize){
                @Override
                protected int sizeOf(String key, Bitmap value){
                    return value.getRowBytes() * value.getHeight();
                }
            };
        }
        //实例化我的Handler
        if(mHandler == null){
            //实例化Handler来处理图片放入
            mHandler = new Handler();
        }
        if(mPool == null){
            //创建一个固定大小的线程池
            mPool = Executors.newFixedThreadPool(5);
        }
    }

    public static ImageLoader getInstance(){
        if(imageLoader == null){
            synchronized (ImageLoader.class){
                if(imageLoader == null){
                    imageLoader = new ImageLoader(MainActivity.getMainActivity());
                }
            }
        }
        return imageLoader;
    }

    //放缩
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void ScaleImage(ImageView iv, Bitmap bp){
        int width = bp.getWidth();
        int height = bp.getHeight();
        int ivWidth = iv.getWidth();
        int ivHeight = iv.getHeight();

        ivWidth = ivWidth > 0? ivWidth : 480;
        ivHeight = ivHeight > 0? ivHeight : 360;

        double scale = 1.0;
        double dx = (double)width/ivWidth;
        double dy = (double)height/ivHeight;

        if(dx > dy && dy >= 1){
            scale = dx;
        }
        if(dx < dy && dx >= 1){
            scale = dy;
        }

        int newWidth = (int)(double)(width / scale);
        int newHeight = (int)(double)(height / scale);

        bp = Bitmap.createScaledBitmap(bp,newWidth,newHeight,true);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Bitmap display(ImageView iv, String url){
        //现在内存中查找
        Bitmap bitmap = imageCaches.get(url);
        if(bitmap != null) {
            //内存中有，显示图片即可
            ScaleImage(iv, bitmap);
            Bitmap finalBitmap1 = bitmap;
            long date = System.currentTimeMillis();
            ImgDataBase db = ImgDataBase.getDataBase("ImgTest.db");
            db.uploadTFlag(url, date);
            return finalBitmap1;

        }
        //如果内存中没有，需要在本地获取
        bitmap = loadFromLocal(url);
        if(bitmap != null){
            ScaleImage(iv, bitmap);
            Bitmap finalBitmap = bitmap;
            long date = System.currentTimeMillis();
            ImgDataBase db = ImgDataBase.getDataBase("ImgTest.db");
            db.uploadTFlag(url, date);
            return finalBitmap;
        }
        return null;
    }

    public Bitmap GetFromNet(ImageView iv, String url){
        return loadFromNet(iv, url);
    }

    private Bitmap loadFromNet(ImageView iv, String url){
        //从网络中获得图片
        try{
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            //连接服务器超时
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            //连接
            connection.connect();

            //获取流
            InputStream is = connection.getInputStream();

            //将流变成bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            //获取宽高

            int height = bitmap.getHeight();
            int width = bitmap.getWidth();

            int ivWidth = iv.getWidth();
            int ivHeight = iv.getHeight();

            ivWidth = ivWidth > 0? ivWidth : 480;
            ivHeight = ivHeight > 0? ivHeight : 360;

            int dx = width/ivWidth;
            int dy = height/ivHeight;

            int scale = 1;

            if(dx > dy && dy >= 1){
                scale = dx;
            }
            if(dy > dx && dx >= 1){
                scale = dy;
            }

            int newWidth = width / scale;
            int newHeight = height / scale;

            bitmap = Bitmap.createScaledBitmap(bitmap,newWidth,newHeight,true);

            //存储到本地
            saveToLocal(bitmap , url);

            //存储到内存
            imageCaches.put(url, bitmap);

            return bitmap;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveToLocal(Bitmap bitmap, String url) throws FileNotFoundException {
        File file = getCacheFile(url);
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);

        long date = System.currentTimeMillis();

        ImgDataBase db = ImgDataBase.getDataBase("ImgTest.db");

        Img imgs = new Img();
        imgs.setTflag(date);
        imgs.setPath(file.toString());
        imgs.setUrl(url);
        db.saveOneData(imgs);
    }

    //从本地获取图片信息
    private Bitmap loadFromLocal(String url){
        //从本地得到存储路径
        File file = getCacheFile(url);
        if(file.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            if(bitmap == null)
                return null;
            //存储到内存
            imageCaches.put(url, bitmap);
            return bitmap;
        }
        return null;
    }

    //获取缓存的文件路径
    private File getCacheFile(String url){
        String name = MD5Utils.encode(url);
        String state = Environment.getExternalStorageState();
        File dir = new File(mContext.getCacheDir(), "/icon");
        if(!dir.exists()){
            dir.mkdirs();
        }
        return new File(dir, name);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void displayNet(final ImageView iv, String url){
        //现在内存中查找
        Bitmap bitmap = imageCaches.get(url);
        if(bitmap != null) {
            //内存中有，显示图片即可
            ScaleImage(iv, bitmap);
            final Bitmap finalBitmap1 = bitmap;
            long date = System.currentTimeMillis();
            ImgDataBase db = ImgDataBase.getDataBase("ImgTest.db");
            db.uploadTFlag(url, date);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    iv.setImageBitmap(finalBitmap1);
                }
            });
            return ;
        }
        //如果内存中没有，需要在本地获取
        bitmap = loadFromLocal(url);
        if(bitmap != null){
            ScaleImage(iv, bitmap);
            final Bitmap finalBitmap = bitmap;
            long date = System.currentTimeMillis();
            ImgDataBase db = ImgDataBase.getDataBase("ImgTest.db");
            db.uploadTFlag(url, date);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    iv.setImageBitmap(finalBitmap);
                }
            });
            return ;
        }
        //否则需要从网络获取
        loadFromNetTrick(iv, url);
    }

    private void loadFromNetTrick(ImageView iv, String url){
        mTags.put(iv, url);
        mPool.execute(new LoadImageTask(iv, url));
    }

    private class LoadImageTask implements Runnable{
        private ImageView iv;
        private String url;

        public LoadImageTask(ImageView iv, String url){
            this.iv = iv;
            this.url = url;
        }

        @Override
        public void run() {
            //从网络中获得图片
            try{
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                //连接服务器超时
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                //连接
                connection.connect();

                //获取流
                InputStream is = connection.getInputStream();

                //将流变成bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                //获取宽高

                int height = bitmap.getHeight();
                int width = bitmap.getWidth();

                Log.d("FALSE LOAD:", "" + width);
                Log.d("FALSE LOAD:", "" + height);

                int ivWidth = iv.getWidth();
                int ivHeight = iv.getHeight();

                ivWidth = ivWidth > 0? ivWidth : 480;
                ivHeight = ivHeight > 0? ivHeight : 360;

                int dx = width/ivWidth;
                int dy = height/ivHeight;

                int scale = 1;

                if(dx > dy && dy >= 1){
                    scale = dx;
                }
                if(dy > dx && dx >= 1){
                    scale = dy;
                }

                int newWidth = width / scale;
                int newHeight = height / scale;

                bitmap = Bitmap.createScaledBitmap(bitmap,newWidth,newHeight,true);

                //存储到本地
                saveToLocal(bitmap , url);

                //存储到内存
                imageCaches.put(url, bitmap);

                //在显示UI之前，拿到最新的url地址
                String recentlyUrl = (String)iv.getTag();

                if(url.equals(recentlyUrl)){
                    mHandler.post(new Runnable(){
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run(){
                            display(iv, url);
                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
