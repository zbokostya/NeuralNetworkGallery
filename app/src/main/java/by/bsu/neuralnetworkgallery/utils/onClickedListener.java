package by.bsu.neuralnetworkgallery.utils;

import java.util.ArrayList;

import by.bsu.neuralnetworkgallery.adapter.PhotoAdapter;
import by.bsu.neuralnetworkgallery.entity.Photo;

public interface onClickedListener {
    void onPicClicked(PhotoAdapter.PicHolder holder, int position, ArrayList<Photo> pics);
    void onPicClicked(String pictureFolderPath,String folderName);
}
