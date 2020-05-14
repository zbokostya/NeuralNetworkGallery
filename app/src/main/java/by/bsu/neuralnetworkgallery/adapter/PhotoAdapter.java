//package by.bsu.neuralnetworkgallery.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//
//import androidx.recyclerview.widget.RecyclerView;
//import by.bsu.neuralnetworkgallery.R;
//import by.bsu.neuralnetworkgallery.activity.PhotoActivity;
//import by.bsu.neuralnetworkgallery.entity.Photo;
//
//public class PhotoAdapter {
//    private class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder>  {
//
//        @Override
//        public ImageGalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//            Context context = parent.getContext();
//            LayoutInflater inflater = LayoutInflater.from(context);
//
//            // Inflate the layout
//            View photoView = inflater.inflate(R.layout.item_photo, parent, false);
//
//            ImageGalleryAdapter.MyViewHolder viewHolder = new ImageGalleryAdapter.MyViewHolder(photoView);
//            return viewHolder;
//        }
//
//        @Override
//        public void onBindViewHolder(ImageGalleryAdapter.MyViewHolder holder, int position) {
//
//            Photo photo = mPhotos[position];
//            ImageView imageView = holder.mPhotoImageView;
//
//            Glide.with(mContext)
//                    .load(photo.getUrl())
//                    .placeholder(R.drawable.ic_cloud_off_red)
//                    .into(imageView);
//        }
//
//        @Override
//        public int getItemCount() {
//            return (mPhotos.length);
//        }
//
//        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//
//            public ImageView mPhotoImageView;
//
//            public MyViewHolder(View itemView) {
//
//                super(itemView);
//                mPhotoImageView = (ImageView) itemView.findViewById(R.id.iv_photo);
//                itemView.setOnClickListener(this);
//            }
//
//            @Override
//            public void onClick(View view) {
//
//                int position = getAdapterPosition();
//                if(position != RecyclerView.NO_POSITION) {
//                    Photo spacePhoto = mPhotos[position];
//
//                    Intent intent = new Intent(mContext, PhotoActivity.class);
//                    intent.putExtra(PhotoActivity.EXTRA_SPACE_PHOTO, spacePhoto);
//                    startActivity();
//                }
//            }
//        }
//
//        private Photo[] mPhotos;
//        private Context mContext;
//
//        public ImageGalleryAdapter(Context context, Photo[] photos) {
//            mContext = context;
//            mPhotos = photos;
//        }
//    }
//}
