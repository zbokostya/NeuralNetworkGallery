package by.bsu.neuralnetworkgallery.entity;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;

import android.net.Uri;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;

import java.io.Console;
import java.io.File;
import java.util.ArrayList;

public class Photo {
    private String pictureName;
    private String picturePath;
    private String pictureSize;
    private String imageUri;
    private Boolean selected = false;

    public Photo() {
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getPictureSize() {
        return pictureSize;
    }

    public void setPictureSize(String pictureSize) {
        this.pictureSize = pictureSize;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public static Photo[] getFromSDCard(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage;
        String absoluteFolderOfImage;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            absoluteFolderOfImage = cursor.getString(column_index_folder_name);
            Log.d("123", absoluteFolderOfImage);
            listOfAllImages.add(absolutePathOfImage);
        }
        Photo[] rez = new Photo[listOfAllImages.size()];
        for (int i = 0; i < listOfAllImages.size(); i++) {
           // rez[i] = new Photo(listOfAllImages.get(i), "");
        }
        //Log.d("123", rez[0].getUrl());
        return rez;
    }

}
