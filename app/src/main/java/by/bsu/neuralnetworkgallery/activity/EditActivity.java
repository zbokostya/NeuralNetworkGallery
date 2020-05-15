package by.bsu.neuralnetworkgallery.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import by.bsu.neuralnetworkgallery.MainActivity;
import by.bsu.neuralnetworkgallery.R;
import by.bsu.neuralnetworkgallery.adapter.StyleAdapter;
import by.bsu.neuralnetworkgallery.dao.ServerConnector;
import by.bsu.neuralnetworkgallery.dao.StyleReader;
import by.bsu.neuralnetworkgallery.entity.Style;

public class EditActivity extends Activity implements StyleAdapter.ItemClickListener {

    Bitmap bitmap;
    ImageView image;
    RecyclerView recyclerView;
    ArrayList<Style> styles;
    StyleReader reader = new StyleReader();
    String id_post = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Button choose = findViewById(R.id.choose);
        image = findViewById(R.id.imageView);
        Button change = findViewById(R.id.change);
        recyclerView = findViewById(R.id.recycler_styles);
        init();


        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerConnector connection = new ServerConnector(getApplicationContext(), image);
                connection.postImage(id_post);


            }
        });
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 111);
            }
        });
    }



    private void init(){
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                StyleAdapter adapter = new StyleAdapter(getApplicationContext(), styles, (ImageView) findViewById(R.id.expanded_image_view), (ConstraintLayout) findViewById(R.id.container));
                adapter.setClickListener(EditActivity.this);
                recyclerView.setAdapter(adapter);
            }
        };
        reader.read(getApplicationContext());
        Runnable runnable = new Runnable() {
            public void run() {
                while (!reader.isReady()) {
                    synchronized (this) {
                        try {
                            wait(1000);
                        } catch (Exception e) {
                        }
                    }
                }
                styles = reader.getStyles();
                handler.sendEmptyMessage(0);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 111 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                //getting image from gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //Setting image to ImageView
                image.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onItemClick(View view, int position) {
        id_post = styles.get(position).getId();
        Toast.makeText(getApplicationContext(), styles.get(position).getTitle() + styles.get(position).getId(), Toast.LENGTH_SHORT).show();
    }


}
