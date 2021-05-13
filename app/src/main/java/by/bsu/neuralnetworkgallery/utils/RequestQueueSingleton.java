package by.bsu.neuralnetworkgallery.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestQueueSingleton {
    private static volatile RequestQueueSingleton instance;

    RequestQueue queue;

    private RequestQueueSingleton(Context context) {
        this.queue = Volley.newRequestQueue(context);
    }

    public static RequestQueueSingleton getInstance(Context context) {
        RequestQueueSingleton result = instance;
        if (result != null) {
            return result;
        }
        synchronized (RequestQueueSingleton.class) {
            if (instance == null) {
                instance = new RequestQueueSingleton(context);
            }
            return instance;
        }
    }

    public void addToRequestQueue(Request request) {
        this.queue.add(request);
    }

}
