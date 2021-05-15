package by.bsu.neuralnetworkgallery.dao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import by.bsu.neuralnetworkgallery.R;
import by.bsu.neuralnetworkgallery.entity.Style;
import by.bsu.neuralnetworkgallery.utils.RequestQueueSingleton;

public class StyleReader {
    final private String STYLES_URL = "http://51.15.53.117:9901/api/userfiles/styles";
    private ArrayList<Style> styles = new ArrayList<>();
    private boolean ready = false;


    public void read(Context context) {
        ready = false;
        styles.clear();
        styles.add(new Style("0", "0", "Choose your own style"));
        Log.i("StyleReader", "get styles");
        ready = true;
        for (int i = 1; i < 7; i++) {
            styles.add(new Style("0", i + "", "Style: " + i));
        }
        ready = true;
//            int finalI = i;
//            ImageRequest imageRequest = new ImageRequest(STYLES_URL + "?originalName=" + "prepared_style_" + i + ".jpg", new Response.Listener<Bitmap>() {
//                @Override
//                public void onResponse(Bitmap response) {
//                    Log.i("ServerConnection", "image get success");
//                    styles.add(new Style(response, finalI + ""));
//                    if (finalI == 6) {
//                        ready = true;
//                    }
//                }
//            }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.i("ServerConnection", "image get error: " + error.toString());
//                }
//            });
//            RequestQueueSingleton.getInstance(context).addToRequestQueue(imageRequest);


    }

    public boolean isReady() {
        return ready;
    }

    public ArrayList<Style> getStyles() {
        return styles;
    }

//    private void parse(JSONObject object) throws JSONException {
//        JSONArray styles = object.getJSONArray("styles");
//        for (int i = 0; i < styles.length(); ++i) {
//            Style style = new Style.Builder()
//                    .withId(styles.getJSONObject(i).getString("id"))
//                    .withTitle(styles.getJSONObject(i).getString("title"))
//                    .withUrl(styles.getJSONObject(i).getString("url"))
//                    .withDescription(styles.getJSONObject(i).getString("description"))
//                    .build();
//            this.styles.add(style);
//        }
//        ready = true;
//        Log.i("StyleReader", "Read " + this.styles.size() + " styles");
//    }
}
