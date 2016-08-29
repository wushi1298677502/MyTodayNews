/*
 * Created by Storm Zhang, Feb 11, 2014.
 */

package com.android.volley.myrequest;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class GsonRequest<T> extends Request<T> {
    private final Listener<T> mListener;
    private final Class<T> mClazz;
    private Map<String, String> mHeaders;
    private Map<String, String> mParames;
    private final Gson mGson = new Gson();

    /**
     * Get请求
     * @param url
     * @param clazz
     * @param listener
     * @param errorListener
     */
    public GsonRequest(String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
        this(Method.GET, url, clazz, null, null, listener, errorListener);
    }

    /**
     * Post请求 传参
     * @param url
     * @param parames
     * @param clazz
     * @param listener
     * @param errorListener
     */
    public GsonRequest(String url, Map<String, String> parames, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
        this(Method.POST, url, clazz, null, parames, listener, errorListener);
    }

    public GsonRequest(int method, String url, Class<T> clazz,
                       Map<String, String> headers, Map<String, String> parames, Listener<T> listener,
                       ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mClazz = clazz;
        this.mHeaders = headers;
        this.mParames = parames;
        this.mListener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParames != null ? mParames : super.getParams();
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(mGson.fromJson(json, mClazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
