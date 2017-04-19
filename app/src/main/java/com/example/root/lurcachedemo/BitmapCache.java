package com.example.root.lurcachedemo;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by root on 17-4-6.
 */

public class BitmapCache {
    private static String TAG = "debug";
    private LruCache<String,Bitmap> mBitmapCache;
    private ReentrantLock mLock = new ReentrantLock();

    public BitmapCache(){
        long maxSize = Runtime.getRuntime().maxMemory();
        Log.d(TAG,"maxMemory Size "+ toMB(maxSize));
        mBitmapCache = new LruCache<String,Bitmap>((int) maxSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    public void add(String key, Bitmap value){
        mLock.lock();
        try {
            mBitmapCache.put(key,value);
        } finally {
            mLock.unlock();
        }

    }

    public void remove(String key) {
        mLock.lock();
        try {
            mBitmapCache.remove(key);
        } finally {
            mLock.unlock();
        }
    }

    public Bitmap get(String key) {
        mLock.lock();
        try {
            return mBitmapCache.get(key);
        } finally {
            mLock.unlock();
        }
    }

    public boolean containsKey(String key) {
        mLock.lock();
        try {
            return mBitmapCache.get(key) != null;
        } finally {
            mLock.unlock();
        }
    }

    public static long toMB(long byteOfSize){
        return byteOfSize>>1024;
    }


}
