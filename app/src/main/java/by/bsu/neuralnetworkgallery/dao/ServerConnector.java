package by.bsu.neuralnetworkgallery.dao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ServerConnector {
    final private String X_API_KEY = "J8s6oR3w8O2PdGJPPQMC94KsGp0swPaH4bq34ouo";
    final private String POST_URL = "https://api.deeparteffects.com/v1/noauth/upload";
    final private String GET_URL = "https://api.deeparteffects.com/v1/noauth/result?submissionId=";

    private Context context;
    private ImageView imageView;
    private Bitmap bitmap;
    private boolean isReady = false;

    public ServerConnector(Context context, ImageView imageView) {
        this.context = context;
        this.imageView = imageView;
        bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
    }

    public void postImage(String style) {
        isReady = false;
        Log.i("ServerConnection", "post image");
        JSONObject request = new JSONObject();
        try {
            request.put("styleId", style);
            request.put("imageBase64Encoded", getStringImage(bitmap));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest rr = new JsonObjectRequest(Request.Method.POST, POST_URL, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("ServerConnection", "post success");
                try {
                    getLink(response.getString("submissionId"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ServerConnection", "error: " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-Api-Key", X_API_KEY);
                return params;
            }
        };
        queue.add(rr);
    }

    private void getLink(final String id) {
        Log.i("ServerConnection", "get link");
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest rr = new JsonObjectRequest(Request.Method.GET, GET_URL + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String result = response.getString("status");
                    Log.i("ServerConnection", result);
                    if (!result.equals("finished")) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getLink(id);
                            }
                        }, 3000);
                    } else {
                        getResult(response.getString("url"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ServerConnection", "get link error: " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-Api-Key", X_API_KEY);
                return params;
            }
        };
        queue.add(rr);
    }

    public boolean isReady(){
        return isReady;
    }

    public Bitmap result(){
        return bitmap;
    }

    private void getResult(String url){
        RequestQueue queue = Volley.newRequestQueue(context);
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                Log.i("ServerConnection", "image get success");
                bitmap = response;
                isReady = true;
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ServerConnection", "image get error: " + error.toString());
            }
        });
        queue.add(imageRequest);
    }

    private String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos;
        String encodedImage = "";
        int quality = 100;
        do {
            baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            quality-=5;
        } while (encodedImage.length() >= 500000);
        return encodedImage;
    }
}
