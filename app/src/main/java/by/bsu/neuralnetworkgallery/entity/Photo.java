package by.bsu.neuralnetworkgallery.entity;

import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

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

    public static Photo[] getSpacePhotos() {

        return new Photo[]{
                new Photo("http://i.imgur.com/zuG2bGQ.jpg", "Galaxy"),
                new Photo("http://i.imgur.com/ovr0NAF.jpg", "Space Shuttle"),
                new Photo("http://i.imgur.com/n6RfJX2.jpg", "Galaxy Orion"),
                new Photo("http://i.imgur.com/qpr5LR2.jpg", "Earth"),
                new Photo("http://i.imgur.com/pSHXfu5.jpg", "Astronaut"),
                new Photo("http://i.imgur.com/3wQcZeY.jpg", "Satellite"),
        };
    }

    public static Photo[] getFromSdcard() {
        ArrayList<Photo> f = new ArrayList<>();// list of file paths
        File[] listFile;
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString());

        if (file.isDirectory()) {
            listFile = file.listFiles();


            for (int i = 0; i < listFile.length; i++) {

                f.add(new Photo(listFile[i].getAbsolutePath(), "From file"));

            }
        }
        Photo[] photos = new Photo[f.size()];
        photos = f.toArray(photos);
        return photos;
    }

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
