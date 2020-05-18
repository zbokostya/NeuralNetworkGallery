package by.bsu.neuralnetworkgallery.utils;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class ImageGestureDetector implements View.OnTouchListener {

    private float mMotionDownX, mMotionDownY;
    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;
    private View mView;
    private Float mScaleFactor = 1f;

    public ImageGestureDetector(View view) {
        mGestureDetector = new GestureDetector(view.getContext(), mGestureListener);
        mScaleGestureDetector = new ScaleGestureDetector(view.getContext(), mScaleGestureListener);
        mView = view;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        this.mGestureDetector.onTouchEvent(event);
        this.mScaleGestureDetector.onTouchEvent(event);
        return true;
    }

    private GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            mMotionDownX = e.getRawX() - mView.getTranslationX();
            mMotionDownY = e.getRawY() - mView.getTranslationY();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (e1.getPointerId(0) != e2.getPointerId(0))
                return false;
            //if(Math.abs(e2.getRawY() - e1.getRawY())>350&&e2.getAction()==MotionEvent.)
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return true;
        }
    };



    private ScaleGestureDetector.OnScaleGestureListener mScaleGestureListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(0.5f, Math.min(mScaleFactor, 5.0f));
            mView.setScaleX(mScaleFactor);
            mView.setScaleY(mScaleFactor);
            return true;
        }
    };
}