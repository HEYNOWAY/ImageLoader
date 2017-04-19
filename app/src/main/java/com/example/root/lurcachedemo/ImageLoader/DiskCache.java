package com.example.root.lurcachedemo.ImageLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by root on 17-4-8.
 */

public class DiskCache implements ImageCache{
    static String cacheDir= "sdcard/cache/";

    public Bitmap get(String url){
        Log.d("debug","diskCache get...");
        return BitmapFactory.decodeFile(cacheDir+url);
    }

    public void put(String url, Bitmap bitmap){
        Log.d("debug","diskCache put...");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(cacheDir+url);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(out);
        }
    }

    private void closeQuietly(Closeable closeable){
        if(closeable!=null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
