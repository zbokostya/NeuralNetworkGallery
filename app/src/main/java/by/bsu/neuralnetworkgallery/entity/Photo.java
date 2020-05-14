package by.bsu.neuralnetworkgallery.entity;

import android.app.Activity;
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
    private Uri mUrl;
    private String mTitle;

    public Photo(Uri url, String title) {
        mUrl = url;
        mTitle = title;
    }

    protected Photo(Parcel in) {
        mUrl = (Uri)in.readValue(Uri.class.getClassLoader());
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

    public Uri getUrl() {
        return mUrl;
    }

    public void setUrl(Uri url) {
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

    static public Photo[] getFromSDCard(Activity activity) {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor;
        int column_index_id;
        //ArrayList<Uri> listOfAllImages = new ArrayList<>();
        ArrayList<Photo> listPhotos = new ArrayList<>();
        Long imageId ;
        String[] projection = {MediaStore.MediaColumns._ID};

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);
        if (cursor != null) {
            column_index_id = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            while (cursor.moveToNext()) {
                imageId = cursor.getLong(column_index_id);
                Uri uriImage = Uri.withAppendedPath(uri, "" + imageId);
                listPhotos.add(new Photo(uriImage, ""));
            }
            cursor.close();
        }
        Photo[] rez = new Photo[listPhotos.size()];
        rez = listPhotos.toArray(rez);
        Log.d("1234", rez[0].getUrl().toString());
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
        parcel.writeValue(mUrl);
        parcel.writeString(mTitle);
    }
}
