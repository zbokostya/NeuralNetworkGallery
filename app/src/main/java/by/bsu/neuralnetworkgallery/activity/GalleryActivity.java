package by.bsu.neuralnetworkgallery.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import by.bsu.neuralnetworkgallery.R;
import by.bsu.neuralnetworkgallery.adapter.PhotoAdapter;
import by.bsu.neuralnetworkgallery.adapter.PhotoFolderAdapter;
import by.bsu.neuralnetworkgallery.entity.Folder;
import by.bsu.neuralnetworkgallery.entity.Photo;
import by.bsu.neuralnetworkgallery.utils.onClickedListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity implements onClickedListener {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        recyclerView = findViewById(R.id.folderRecycler);
        //recyclerView.addItemDecoration(new ViewGroup.MarginLayoutParams(this));
        recyclerView.hasFixedSize();
        ArrayList<Folder> folderArrayList = getPicturePaths();
        RecyclerView.Adapter folderAdapter = new PhotoFolderAdapter(folderArrayList, GalleryActivity.this, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(folderAdapter);
    }


    private ArrayList<Folder> getPicturePaths() {
        ArrayList<Folder> picFolders = new ArrayList<>();
        ArrayList<String> picPaths = new ArrayList<>();
        Uri allImagesuri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID};
        Cursor cursor = this.getContentResolver().query(allImagesuri, projection, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
            }
            do {
                Folder folds = new Folder();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String datapath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

                //String folderpaths =  datapath.replace(name,"");
                String folderpaths = datapath.substring(0, datapath.lastIndexOf(folder + "/"));
                folderpaths = folderpaths + folder + "/";
                if (!picPaths.contains(folderpaths)) {
                    picPaths.add(folderpaths);

                    folds.setFolderPath(folderpaths);
                    folds.setFolderName(folder);
                    folds.setFirstPicturePath(datapath);//if the folder has only one picture this line helps to set it as first so as to avoid blank image in itemview
                    folds.addPicures();
                    picFolders.add(folds);
                } else {
                    for (int i = 0; i < picFolders.size(); i++) {
                        if (picFolders.get(i).getFolderPath().equals(folderpaths)) {
                            picFolders.get(i).setFirstPicturePath(datapath);
                            picFolders.get(i).addPicures();
                        }
                    }
                }
            } while (cursor.moveToNext());
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < picFolders.size(); i++) {
            Log.d("picture folders", picFolders.get(i).getFolderName() + " and path = " + picFolders.get(i).getFolderPath() + " " + picFolders.get(i).getNumberOfPictures());
        }
        return picFolders;
    }

    @Override
    public void onPicClicked(PhotoAdapter.PicHolder holder, int position, ArrayList<Photo> pics) {

    }

    @Override
    public void onPicClicked(String pictureFolderPath, String folderName) {
        Intent move = new Intent(GalleryActivity.this, FolderActivity.class);
        move.putExtra("folderPath", pictureFolderPath);
        move.putExtra("folderName", folderName);

        //move.putExtra("recyclerItemSize",getCardsOptimalWidth(4));
        startActivity(move);
    }
}
