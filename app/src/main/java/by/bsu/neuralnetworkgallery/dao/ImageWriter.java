package by.bsu.neuralnetworkgallery.dao;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Calendar;

public class ImageWriter {

    public void writeFile(final Bitmap bitmap) {
        File file = Environment.getExternalStorageDirectory();
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        final String fileName = "IMG_" + year + "" + month + "" + day + "_" + hour + "" + minute + "" + second + ".png";
        try {
            File dir = new File(file + "/Pictures/NeuralNetworkGallery/");
            dir.mkdirs();
            File write = new File(dir, fileName);
            FileOutputStream out = new FileOutputStream(write);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}