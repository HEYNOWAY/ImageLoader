package com.example.root.lurcachedemo.ImageLoader;

import android.graphics.Bitmap;

/**
 * Created by root on 17-4-8.
 */

public interface ImageCache {
    void put(String url, Bitmap bitmap);
    Bitmap get(String url);
}
