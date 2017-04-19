package com.example.root.lurcachedemo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * Created by root on 17-4-6.
 */

public class BitmapTools {

    public static Bitmap decodSampleBitmapFromPath(String path, int reqWidth, int reqHgigth){
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,opts);
        opts.inSampleSize = calculateInSampleSize(opts,reqWidth,reqHgigth);
        opts.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path,opts);

    }

    public static Bitmap decodeSampledBitmapFromData(byte[] data, int reqWidth, int reqHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        opts.inSampleSize = calculateInSampleSize(opts, reqWidth, reqHeight);
        opts.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, opts);
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int id, int reqWidth, int reqHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, id, opts);
        opts.inSampleSize = calculateInSampleSize(opts, reqWidth, reqHeight);
        opts.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, id, opts);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWdith, int reqHeight){
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if(height>reqHeight||width>reqWdith){
            final int halfHeight = height>>1;
            final int halfWidth = width>>1;
            while((halfHeight/inSampleSize)>reqHeight&&(halfWidth/inSampleSize)>reqWdith){
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromInputStream(InputStream is, int reqWidth, int reqHeight) {
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        if (bitmap == null)
            return null;
        if (bitmap.getWidth() > reqWidth || bitmap.getHeight() > reqHeight) {
            int newWidth;
            int newHeight;
            if (bitmap.getWidth() > bitmap.getHeight()) {
                newWidth = reqWidth;
                newHeight = newWidth * bitmap.getHeight() / bitmap.getWidth();
            } else {
                newHeight = reqHeight;
                newWidth = newHeight * bitmap.getWidth() / bitmap.getHeight();
            }
            bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
        }
        return bitmap;
    }



}
