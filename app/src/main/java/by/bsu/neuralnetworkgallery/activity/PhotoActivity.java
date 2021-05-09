package by.bsu.neuralnetworkgallery.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.viewpager.widget.ViewPager;

import by.bsu.neuralnetworkgallery.R;
import by.bsu.neuralnetworkgallery.adapter.PhotoAdapter;
import by.bsu.neuralnetworkgallery.adapter.ViewPagerAdapter;
import by.bsu.neuralnetworkgallery.entity.Photo;
import by.bsu.neuralnetworkgallery.utils.onClickedListener;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.Fade;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class PhotoActivity extends AppCompatActivity{

    public static final String EXTRA_SPACE_PHOTO = "SpacePhotoActivity.SPACE_PHOTO";

   // private ImageView mImageView;
    private String imagePath;
    private String folderPath;
    private ArrayList<Photo> allpictures;
    private int position;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
   // String
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        folderPath = getIntent().getStringExtra("folderPath");
        getSupportActionBar().setTitle(getIntent().getStringExtra("folderName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        position = getIntent().getIntExtra("position", 0);
        allpictures = getAllImagesByFolder(folderPath);
        viewPager = findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(this, allpictures);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(position);
    }

    public void setPosition(int position){
        this.position = position;
    }


    public ArrayList<Photo> getAllImagesByFolder(String path) {
        ArrayList<Photo> images = new ArrayList<>();
        Uri allVideosuri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};
        Cursor cursor = PhotoActivity.this.getContentResolver().query(allVideosuri, projection, MediaStore.Images.Media.DATA + " like ? ", new String[]{"%" + path + "%"}, null);
        try {
            cursor.moveToFirst();
            do {
                Photo pic = new Photo();

                pic.setPictureName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));

                pic.setPicturePath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));

                pic.setPictureSize(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));

                images.add(pic);
            } while (cursor.moveToNext());
            cursor.close();
            Collections.reverse(images);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return images;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
