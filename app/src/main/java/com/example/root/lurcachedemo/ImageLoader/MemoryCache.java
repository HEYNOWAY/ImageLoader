package com.example.root.lurcachedemo.ImageLoader;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**
 * Created by root on 17-4-8.
 */

public class MemoryCache implements  ImageCache{
    LruCache<String, Bitmap> mImageCache;

    public MemoryCache(){
        initImageCache();
    }

    private void initImageCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);
        final int cacheSize = maxMemory/4;
        mImageCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight()/1024;
            }
        };
    }

    public void put(String url, Bitmap bitmap){
        mImageCache.put(url,bitmap);
    }

    public Bitmap get(String url){
        Log.d("debut","memoryCache");
        return mImageCache.get(url);
    }
}
