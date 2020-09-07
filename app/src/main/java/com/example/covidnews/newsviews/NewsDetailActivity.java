package com.example.covidnews.newsviews;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.covidnews.NetParser.HTMLParser;
import com.example.covidnews.NetParser.ImageLoader;
import com.example.covidnews.NewsDataBase.News;
import com.example.covidnews.NewsDataBase.NewsDataBase;
import com.example.covidnews.R;

import java.util.ArrayList;

public class NewsDetailActivity extends AppCompatActivity {
    private TextView content;
    private TextView title;
    private TextView time;
    private TextView source;
    private ImageView iv;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ActionBar actionBar = getSupportActionBar();
        if(mHandler == null)
            mHandler = new Handler();
        if(actionBar!=null)
        {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        String intent = getIntent().getExtras().getString("id");
        content = findViewById(R.id.description);  //内容
        title = findViewById(R.id.news_title);
        time = findViewById(R.id.time);
        source = findViewById(R.id.res);
        iv = findViewById(R.id.image);
        SetNews(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void SetNews(String id){
        NewsDataBase newsDataBase = NewsDataBase.getDataBase("NewsTest.db");
        News news = newsDataBase.getOneData(id);
        Log.d("GOTOID", id);
        //设置图片

        new Thread(new ImgSetter(iv, news, mHandler)).start();

        title.setText(news.getTitle());
        time.setText(news.getTime());
        source.setText(news.getSource());
        //设置内容
        StringBuilder sb = new StringBuilder();
        String cont = news.getContent();
        String[] contents;
        if(news.getLanguage().equals("en")){
            contents = cont.split(".,");
        }else{
            contents = cont.split(",");
        }
        for(int i = 0; i <= contents.length - 1; i ++){
            sb.append(contents[i]);
            sb.append("\n");
        }
        String f = sb.toString();
        news.setIsClick(true);
        newsDataBase.saveOneData(news);
        content.setText(f);
        long date = System.currentTimeMillis();
        news.setTflag(date);
        newsDataBase.saveOneData(news);
    }
}

class ImgSetter implements Runnable{
    private News news;
    private ImageView iv;
    private Handler mHandler;

    ImgSetter(ImageView iv, News news, Handler mHandler){
        this.news = news;
        this.iv = iv;
        this.mHandler = mHandler;
    }

    @Override
    public void run() {
        if(CheckHaveImg(news)) {
            String imgurl = news.getImgurls();
            ImageLoader imageLoader = ImageLoader.getInstance();
            final Bitmap bitmap = imageLoader.display(iv, imgurl);
            if(bitmap != null){
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iv.setImageBitmap(bitmap);
                    }
                });
            }else{
                final Bitmap bitmap1 = imageLoader.GetFromNet(iv, imgurl);
                if(bitmap1 != null){
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            iv.setImageBitmap(bitmap1);
                        }
                    });
                }
            }
        }
    }

    private boolean CheckHaveImg(News news){
        if(news.getImgurls()!=null) {
            if(news.getImgurls().equals("None"))
                return false;
            return true;
        }
        //否则需要Get判断
        String sourceUrl = news.getSourceUrl();
        String imgurls = ImgGet(sourceUrl);
        if(imgurls == null) {
            news.setImgurls("None");
            NewsDataBase newsDataBase = NewsDataBase.getDataBase("NewsTest.db");
            newsDataBase.saveOneData(news);
            return false;
        }
        else {
            news.setImgurls(imgurls);
            NewsDataBase newsDataBase = NewsDataBase.getDataBase("NewsTest.db");
            newsDataBase.saveOneData(news);
            return true;
        }
    }

    private String ImgGet(String url){
        ArrayList<String> imgs = HTMLParser.parseHTML(url);
        if(imgs.size() != 0) {
            String[] root = url.split("/");
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i <= root.length - 2; i++){
                sb.append(root[i]);
                sb.append("/");
            }
            String imgRoot = sb.toString();
            Log.d("ROOT",imgRoot);
            Log.d("Size",imgs.size() + "");
            for(int i = 0; i <= imgs.size() - 1; i++) {
                return imgRoot + imgs.get(i);
            }
        }
        return null;
    }
}
