package com.example.root.lurcachedemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.root.lurcachedemo.ImageLoader.DiskCache;
import com.example.root.lurcachedemo.ImageLoader.DoubleCache;
import com.example.root.lurcachedemo.ImageLoader.ImageCache;
import com.example.root.lurcachedemo.ImageLoader.MemoryCache;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageCache mBitmapCache;
    private GridView mImageWall;
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBitmapCache = new MemoryCache();
        mImageWall = (GridView) findViewById(R.id.image_wall);
        adapter = new BaseAdapter(this);
        mImageWall.setAdapter(adapter);
        mImageWall.setOnScrollListener(onlistener);

    }

    private AbsListView.OnScrollListener onlistener = new AbsListView.OnScrollListener() {
        private boolean mIsFirstRun = true;
        private int firstVisibleItem;
        private int visibleItemCount;


        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

            if(scrollState==SCROLL_STATE_IDLE){
                loadBitmaps(firstVisibleItem,visibleItemCount);
            } else {
                BitmapDownloadTask.CancleAll();
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            this.firstVisibleItem = firstVisibleItem;
            this.visibleItemCount = visibleItemCount;
            if(mIsFirstRun&&visibleItemCount>0&&totalItemCount>0){
                mIsFirstRun = false;
                loadBitmaps(firstVisibleItem,visibleItemCount);
            }
        }
    };


    private void loadBitmaps(int firstVisibleItem, int visibleItemCount) {
        for(int i = firstVisibleItem; i < firstVisibleItem+visibleItemCount; i++){
            String imageUrl = ImageUrls.IMAGE_URLS[i];
            Bitmap bitmap = mBitmapCache.get(imageUrl);
            ImageView imageView = (ImageView) mImageWall.findViewWithTag(imageUrl);
            if(imageView == null)
                continue;
            if(bitmap==null){
                BitmapDownloadTask task = new BitmapDownloadTask(mBitmapCache,imageView,imageUrl);
                BitmapDownloadTask.addTask(task);
                task.execute();
            } else {
                imageView.setImageBitmap(bitmap);
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        BitmapDownloadTask.CancleAll();
    }

    private class BaseAdapter extends android.widget.BaseAdapter{

        private Context mContext;

        public BaseAdapter(Context context){
            mContext = context;
        }

        @Override
        public int getCount() {
            return ImageUrls.IMAGE_URLS.length;
        }

        @Override
        public String getItem(int position) {
            return ImageUrls.IMAGE_URLS[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
            if(convertView==null){
                convertView = inflater.inflate(R.layout.item_iamgeview,parent,false);
            }

            String url =  getItem(position);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.img);
            imageView.setTag(url);
            setImagerView(url,imageView);
            return convertView;
        }

        private void setImagerView(String url, ImageView imageView){
            Bitmap bitmap = mBitmapCache.get(url);
            if(bitmap==null){
                imageView.setImageResource(R.mipmap.ic_launcher);
            } else {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
