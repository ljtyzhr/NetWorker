package com.android.networker.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 我们可以定义一个TypeToken支持数组类型数据
 * 覆盖getParams()方法，让其支持Post请求
 * Created by ljtyzhr on 2016/1/27.
 */
public class MultiGsonRequest<T> extends Request<T> {

    private final Listener<T> mListener;
    private static Gson mGson = new Gson();
    private Class<T> mClass;
    private Map<String, String> mParams;//post Params
    private TypeToken<T> mTypeToken;

    public MultiGsonRequest(int method, Map<String, String> params, String url, Class<T> clazz, Listener<T> listener,
                       ErrorListener errorListener) {
        super(method, url, errorListener);
        mClass = clazz;
        mListener = listener;
        mParams = params;
    }

    public MultiGsonRequest(int method, Map<String, String> params, String url, TypeToken<T> typeToken, Listener<T> listener,
                       ErrorListener errorListener) {
        super(method, url, errorListener);
        mTypeToken = typeToken;
        mListener = listener;
        mParams = params;
    }

    /**
     * Get请求的支持
     * @param url
     * @param clazz
     * @param listener
     * @param errorListener
     */
    public MultiGsonRequest(String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
        this(Method.GET, null, url, clazz, listener, errorListener);
    }

    public MultiGsonRequest(String url, TypeToken<T> typeToken, Listener<T> listener, ErrorListener errorListener) {
        this(Method.GET, null, url, typeToken, listener, errorListener);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            if (mTypeToken == null) {
                //用Gson解析返回Java对象
                return Response.success(mGson.fromJson(jsonString, mClass),
                        HttpHeaderParser.parseCacheHeaders(response));
            } else {
                JSONObject result = mGson.fromJson(jsonString, mTypeToken.getType());
                Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);

                //通过构造TypeToken让Gson解析成自定义的对象类型
                return (Response<T>) Response.success(result, cacheEntry);
            }
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }
}
