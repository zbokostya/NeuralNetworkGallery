package by.bsu.neuralnetworkgallery.entity;

import java.util.ArrayList;

public class Folder {
    String folderPath;
    String folderName;
    String firstPicturePath;
    int numberOfPictures = 0;

    public Folder() {
    }

    public Folder(String folderPath, String folderName) {
        this.folderPath = folderPath;
        this.folderName = folderName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFirstPicturePath() {
        return firstPicturePath;
    }

    public void setFirstPicturePath(String firstPicturePath) {
        this.firstPicturePath = firstPicturePath;
    }

    public int getNumberOfPictures() {
        return numberOfPictures;
    }
    public void addPicures() {
        numberOfPictures++;
    }

    public void setNumberOfPictures(int numberOfPictures) {
        this.numberOfPictures = numberOfPictures;
    }
}
