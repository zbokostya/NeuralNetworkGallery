package by.bsu.neuralnetworkgallery.utils;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import by.bsu.neuralnetworkgallery.R;

public class ImageGestureDetector implements View.OnTouchListener, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, ScaleGestureDetector.OnScaleGestureListener {

    private float onScaleBegin = 0;
    private float onScaleEnd = 0;
    private float scale = 1f;
    private View imageView;
    private boolean show = true;
    private AppCompatActivity activity;
    private RecyclerView recyclerView;

    public ImageGestureDetector(View imageView, AppCompatActivity activity) {
        this.imageView = imageView;
        this.activity = activity;
        recyclerView = activity.findViewById(R.id.recyclerViewBottom);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        activity.getSupportActionBar().setShowHideAnimationEnabled(false);
        if (show) {
            recyclerView.setVisibility(View.GONE);
            activity.getSupportActionBar().hide();
            show = false;
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            activity.getSupportActionBar().show();
            show = true;
        }
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        scale *= scaleGestureDetector.getScaleFactor();
        scale = Math.max(scale, 0.5f);
        scale = Math.min(scale, 3f);
        imageView.setScaleX(scale);
        imageView.setScaleY(scale);
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
        onScaleBegin = scale;
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        onScaleEnd = scale;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }

}