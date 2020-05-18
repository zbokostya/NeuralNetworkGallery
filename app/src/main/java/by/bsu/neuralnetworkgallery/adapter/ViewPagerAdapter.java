package by.bsu.neuralnetworkgallery.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import by.bsu.neuralnetworkgallery.R;
import by.bsu.neuralnetworkgallery.entity.Photo;

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
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.viewpager_item, container, false);
        ImageView imageView = view.findViewById(R.id.viewPagerImage);
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
                    show = false;
                } else {
                    activity.getSupportActionBar().show();
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
