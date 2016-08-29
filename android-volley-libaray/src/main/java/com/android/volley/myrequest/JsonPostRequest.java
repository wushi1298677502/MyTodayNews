package com.android.volley.myrequest;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class JsonPostRequest extends Request<JSONObject> {
	private Map maps;
	private final Listener<JSONObject> mListener;

	public JsonPostRequest(int method, String url, Map maps, Listener<JSONObject> listener,
						   ErrorListener errorListener) {
		super(method, url, errorListener);
		mListener = listener;
		this.maps = maps;
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return maps != null ? maps : super.getParams();
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			
			return Response.success(new JSONObject(jsonString),HttpHeaderParser.parseCacheHeaders(response));
			
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			return Response.error(new ParseError(je));
		}
	}

	@Override
	protected void deliverResponse(JSONObject response) {
		mListener.onResponse(response);
	}
}
