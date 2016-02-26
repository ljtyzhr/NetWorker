package com.android.networker.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;

import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * Volley的图片缓存类
 * 参见如下地址的 LruBitmapCache
 * http://developer.android.com/intl/zh-cn/training/volley/request.html
 * Created by ljtyzhr on 2016/1/25.
 */
public class VolleyLruCache extends LruCache<String, Bitmap> implements ImageCache{

    public VolleyLruCache(int maxSize) {
        super(maxSize);
    }

    public VolleyLruCache(Context ctx) {
        this(getCacheSize(ctx));
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }

    // 缓存大小约等于三倍屏幕图片大小值
    // Returns a cache size equal to approximately three screens worth of images.
    public static int getCacheSize(Context ctx) {
        final DisplayMetrics displayMetrics = ctx.getResources().
                getDisplayMetrics();
        final int screenWidth = displayMetrics.widthPixels;
        final int screenHeight = displayMetrics.heightPixels;
        // 4 bytes per pixel
        final int screenBytes = screenWidth * screenHeight * 4;

        return screenBytes * 3;
    }
}
