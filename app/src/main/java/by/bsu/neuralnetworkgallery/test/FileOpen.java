package by.bsu.neuralnetworkgallery.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import by.bsu.neuralnetworkgallery.entity.Folder;

public class FileOpen extends Activity {
    private static final String PARAM_MULTIPLE_IMAGE = "image";
    private static final String PARAM_IMAGE = "images";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String type = intent.getType();
        String action = intent.getAction();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendImage(intent);
            }
        }
        if (Intent.ACTION_SEND_MULTIPLE.equals(action) && getIntent().hasExtra(Intent.EXTRA_STREAM)) {
            ArrayList<Parcelable> list = getIntent().getParcelableArrayListExtra(Intent.EXTRA_STREAM);
            Intent i = new Intent(this, Folder.class);
            i.putExtra(PARAM_MULTIPLE_IMAGE, list);
            startActivity(i);
            finish();
        }
    }
    private void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            Intent i = new Intent(this, Folder.class);
            i.putExtra(PARAM_IMAGE, imageUri.toString());
            startActivity(i);
            finish();
        } else {
            Toast.makeText(this, "Error occured, URI is invalid", Toast.LENGTH_LONG).show();
        }
    }

}