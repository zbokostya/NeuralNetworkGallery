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

public class Photo implements Parcelable {
    private String mUrl;
    private String mTitle;

    public Photo(String url, String title) {
        mUrl = url;
        mTitle = title;
    }

    protected Photo(Parcel in) {
        mUrl = in.readString();
        mTitle = in.readString();
    }

    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

//    public static Photo[] getSpacePhotos() {
//
//        return new Photo[]{
//                new Photo("http://i.imgur.com/zuG2bGQ.jpg", "Galaxy"),
//                new Photo("http://i.imgur.com/ovr0NAF.jpg", "Space Shuttle"),
//                new Photo("http://i.imgur.com/n6RfJX2.jpg", "Galaxy Orion"),
//                new Photo("http://i.imgur.com/qpr5LR2.jpg", "Earth"),
//                new Photo("http://i.imgur.com/pSHXfu5.jpg", "Astronaut"),
//                new Photo("http://i.imgur.com/3wQcZeY.jpg", "Satellite"),
//        };
//    }

    /*static public Photo[] getFromSDCard(Activity activity) {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor;
        int column_index_id, column_index_name;
        ArrayList<Photo> listPhotos = new ArrayList<>();
        Long imageId;
        String imageName;
        String[] projection = {
                MediaStore.MediaColumns._ID,
                MediaStore.MediaColumns.MIME_TYPE
        };


        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);


        if (cursor != null) {
            column_index_id = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            column_index_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE);
            while (cursor.moveToNext()) {
                imageId = cursor.getLong(column_index_id);
                imageName = cursor.getString(column_index_name);
//                Uri uriImageName = Uri.withAppendedPath(uri, "" + imageName);
                Uri uriImage = Uri.withAppendedPath(uri, ""+imageId);

                Log.d("123456768790", imageId+"");
                Log.d("123456768790", imageName);

                listPhotos.add(new Photo(uriImage, ""));
            }

            cursor.close();
        }
        Photo[] rez = new Photo[listPhotos.size()];
        rez = listPhotos.toArray(rez);
        // test
//        Log.d("1234", rez[0].getUrl().getPath());
//        Log.d("1234", Uri.fromFile(new File("/storage/1B13-080C/DCIM/1.jpg")).toString());
        return rez;
    }*/
    public static Photo[] getFromSDCard(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage;
        String absoluteFolderOfImage;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            absoluteFolderOfImage = cursor.getString(column_index_folder_name);
            listOfAllImages.add(absolutePathOfImage);
        }
        Photo[] rez = new Photo[listOfAllImages.size()];
        for(int i = 0; i < listOfAllImages.size(); i++) {
            rez[i] = new Photo(listOfAllImages.get(i), "");
        }
        Log.d("123", rez[0].getUrl());
        return rez;
    }

//    public static ArrayList<String> getImagesPath(Activity activity) {
//        Uri uri;
//        ArrayList<String> listOfAllImages = new ArrayList<String>();
//        Cursor cursor;
//        int column_index_data, column_index_folder_name;
//        String PathOfImage = null;
//        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//
//        String[] projection = { MediaStore.MediaColumns.DATA,
//                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };
//
//        cursor = getApplicationContext().getContentResolver().query(
//                MediaStore.media-type.Media.EXTERNAL_CONTENT_URI,
//                projection,
//                selection,
//                selectionArgs,
//                sortOrder
//        );
//
//        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//        column_index_folder_name = cursor
//                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
//        while (cursor.moveToNext()) {
//            PathOfImage = cursor.getString(column_index_data);
//
//            listOfAllImages.add(PathOfImage);
//        }
//        return listOfAllImages;
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mUrl);
        parcel.writeString(mTitle);
    }
}
