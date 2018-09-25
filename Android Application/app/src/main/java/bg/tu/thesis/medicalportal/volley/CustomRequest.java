package bg.tu.thesis.medicalportal.volley;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.provider.SyncStateContract.Constants;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

public class CustomRequest extends Request<JSONObject> {

	private Listener<JSONObject> listener;
	private Map<String, String> params;
	private boolean authorisation;

	public CustomRequest(String url, Map<String, String> params,
			Listener<JSONObject> reponseListener, ErrorListener errorListener) {
		super(Method.GET, url, errorListener);
		this.listener = reponseListener;
		this.params = params;
	}

	public CustomRequest(int method, String url, Map<String, String> params,
			Listener<JSONObject> reponseListener, ErrorListener errorListener,
			boolean authorisation) {
		super(method, url, errorListener);
		this.listener = reponseListener;
		this.params = params;
		this.authorisation = authorisation;
	}

	protected Map<String, String> getParams()
			throws com.android.volley.AuthFailureError {
		return params;
	};

/*	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		if (authorisation == false) {
			return super.getHeaders();
		} else {
			HashMap<String, String> params = icon_new HashMap<String, String>();
			String creds = String.format("%s:%s", Utils.getUserName(),
					Utils.getUserPassword());
			Log.e("CustomRequest", "autorisation " + creds);
			String auth = "Basic "
					+ Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
			params.put("Authorization", auth);
			return params;
		}
	}*/

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			return Response.success(new JSONObject(jsonString),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			return Response.error(new ParseError(je));
		}
	}

	@Override
	protected void deliverResponse(JSONObject response) {
		// TODO Auto-generated method stub
		listener.onResponse(response);
	}
}