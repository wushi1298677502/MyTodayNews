package com.android.volley.myrequest;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;

/**
 * StringRequest : Returns the charset specified in the Content-Type of this header,
 * or the HTTP default (ISO-8859-1) if none can be found.
 */
public class UTF8StringRequest extends StringRequest {

	public UTF8StringRequest(int method, String url, Listener<String> listener,
							 ErrorListener errorListener) {
		super(method, url, listener, errorListener);
	}
	
	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		 String str = null;
	        try {
	            str = new String(response.data, "UTF-8");
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        }
	        return Response.success(str, HttpHeaderParser.parseCacheHeaders(response));
	}
}
