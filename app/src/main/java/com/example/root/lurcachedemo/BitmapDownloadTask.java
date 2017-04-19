package com.example.root.lurcachedemo;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.root.lurcachedemo.ImageLoader.ImageCache;
import com.example.root.lurcachedemo.ImageLoader.MemoryCache;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 17-4-6.
 */

public class BitmapDownloadTask extends AsyncTask<Void,Void,Bitmap> {
    public static List<BitmapDownloadTask> mTasks = new ArrayList<>();
    private static final String TAG = "debug";

    private String mImgUrl;
    private SoftReference<ImageView> mImageViewSoftReference;

    private ImageCache imageCache;
    private static ImageCache deafultImageCache = new MemoryCache();
    private int mReqWidth;
    private int mReqHeight;


    public BitmapDownloadTask(ImageCache bitmapCache, ImageView imgView, String url){
        imageCache = bitmapCache;
        mImageViewSoftReference = new SoftReference<>(imgView);
        mImgUrl = url;
        mReqWidth = imgView.getMeasuredWidth();
        mReqHeight = imgView.getMeasuredHeight();
    }

    public BitmapDownloadTask(ImageView imageView, String url){
        this(deafultImageCache,imageView,url);
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        return downloadBitmap();
    }

    private Bitmap downloadBitmap(){
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(mImgUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            bitmap = BitmapTools.decodeSampledBitmapFromInputStream(conn.getInputStream(),
                    mReqWidth,mReqHeight);
            Log.d("debug","download....");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(conn!=null){
                conn.disconnect();
            }
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        removeTask(this);
        if(bitmap!=null)
            imageCache.put(mImgUrl,bitmap);
        ImageView imageView = mImageViewSoftReference.get();
        if(isCancelled()||imageView==null||imageView.getTag()!=mImgUrl){
            bitmap = null;
        }
        if(bitmap!=null){
            imageView.setImageBitmap(bitmap);
        }
        super.onPostExecute(bitmap);
    }


    public static void addTask(BitmapDownloadTask task){
        mTasks.add(task);
    }

    public static void removeTask(BitmapDownloadTask task){
        mTasks.remove(task);
    }

    public static void CancleAll(){
        for(BitmapDownloadTask task : mTasks){
            task.cancel(true);
        }
        mTasks.clear();
    }
}
