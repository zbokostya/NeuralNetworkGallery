package by.bsu.neuralnetworkgallery.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import by.bsu.neuralnetworkgallery.R;
import by.bsu.neuralnetworkgallery.activity.EditActivity;
import by.bsu.neuralnetworkgallery.activity.PhotoActivity;
import by.bsu.neuralnetworkgallery.entity.Photo;
import by.bsu.neuralnetworkgallery.utils.ImageGestureDetector;
import by.bsu.neuralnetworkgallery.utils.onClickedListener;

public class ViewPagerAdapter extends PagerAdapter {
    private AppCompatActivity activity;
    private LayoutInflater inflater;
    private ArrayList<Photo> photos;

    public ViewPagerAdapter(AppCompatActivity activity, ArrayList<Photo> photos) {
        this.activity = activity;
        this.photos = photos;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.viewpager_item, container, false);
        ImageView imageView = view.findViewById(R.id.viewPagerImage);
        Glide.with(activity.getApplicationContext())
                .load(photos.get(position).getPicturePath())
                .into(imageView);
        container.addView(view);
        ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(imageView.getContext(), new ImageGestureDetector(imageView, activity));
        GestureDetector gestureDetector = new GestureDetector(imageView.getContext(), new ImageGestureDetector(imageView, activity));
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scaleGestureDetector.onTouchEvent(motionEvent);
                gestureDetector.onTouchEvent(motionEvent);
                return true;
            }

        });
        return view;
    }


    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((View) object);
    }

}
