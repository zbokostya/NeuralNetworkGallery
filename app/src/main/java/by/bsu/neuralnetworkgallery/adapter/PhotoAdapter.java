package by.bsu.neuralnetworkgallery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import by.bsu.neuralnetworkgallery.R;
import by.bsu.neuralnetworkgallery.entity.Photo;
import by.bsu.neuralnetworkgallery.utils.onClickedListener;

import static androidx.core.view.ViewCompat.setTransitionName;


//gives images in folder

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PicHolder> {

    private ArrayList<Photo> pictureList;
    private Context pictureContx;
    private onClickedListener clickedListener;

    public PhotoAdapter(ArrayList<Photo> pictureList, Context pictureContx, onClickedListener clickedListener) {
        this.pictureList = pictureList;
        this.pictureContx = pictureContx;
        this.clickedListener = clickedListener;
    }

    @NonNull
    @Override
    public PicHolder onCreateViewHolder(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View cell = inflater.inflate(R.layout.picture_layout, container, false);
        return new PicHolder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull final PicHolder holder, final int position) {

        final Photo image = pictureList.get(position);

        Glide.with(pictureContx)
                .load(image.getPicturePath())
                .centerCrop()
                .into(holder.picture);
        setTransitionName(holder.picture, String.valueOf(position) + "_image");
        holder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedListener.onPicClicked(holder, position, pictureList);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }

    public class PicHolder extends RecyclerView.ViewHolder {

        public ImageView picture;

        PicHolder(@NonNull View itemView) {
            super(itemView);

            picture = itemView.findViewById(R.id.image);
        }
    }
}