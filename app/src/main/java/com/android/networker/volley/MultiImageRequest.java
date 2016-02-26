package com.android.networker.volley;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

/**
 * Volley中的ImageRequest有默认的数据解析，会转换成符合要求的bitmap返回，项目中有可能是gif类图片，
 * 如果转换成bitmap，会只有第一帧。这时我们需要拿到返回的数据，转换成我们需要的类型。
 * 而不是统统的都是bitmap。
 * 一个返回字节数据的ImageRequest
 * Created by ljtyzhr on 2016/1/25.
 */
public class MultiImageRequest extends Request<byte[]> {

    /** 请求超时时间 */
    private static final int IMAGE_TIMEOUT_MS = 2000;
    /** 重试次数 */
    private static final int IMAGE_MAX_RETRIES = 2;
    /** 超时时间的乘数，重试时才用到 */
    private static final float IMAGE_BACKOFF_MULT = 2f;

    private final Response.Listener<byte[]> mListener;

    /**
    * Creates a new image request
    *
    * @param url URL of the image
    * @param listener Listener to receive
    * @param errorListener Error listener, or null to ignore errors
    */
    public MultiImageRequest(String url, Response.Listener<byte[]> listener,
                             Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(IMAGE_TIMEOUT_MS, IMAGE_MAX_RETRIES, IMAGE_BACKOFF_MULT));
        mListener = listener;
    }

    @Override
    public Priority getPriority() {
        return Priority.LOW;
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(byte[] response) {
        mListener.onResponse(response);
    }
}
