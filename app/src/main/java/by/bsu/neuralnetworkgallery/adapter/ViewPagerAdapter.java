package by.bsu.neuralnetworkgallery.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import by.bsu.neuralnetworkgallery.R;
import by.bsu.neuralnetworkgallery.activity.EditActivity;
import by.bsu.neuralnetworkgallery.entity.Photo;
import by.bsu.neuralnetworkgallery.utils.ImageGestureDetector;

public class ViewPagerAdapter extends PagerAdapter {
    private AppCompatActivity activity;
    private LayoutInflater inflater;
    private ArrayList<Photo> photos;
    private boolean show = true;

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
        final Button button = view.findViewById(R.id.edit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EditActivity.class);
                Uri uri = Uri.fromFile(new File(photos.get(position).getPicturePath()));
                intent.putExtra("image_path", uri.toString());
                activity.startActivity(intent);
            }
        });
        Glide.with(activity.getApplicationContext())
                .load(photos.get(position).getPicturePath())
                .into(imageView);
        container.addView(view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.getSupportActionBar().setShowHideAnimationEnabled(false);
                if (show) {
                    activity.getSupportActionBar().hide();
                    button.setVisibility(View.INVISIBLE);
                    show = false;
                } else {
                    activity.getSupportActionBar().show();
                    button.setVisibility(View.VISIBLE);
                    show = true;
                }
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
