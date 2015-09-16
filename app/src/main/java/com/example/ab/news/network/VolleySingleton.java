package com.example.ab.news.network;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.ab.news.NewsApp;

/**
 * Created by ab on 1/31/15.
 *
 * If your application makes constant use of the network, it`s
 * probably most efficient to set up a single instance of Request-
 * Queue that will last the lifetime of your app.
 *
 * implement a singleton class that encapsulates RequestQueue and
 * other Volley functionality
 *
 * A key concept is that the RequestQueue must be instantiated
 * with the Application context, not an Activity context. This
 * ensures that the RequestQueue will last for the lifetime of
 * your app, instead of being recreated every time the activity
 * is recreated
 *
 * Here is an example of a singleton class that provides
 * RequestQueue and ImageLoader functionality:
 */
public class VolleySingleton {
    private static VolleySingleton sInstance = null;

    private ImageLoader mImageLoader;
    private RequestQueue mRequestQueue;

    private VolleySingleton(){
        mRequestQueue = Volley.newRequestQueue(NewsApp.getAppContext());

        mImageLoader = new ImageLoader(mRequestQueue,new ImageLoader.ImageCache() {

            private LruCache<String, Bitmap> cache =
                    new LruCache<>((int)(Runtime.getRuntime().maxMemory()/1024)/8);

            @Override
            public Bitmap getBitmap(String url) {
              return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public static synchronized VolleySingleton getInstance(){
        if(sInstance==null)
        {
            sInstance=new VolleySingleton();
        }
        return sInstance;
    }

    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }

    public ImageLoader getImageLoader(){
        return mImageLoader;
    }
}