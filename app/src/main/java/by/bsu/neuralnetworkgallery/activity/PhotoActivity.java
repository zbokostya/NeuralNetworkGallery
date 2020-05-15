package by.bsu.neuralnetworkgallery.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import by.bsu.neuralnetworkgallery.R;
import by.bsu.neuralnetworkgallery.entity.Photo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;


public class PhotoActivity extends Activity {

    public static final String EXTRA_SPACE_PHOTO = "SpacePhotoActivity.SPACE_PHOTO";

    private ImageView mImageView;
    String imagePath;
   // String
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        mImageView = (ImageView) findViewById(R.id.image);
        //Photo spacePhoto =

        /*Glide.with(this)
                .load(spacePhoto.getUrl())
                .asBitmap()
                .error(R.drawable.ic_cloud_off_red)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(mImageView);

*/
    }
}
