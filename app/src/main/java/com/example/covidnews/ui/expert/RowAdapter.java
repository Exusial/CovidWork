package com.example.covidnews.ui.expert;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.covidnews.NetParser.ImageLoader;
import com.example.covidnews.R;


import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RowAdapter extends BaseQuickAdapter<Expert_row, BaseViewHolder> {
    private static ExecutorService mPool;
    private Handler mHandler;

    public RowAdapter(int layid, List<Expert_row> list){
        super(layid,list);
        if(mPool == null)
            mPool = Executors.newFixedThreadPool(3);
        if(mHandler == null)
            mHandler = new Handler();
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Expert_row expert_row) {
        baseViewHolder.setText(R.id.ntitle1,expert_row.name).setText(R.id.ntitle2,expert_row.professions).setText(R.id.ntitle3,expert_row.inst);
        ImageView avatar = baseViewHolder.getView(R.id.avatar);
        avatar.setImageResource(R.drawable.person);
        //设置图片Tag
        avatar.setTag(expert_row.avatar);
        ImageLoader loader = ImageLoader.getInstance();

        //设置得到
        Bitmap bitmap = loader.display(avatar, expert_row.avatar);

        if(bitmap != null){
            avatar.setImageBitmap(bitmap);
        }else{
            BitmapTask task = new BitmapTask(expert_row.avatar, avatar);
            mPool.execute(task);
        }
    }

    class BitmapTask implements Runnable {
        String url;
        ImageView iv;

        BitmapTask(String url, ImageView iv){
            this.url = url;
            this.iv = iv;
        }

        @Override
        public void run() {
            final Bitmap bitmap = doInBackground(iv, url);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onPostExecute(bitmap);
                }
            });
        }

        private Bitmap doInBackground(ImageView iv, String url){
            //后台下载图片
            ImageLoader loader = ImageLoader.getInstance();
            return loader.GetFromNet(iv, url);
        }

        private void onPostExecute(Bitmap bitmap){
            ImageView imageView = (ImageView) getRecyclerView().findViewWithTag(url);
            if(imageView != null && bitmap != null){
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}

