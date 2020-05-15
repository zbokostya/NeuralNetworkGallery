package by.bsu.neuralnetworkgallery.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import by.bsu.neuralnetworkgallery.R;
import by.bsu.neuralnetworkgallery.adapter.PhotoAdapter;
import by.bsu.neuralnetworkgallery.entity.Photo;
import by.bsu.neuralnetworkgallery.utils.onClickedListener;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class FolderActivity extends AppCompatActivity implements onClickedListener {
    RecyclerView recyclerView;
    String folderPath;
    ArrayList<Photo> allpictures;
    TextView folderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        folderPath = getIntent().getStringExtra("folderPath");

        folderName = findViewById(R.id.foldername);
        Log.d("text", getIntent().getStringExtra("folderName"));
        folderName.setText(getIntent().getStringExtra("folderName"));

        allpictures = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();


        allpictures = getAllImagesByFolder(folderPath);
        recyclerView.setAdapter(new PhotoAdapter(allpictures, FolderActivity.this));
    }

    public ArrayList<Photo> getAllImagesByFolder(String path) {
        ArrayList<Photo> images = new ArrayList<>();
        Uri allVideosuri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};
        Cursor cursor = FolderActivity.this.getContentResolver().query(allVideosuri, projection, MediaStore.Images.Media.DATA + " like ? ", new String[]{"%" + path + "%"}, null);
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
            ArrayList<Photo> reSelection = new ArrayList<>();
            for (int i = images.size() - 1; i > -1; i--) {
                reSelection.add(images.get(i));
            }
            images = reSelection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return images;
    }

    @Override
    public void onPicClicked(PhotoAdapter.PicHolder holder, int position, ArrayList<Photo> pics) {

    }

    @Override
    public void onPicClicked(String pictureFolderPath, String folderName) {

    }
}