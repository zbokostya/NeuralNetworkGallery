package by.bsu.neuralnetworkgallery.dao;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import by.bsu.neuralnetworkgallery.entity.Style;

public class StyleReader {
    final private String X_API_KEY = "ra64ysKo6o2dTe0Dk9Qnv3jLNY3p3z5z1PGN6vJH";
    final private String STYLES_URL = "https://api.deeparteffects.com/v1/noauth/styles";
    private ArrayList<Style> styles = new ArrayList<>();
    private boolean ready = false;


    public void read(Context context) {
        ready = false;
        styles.clear();
        Log.i("StyleReader", "get styles");
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, STYLES_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    parse(response);
                } catch (JSONException e) {
                    Log.i("StyleReader", "error in parsing");
                    e.printStackTrace();
                }
                Log.i("StyleReader", "get styles success");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("StyleReader", "error in getting styles: " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("X-Api-Key", X_API_KEY);
                return params;
            }
        };
        queue.add(request);
    }

    public boolean isReady(){
        return ready;
    }

    public ArrayList<Style> getStyles(){
        return styles;
    }

    private void parse(JSONObject object) throws JSONException {
        JSONArray styles = object.getJSONArray("styles");
        for (int i = 0; i < styles.length(); ++i) {
            Style style = new Style.Builder()
                    .withId(styles.getJSONObject(i).getString("id"))
                    .withTitle(styles.getJSONObject(i).getString("title"))
                    .withUrl(styles.getJSONObject(i).getString("url"))
                    .withDescription(styles.getJSONObject(i).getString("description"))
                    .build();
            this.styles.add(style);
        }
        ready = true;
        Log.i("StyleReader", "Read " + this.styles.size() + " styles");
    }
}
