package by.bsu.neuralnetworkgallery.adapter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import by.bsu.neuralnetworkgallery.R;
import by.bsu.neuralnetworkgallery.utils.onClickedListener;

public class BottomCarouselAdapter extends RecyclerView.Adapter<BottomCarouselAdapter.SettingsHolder> {

    private int[] allPhotos;
    private AppCompatActivity carouselContext;
    private onClickedListener onClickedListener;

    public BottomCarouselAdapter(int[] allPhotos, AppCompatActivity carouselContext, onClickedListener onClickedListener) {
        this.allPhotos = allPhotos;
        this.carouselContext = carouselContext;
        this.onClickedListener = onClickedListener;
    }

    @NonNull
    @Override
    public SettingsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cell = inflater.inflate(R.layout.settings_item, parent, false);
        return new SettingsHolder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsHolder holder, int position) {
        holder.settingPic.setImageResource(allPhotos[position]);
        holder.settingName.setText(position+"PIDOR");
        DisplayMetrics metrics = new DisplayMetrics();
        carouselContext.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int widthCount = metrics.widthPixels / 160;
        holder.settingCard.getLayoutParams().height= metrics.widthPixels / widthCount;
        holder.settingCard.getLayoutParams().width= metrics.widthPixels / widthCount;
        holder.settingPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickedListener.onPicClicked(position+"PIDOR");
            }
        });
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class SettingsHolder extends RecyclerView.ViewHolder {
        ImageView settingPic;
        TextView settingName;
        CardView settingCard;
        RelativeLayout settingLayout;

        public SettingsHolder(@NonNull View itemView) {
            super(itemView);
            settingPic = itemView.findViewById(R.id.settingPic);
            settingName = itemView.findViewById(R.id.settingName);
            settingCard = itemView.findViewById(R.id.settingCard);
            settingLayout = itemView.findViewById(R.id.settingLayout);
        }
    }
}
