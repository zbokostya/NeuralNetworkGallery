package by.bsu.neuralnetworkgallery.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import by.bsu.neuralnetworkgallery.R;
import by.bsu.neuralnetworkgallery.activity.EditActivity;
import by.bsu.neuralnetworkgallery.entity.Photo;

import static androidx.core.view.ViewCompat.setTransitionName;

public class PhotoFragment extends Fragment {

    private ArrayList<Photo> allImages = new ArrayList<>();
    private int position;
    private Context animeContx;
    private ImageView image;
    private ImageButton share;
    private ViewPager imagePager;
    private ImagesPagerAdapter pagingImages;
    private int previousSelected = -1;
    private boolean visibleShare = false;

    public PhotoFragment() {

    }

    public PhotoFragment(ArrayList<Photo> allImages, int imagePosition, Context anim) {
        this.allImages = allImages;
        this.position = imagePosition;
        this.animeContx = anim;
    }

    public static PhotoFragment newInstance(ArrayList<Photo> allImages, int imagePosition, Context anim) {
        PhotoFragment fragment = new PhotoFragment(allImages, imagePosition, anim);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.picture_browser, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /**
         * setting up the viewPager with images
         */
        imagePager = view.findViewById(R.id.imagePager);
        pagingImages = new ImagesPagerAdapter();
        imagePager.setAdapter(pagingImages);
        imagePager.setOffscreenPageLimit(3);
        imagePager.setCurrentItem(position);//displaying the image at the current position passed by the ImageDisplay Activity

        share = view.findViewById(R.id.share);
        /**
         * setting up the recycler view indicator for the viewPager
         */

        allImages.get(position).setSelected(true);
        previousSelected = position;

        /**
         * this listener controls the visibility of the recyclerView
         * indication and it current position in respect to the image ViewPager
         */
        imagePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (previousSelected != -1) {
                    allImages.get(previousSelected).setSelected(false);
                    previousSelected = position;
                    allImages.get(position).setSelected(true);

                } else {
                    previousSelected = position;
                    allImages.get(position).setSelected(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.fromFile(new File(allImages.get(previousSelected).getPicturePath()));
                Log.d("file", uri.toString());
                Intent move = new Intent(animeContx, EditActivity.class);
                move.putExtra("image_path", uri.toString());
                startActivity(move);
            }
        });



    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    private class ImagesPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return allImages.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup containerCollection, int position) {
            LayoutInflater layoutinflater = (LayoutInflater) containerCollection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutinflater.inflate(R.layout.picture_browser_pager, null);
            image = view.findViewById(R.id.image);
           // textView = view.findViewById(R.id.textBrowser);

            setTransitionName(image, String.valueOf(position) + "picture");

            Photo pic = allImages.get(position);
            Glide  .with(animeContx)
                    .load(pic.getPicturePath())
                    .fitCenter()
                    .into(image);

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(visibleShare) {
                        share.setVisibility(View.GONE);
                        visibleShare = false;
                    } else {
                        share.setVisibility(View.VISIBLE);
                        visibleShare = true;
                    }
                    Log.d("Cliker", "Clicked123");
                }
            });



            ((ViewPager) containerCollection).addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup containerCollection, int position, Object view) {
            ((ViewPager) containerCollection).removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == ((View) object);
        }
    }
}