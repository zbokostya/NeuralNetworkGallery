package by.bsu.neuralnetworkgallery;

import androidx.appcompat.app.AppCompatActivity;
import by.bsu.neuralnetworkgallery.activity.GalleryActivity;
import by.bsu.neuralnetworkgallery.activity.RequestActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button showGalleryBtn = findViewById(R.id.btn_show_gallery);
        showGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(MainActivity.this, GalleryActivity.class);
                startActivity(galleryIntent);
            }
        });
        Button changeImageBtn = findViewById(R.id.btn_change_image);
        changeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changeImageIntent = new Intent(MainActivity.this, RequestActivity.class);
                startActivity(changeImageIntent);
            }
        });

    }



}
