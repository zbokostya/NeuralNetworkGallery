package by.bsu.neuralnetworkgallery.dao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import by.bsu.neuralnetworkgallery.utils.MultipartRequest;
import by.bsu.neuralnetworkgallery.utils.RequestQueueSingleton;

//public class ServerConnector {
//    final private String X_API_KEY = "ra64ysKo6o2dTe0Dk9Qnv3jLNY3p3z5z1PGN6vJH";
//    final private String POST_URL = "https://api.deeparteffects.com/v1/noauth/upload";
//    final private String GET_URL = "https://api.deeparteffects.com/v1/noauth/result?submissionId=";
//
//    private Context context;
//    private Bitmap bitmap;
//    private boolean isReady = false;
//
//    public ServerConnector(Context context, ImageView imageView) {
//        this.context = context;
//        bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
//    }
//
//    public void postImage(String style) {
//        isReady = false;
//        Log.i("ServerConnection", "post image");
//        JSONObject request = new JSONObject();
//        try {
//            request.put("styleId", style);
//            request.put("imageBase64Encoded", getStringImage());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        RequestQueue queue = Volley.newRequestQueue(context);
//        JsonObjectRequest rr = new JsonObjectRequest(Request.Method.POST, POST_URL, request, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.i("ServerConnection", "post success");
//                try {
//                    getLink(response.getString("submissionId"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("ServerConnection", "error: " + error.toString());
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("X-Api-Key", X_API_KEY);
//                return params;
//            }
//        };
//        queue.add(rr);
//    }
//
//    private void getLink(final String id) {
//        Log.i("ServerConnection", "get link");
//        RequestQueue queue = Volley.newRequestQueue(context);
//        JsonObjectRequest rr = new JsonObjectRequest(Request.Method.GET, GET_URL + id, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    String result = response.getString("status");
//                    Log.i("ServerConnection", result);
//                    if (!result.equals("finished")) {
//                        final Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                getLink(id);
//                            }
//                        }, 3000);
//                    } else {
//                        getResult(response.getString("url"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.i("ServerConnection", "get link error: " + error.toString());
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("X-Api-Key", X_API_KEY);
//                return params;
//            }
//        };
//        queue.add(rr);
//    }
//
//    public boolean isReady() {
//        return isReady;
//    }
//
//    public Bitmap result() {
//        return bitmap;
//    }
//
//    private void getResult(String url) {
//        RequestQueue queue = Volley.newRequestQueue(context);
//        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
//            @Override
//            public void onResponse(Bitmap response) {
//                Log.i("ServerConnection", "image get success");
//                bitmap = response;
//                isReady = true;
//            }
//        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.i("ServerConnection", "image get error: " + error.toString());
//            }
//        });
//        queue.add(imageRequest);
//    }
//
//
//    private String getStringImage() {
//        ByteArrayOutputStream baos;
//        String encodedImage = "";
//        int quality = 100;
//        do {
//            baos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
//            byte[] imageBytes = baos.toByteArray();
//            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//            quality-=5;
//        } while (encodedImage.length() >= 500000);
//        return encodedImage;
//    }
//}

public class ServerConnector {
    final private String POST_URL = "51.15.53.117:9901/api/userfiles";
    final private String GET_URL = "51.15.53.117:9901/api/userfiles";

    private Context context;
    private Bitmap bitmap;
    private boolean isReady = false;
    Date curDate;

    public ServerConnector(Context context, ImageView imageView) {
        this.context = context;
        bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
    }

    public void postImage(String filename) {
        isReady = false;
        File file = new File(context.getCacheDir(), filename);
        try {
            file.createNewFile();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        curDate = Calendar.getInstance().getTime();
        Map<String, String> stringValues = new HashMap<>();
        stringValues.put("originalName", curDate + "_" + filename);

        MultipartRequest multipartRequest = new MultipartRequest(POST_URL, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("onErrorResponse", "error");
            }
        }, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponse", "get");
            }
        },
                file,
                file.length(),
                stringValues,
                new HashMap<>(),
                "imageFile",
                new MultipartRequest.MultipartProgressListener() {
                    @Override
                    public void transferred(long transfered, int progress) {

                    }
                }
        ) {
        };
        RequestQueueSingleton.getInstance(context).addToRequestQueue(multipartRequest);
    }


    public boolean isReady() {
        return isReady;
    }

    public boolean isImageChanged(String filename) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_URL + "?originalName=" + "edit_" + curDate + "_" + filename, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("123", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
        return false;
    }

    public Bitmap result() {
        return bitmap;
    }

    public void getImage(String filename) {
        ImageRequest imageRequest = new ImageRequest(GET_URL + "/file?originalName=" + "edit_" + curDate + "_" + filename, new Response.Listener<Bitmap>() {
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

        RequestQueueSingleton.getInstance(context).addToRequestQueue(imageRequest);
    }

}

