package by.bsu.neuralnetworkgallery.adapter;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import by.bsu.neuralnetworkgallery.R;
import by.bsu.neuralnetworkgallery.entity.Folder;
import by.bsu.neuralnetworkgallery.utils.onClickedListener;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


import java.util.ArrayList;

// gives folders
public class PhotoFolderAdapter extends RecyclerView.Adapter<PhotoFolderAdapter.FolderHolder> {

    private ArrayList<Folder> folders;
    private Context folderContx;
    private onClickedListener clickedListener;

    public PhotoFolderAdapter(ArrayList<Folder> folders, Context folderContx, onClickedListener onClickedListener) {
        this.folders = folders;
        this.folderContx = folderContx;
        this.clickedListener = onClickedListener;
    }

    @NonNull
    @Override
    public FolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cell = inflater.inflate(R.layout.folder_item, parent, false);
        return new FolderHolder(cell);

    }

    @Override
    public void onBindViewHolder(@NonNull FolderHolder holder, int position) {
        final Folder folder = folders.get(position);

        Glide.with(folderContx)
                .load(folder.getFirstPicturePath())
                .centerCrop()
                .into(holder.folderPic);

        String text = "(" + folder.getNumberOfPictures() + ") " + folder.getFolderName();
        holder.folderName.setText(text);
        DisplayMetrics metrics = new DisplayMetrics();
        ((AppCompatActivity)holder.folderCard.getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int widthCount = metrics.widthPixels / 240;
        holder.folderCard.getLayoutParams().height= metrics.widthPixels / widthCount;
        holder.folderCard.getLayoutParams().width= metrics.widthPixels / widthCount;
        holder.folderPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedListener.onPicClicked(folder.getFolderPath(), folder.getFolderName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return folders.size();
    }


    public class FolderHolder extends RecyclerView.ViewHolder {
        ImageView folderPic;
        TextView folderName;
        CardView folderCard;
        RelativeLayout folderLayout;

        public FolderHolder(@NonNull View itemView) {
            super(itemView);
            folderPic = itemView.findViewById(R.id.folderPic);
            folderName = itemView.findViewById(R.id.folderName);
            folderCard = itemView.findViewById(R.id.folderCard);
            folderLayout = itemView.findViewById(R.id.folderLayout);
        }
    }

}