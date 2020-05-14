package by.bsu.neuralnetworkgallery.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import by.bsu.neuralnetworkgallery.R;

public class RequestActivity extends Activity {

    Bitmap bitmap;
    ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        Button choose = findViewById(R.id.choose);
        image = findViewById(R.id.imageView);
        String url = "https://api.remove.bg/v1.0/removebg";
        Log.i("test228", "onCreate: ");
        Button change = findViewById(R.id.change);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("test228", "click");
                postImage(bitmap);
            }
        });
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 111);
            }
        });
    }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos;
        String encodedImage = "";
        int quality = 100;
        do {
            baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            quality--;
        } while (encodedImage.length() >= 500000);
        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 111 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                //getting image from gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //Setting image to ImageView
                image.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void getStyles(){
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest rr = new JsonObjectRequest(Request.Method.GET, "https://api.deeparteffects.com/v1/noauth/styles", null,  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //result = response;
                Toast.makeText(getApplicationContext(), "WOrk", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("X-Api-Key", "nbp29jHjEL8wnVp7AsHLaTJAMChbVA1q6fPjyB60");
                return params;
            }
        };
        queue.add(rr);
    }

    public void postImage(Bitmap bitmap){
        Log.i("test228", "postImage");
        JSONObject request = new JSONObject();
        try {
            request.put("styleId","c7984b32-1560-11e7-afe2-06d95fe194ed");
            request.put("imageBase64Encoded",getStringImage(bitmap) + "aa");
            //request.put("optimizeForPrint", "true");
            Log.i("test228", request.getString("imageBase64Encoded").length()+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest rr = new JsonObjectRequest(Request.Method.POST, "https://api.deeparteffects.com/v1/noauth/upload", request,  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("test228", "post success");
                try {
                    getLink(response.getString("submissionId"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("test228", "post error" + error.getMessage());
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-Api-Key", "nbp29jHjEL8wnVp7AsHLaTJAMChbVA1q6fPjyB60");
                return params;
            }
        };
        rr.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        queue.add(rr);
    }

    public void getLink(final String id){
        Log.i("test228", "get link");
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest rr = new JsonObjectRequest(Request.Method.GET, "https://api.deeparteffects.com/v1/noauth/result?submissionId=" + id, null,  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String result = response.getString("status");
                    Log.i("test228", result);
                    if(!result.equals("finished")){
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getLink(id);
                            }
                        }, 3000);
                    }else{
                        Log.i("test228", "get image");
                        getResult(response.getString("url"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("test228", "getLink error");
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-Api-Key", "nbp29jHjEL8wnVp7AsHLaTJAMChbVA1q6fPjyB60");
                return params;
            }
        };
        queue.add(rr);
    }

    public void getResult(String url){
        RequestQueue queue = Volley.newRequestQueue(this);
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                Log.i("test228", "image get success");
                image.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("test228", "image get error");
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        queue.add(imageRequest);
    }
}
