package com.android.volley.myrequest;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Volley adapter for JSON requests that will be parsed into Java objects by Gson.
 */
public class FastJsonRequest<T> extends Request<T> {
    private final Listener<T> listener;
    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final Map<String, String> postParams;

    /**
     * Get请求
     *
     * @param url
     * @param clazz
     * @param listener
     * @param errorListener
     */
    public FastJsonRequest(String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
        this(Method.GET, url, clazz, null, null, listener, errorListener);
    }

    /**
     * Post请求 传参
     *
     * @param url
     * @param parames
     * @param clazz
     * @param listener
     * @param errorListener
     */
    public FastJsonRequest(String url, Map<String, String> parames, Class<T> clazz,
                           Listener<T> listener, ErrorListener errorListener) {
        this(Method.POST, url, clazz, null, parames, listener, errorListener);
    }

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url     URL of the request to make
     * @param clazz   Relevant class object, for Gson's reflection
     * @param headers Map of request headers
     */

    public FastJsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers, Map<String, String> postParams,
                           Listener<T> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.clazz = clazz;
        this.postParams = postParams;
        this.headers = headers;
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return postParams != null ? postParams : super.getParams();
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    JSON.parseObject(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
