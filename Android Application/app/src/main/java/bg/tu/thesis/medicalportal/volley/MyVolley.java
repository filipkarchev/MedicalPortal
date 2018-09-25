package bg.tu.thesis.medicalportal.volley;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import bg.tu.thesis.medicalportal.Utils;

public class MyVolley {
	private static RequestQueue mRequestQueue;
	private static Context mContext;

	private MyVolley() {

	}

	public static void init(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);

		mContext = context;

	}

	public static RequestQueue getRequestQueue() {
		if (mRequestQueue != null) {
			return mRequestQueue;
		} else {
			throw new IllegalStateException("RequestQueue not initialized");
		}
	}

	public static void requestJSONObject(final String url,
			final Listener<JSONObject> listener,
			final ErrorListener errorListener) {
		Log.e("requestJSONObject", url);

		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				RequestQueue queue = MyVolley.getRequestQueue();
				JsonObjectRequest myReq = new JsonObjectRequest(Method.GET,
						url, "", listener, errorListener);
				myReq.setRetryPolicy(new DefaultRetryPolicy(5000, 3,
						DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
				queue.add(myReq);
			}
		};

		if (mContext != null) {
			if (!Utils.isNetworkAvailable(mContext)) {
				Log.e("","No network");
				Utils.showNoNetworkDialog(mContext, runnable);
			} else {
				Log.e("","Network available");
				runnable.run();
			}
		}

	}

	/*
	 * public static void requestJSONObject(String url, Listener<JSONObject>
	 * listener, ErrorListener errorListener, final Map<String, String> params)
	 * { Log.e("requestJSONObject", url);
	 * 
	 * Log.i("VOLLEY", "request send!!!");
	 * 
	 * JSONObject joParamsJsonObject = icon_new JSONObject(params);
	 * Log.e("joParamsJsonObject", joParamsJsonObject.toString()); RequestQueue
	 * queue = MyVolley.getRequestQueue(); CustomRequest myReq = icon_new
	 * CustomRequest(Method.POST, url, params, listener, errorListener);
	 * myReq.setRetryPolicy(icon_new DefaultRetryPolicy(5000, 3,
	 * DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)); queue.add(myReq);
	 * Log.e("requestJSONObject", myReq.toString()); }
	 */
	public static void requestJSONObject(final String url,
			final Listener<JSONObject> listener, final ErrorListener errorListener,
			final Map<String, String> params, final boolean isGetRequest,
			final boolean authorisation) {
		Log.e("requestJSONObject", url);

		Log.i("VOLLEY", "request send!!!");
		
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				JSONObject joParamsJsonObject = new JSONObject(params);
				Log.e("joParamsJsonObject", joParamsJsonObject.toString());
				RequestQueue queue = MyVolley.getRequestQueue();
				CustomRequest myReq;
				if (isGetRequest == true) {
					myReq = new CustomRequest(Method.GET, url, params, listener,
							errorListener, authorisation);
				} else {
					myReq = new CustomRequest(Method.POST, url, params, listener,
							errorListener, authorisation);
				}
				myReq.setRetryPolicy(new DefaultRetryPolicy(5000, 3,
						DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
				queue.add(myReq);
				Log.e("requestJSONObject", myReq.toString());
				
			}
		};

		
		
		if (mContext != null) {
			if (!Utils.isNetworkAvailable(mContext)) {
				Log.e("","No network");
				Utils.showNoNetworkDialog(mContext, runnable);
			} else {
				runnable.run();
			}
		}
	}

	public static void requestJSONArray(String url,
			Listener<JSONArray> listener, ErrorListener errorListener) {
		Log.e("requestJSONArray", url);
		RequestQueue queue = MyVolley.getRequestQueue();
		JsonArrayRequest myReq = new JsonArrayRequest(url, listener,
				errorListener);
		queue.add(myReq);
	}

	/**
	 * Returns instance of ImageLoader initialized with {@see FakeImageCache}
	 * which effectively means that no memory caching is used. This is useful
	 * for images that you know that will be show only once.
	 * 
	 * @return
	 */
	/*
	 * public static ImageLoader getImageLoader() { if (mImageLoader != null) {
	 * return mImageLoader; } else { throw icon_new
	 * IllegalStateException("ImageLoader not initialized"); } }
	 */
}
