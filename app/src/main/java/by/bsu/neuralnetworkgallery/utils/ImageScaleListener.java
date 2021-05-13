package by.bsu.neuralnetworkgallery.utils;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


public class ImageScaleListener implements View.OnTouchListener, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, ScaleGestureDetector.OnScaleGestureListener {

    private float onScaleBegin = 0;
    private float onScaleEnd = 0;
    private float scale = 1f;
    private final View imageView;
    private final int width;
    private final int height;

    public ImageScaleListener(View imageView) {
        this.imageView = imageView;
        width = imageView.getWidth();
        height = imageView.getHeight();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
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
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = (int) (scale * width);
        layoutParams.height = (int) (scale * height);
        imageView.setLayoutParams(layoutParams);
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
        return false;
    }

}