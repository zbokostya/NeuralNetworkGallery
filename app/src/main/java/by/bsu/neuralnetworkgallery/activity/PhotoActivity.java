package by.bsu.neuralnetworkgallery.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import by.bsu.neuralnetworkgallery.R;
import by.bsu.neuralnetworkgallery.adapter.BottomCarouselAdapter;
import by.bsu.neuralnetworkgallery.adapter.PhotoAdapter;
import by.bsu.neuralnetworkgallery.adapter.ViewPagerAdapter;
import by.bsu.neuralnetworkgallery.entity.Photo;
import by.bsu.neuralnetworkgallery.entity.Setting;
import by.bsu.neuralnetworkgallery.utils.ImageGestureDetector;
import by.bsu.neuralnetworkgallery.utils.onClickedListener;
import by.bsu.neuralnetworkgallery.values.PaperPrintFormat;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.transition.Fade;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;


public class PhotoActivity extends AppCompatActivity implements onClickedListener {

    private String imagePath;
    private String folderPath;
    private ArrayList<Photo> allpictures;
    private int position;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private final Setting[] allPhotos = {new Setting("Edit", R.drawable.edit_button), new Setting("Print on А4", R.drawable.a4_icon), new Setting("Print on А5", R.drawable.a5_icon), new Setting("Print on А3", R.drawable.a3_icon), new Setting("Print on А2", R.drawable.a2_icon), new Setting("Delete", R.drawable.delete_button)};
    private RecyclerView recyclerView;

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
        RecyclerView.Adapter bottomCarouselAdapter = new BottomCarouselAdapter(allPhotos, this, this);
        recyclerView = findViewById(R.id.recyclerViewBottom);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(bottomCarouselAdapter);
    }

    public void setPosition(int position) {
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

    @Override
    public void onPicClicked(PhotoAdapter.PicHolder holder, int position, ArrayList<Photo> pics) {

    }

    @Override
    public void onPicClicked(String pictureFolderPath, String folderName) {

    }

    @Override
    public void onPicClicked(int position) {
        String path = "file://" + allpictures.get(viewPager.getCurrentItem()).getPicturePath();
        switch (position) {
            case 0: {
                Intent move = new Intent(PhotoActivity.this, EditActivity.class);
                move.putExtra("image_uri", path);
                move.putExtra("image_name", allpictures.get(viewPager.getCurrentItem()).getPictureName());
                startActivity(move);
                break;
            }
            case 1: {
                Intent move = new Intent(PhotoActivity.this, ImageEditActivity.class);
                move.putExtra("image_uri", path);
                move.putExtra("list_height", PaperPrintFormat.A4_HEIGHT);
                move.putExtra("list_width", PaperPrintFormat.A4_WIDTH);
                startActivity(move);
                break;
            }
            case 2: {
                Intent move = new Intent(PhotoActivity.this, ImageEditActivity.class);
                move.putExtra("image_uri", path);
                move.putExtra("list_height", PaperPrintFormat.A5_HEIGHT);
                move.putExtra("list_width", PaperPrintFormat.A5_WIDTH);
                startActivity(move);
                break;
            }
            case 3: {
                Intent move = new Intent(PhotoActivity.this, ImageEditActivity.class);
                move.putExtra("image_uri", path);
                move.putExtra("list_height", PaperPrintFormat.A3_HEIGHT);
                move.putExtra("list_width", PaperPrintFormat.A3_WIDTH);
                startActivity(move);
                break;
            }
            case 4: {
                Intent move = new Intent(PhotoActivity.this, ImageEditActivity.class);
                move.putExtra("image_uri", path);
                move.putExtra("list_height", PaperPrintFormat.A2_HEIGHT);
                move.putExtra("list_width", PaperPrintFormat.A2_WIDTH);
                startActivity(move);
                break;
            }
            case 5: {
                final Uri uri = MediaStore.Files.getContentUri("external");
                File file = new File(allpictures.get(viewPager.getCurrentItem()).getPicturePath());
                Log.d("4444", file.exists()+"");
                file.delete();
                PhotoActivity.this.getContentResolver().delete(uri,
                        MediaStore.Files.FileColumns.DATA + "=?", new String[]{allpictures.get(viewPager.getCurrentItem()).getPicturePath()});
                Intent move = new Intent(PhotoActivity.this, FolderActivity.class);
                move.putExtra("folderPath", folderPath);
                move.putExtra("folderName", getIntent().getStringExtra("folderName"));
                startActivity(move);
                finish();
                break;
            }
        }

    }

}
