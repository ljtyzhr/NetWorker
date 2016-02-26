package com.android.networker.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Volley的管理类，单例实现
 * Created by ljtyzhr on 2015/12/31.
 */
public class VolleyManager {

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mContext;

    private static VolleyManager mInstance;

    private VolleyManager(Context context) {
        mContext = context.getApplicationContext();
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue,new VolleyLruCache(mContext));
    }


    /**
     * 获取实例
     *
     * @param context
     * @return
     */
    public static synchronized VolleyManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyManager(context);
        }
        return mInstance;
    }

    /**
     * 获取请求队列
     *
     * @return
     */
    public synchronized RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * 加入请求队列
     *
     * @param req
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    /**
     * 获取ImageLoader
     *
     * @return
     */
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
