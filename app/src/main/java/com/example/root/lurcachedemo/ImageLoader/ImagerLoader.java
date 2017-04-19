package com.example.root.lurcachedemo.ImageLoader;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.root.lurcachedemo.BitmapTools;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by root on 17-4-8.
 */

public class ImagerLoader {
    private ArrayList<Future> mTasks = new ArrayList<>();
    //默认为内存缓存
    private ImageCache mImageCache = new MemoryCache();
    //线程池,线程池数量为CPU的数量
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.
            getRuntime().availableProcessors());


    public void displayImage(final String url, final ImageView imageView) {
        imageView.setTag(url);
        Bitmap bmp = mImageCache.get(url);
        if (bmp != null) {
            imageView.setImageBitmap(bmp);
        } else {
            downloadIamgeAsync(url, imageView);
        }
    }

    private void downloadIamgeAsync(final String url, final ImageView imageView) {
        Future mFuture = mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = downloadImage(url, imageView.getMeasuredWidth(), imageView.getMeasuredHeight());
                if (bitmap == null)
                    return;
                if (imageView.getTag().equals(url))
                    imageView.setImageBitmap(bitmap);
                if (mImageCache != null) {
                    mImageCache.put(url, bitmap);
                }
            }
        });
    }

    private Bitmap downloadImage(String url, int reqWidth, int reqHeight) {
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        try {
            URL mRUL = new URL(url);
            conn = (HttpURLConnection) mRUL.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            bitmap = BitmapTools.decodeSampledBitmapFromInputStream(conn.getInputStream(), reqWidth, reqHeight);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void setImageCahce(ImageCache imageCache) {
        mImageCache = imageCache;
    }


}



