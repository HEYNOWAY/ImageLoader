package com.example.root.lurcachedemo.ImageLoader;

import android.graphics.Bitmap;

/**
 * Created by root on 17-4-8.
 */

public class DoubleCache implements ImageCache {
    MemoryCache mMemoryCache = new MemoryCache();
    DiskCache mDiskCache = new DiskCache();

    public Bitmap get(String url){
        Bitmap bitmap = mMemoryCache.get(url);
        if(bitmap==null){
            bitmap = mDiskCache.get(url);
        }
        return bitmap;
    }

    public void put(String url,Bitmap bitmap){
        mMemoryCache.put(url,bitmap);
        mDiskCache.put(url,bitmap);
    }
}
