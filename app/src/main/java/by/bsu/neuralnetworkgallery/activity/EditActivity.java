package by.bsu.neuralnetworkgallery.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

import by.bsu.neuralnetworkgallery.MainActivity;
import by.bsu.neuralnetworkgallery.R;
import by.bsu.neuralnetworkgallery.adapter.StyleAdapter;
import by.bsu.neuralnetworkgallery.dao.ImageWriter;
import by.bsu.neuralnetworkgallery.dao.ServerConnector;
import by.bsu.neuralnetworkgallery.dao.StyleReader;
import by.bsu.neuralnetworkgallery.entity.Style;

public class EditActivity extends AppCompatActivity implements StyleAdapter.ItemClickListener {

    Bitmap bitmap;
    ImageView image;
    RecyclerView recyclerView;
    ArrayList<Style> styles;
    StyleReader reader = new StyleReader();
    String id_post = "";
    View previouslySelected;
    ProgressBar progressBar;
    boolean isItemSelected = false;
    boolean inProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit photo");
        Button choose = findViewById(R.id.choose);
        image = findViewById(R.id.imageView);
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(getIntent().getStringExtra("image_path")));
            image.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Button change = findViewById(R.id.change);
        recyclerView = findViewById(R.id.recycler_styles);
        progressBar = findViewById(R.id.progressBar);
        init();
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRequest();
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


    private void performRequest(){
        progressBar.setVisibility(View.VISIBLE);
        if (id_post.isEmpty())
            Toast.makeText(getApplicationContext(), "Select style first!", Toast.LENGTH_LONG).show();
        else if(inProgress)
            Toast.makeText(getApplicationContext(), "Wait!", Toast.LENGTH_LONG).show();
        else {
            inProgress = true;
            final ServerConnector connection = new ServerConnector(getApplicationContext(), image);

            final Handler handler = new Handler() {
                @SuppressLint("ResourceType")
                @Override
                public void handleMessage(Message msg) {
                    id_post = "";
                    progressBar.setVisibility(View.INVISIBLE);
                    image.setImageBitmap(bitmap);
                    inProgress = false;
                    isItemSelected = false;
                    previouslySelected.setBackgroundResource(R.layout.border);
                    previouslySelected = null;
                    ImageWriter writer = new ImageWriter();
                    writer.writeFile(bitmap);
                }
            };
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    connection.postImage(id_post);
                    while (!connection.isReady()) {
                        synchronized (this) {
                            try {
                                wait(1000);
                            } catch (Exception e) {
                            }
                        }
                    }
                    bitmap = connection.result();
                    handler.sendEmptyMessage(0);
                }
            });
            thread.start();
        }
    }

    private void init() {
        progressBar.setVisibility(View.VISIBLE);
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                StyleAdapter adapter = new StyleAdapter(getApplicationContext(), styles, (ImageView) findViewById(R.id.expanded_image_view), (ConstraintLayout) findViewById(R.id.container));
                adapter.setClickListener(EditActivity.this);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);
            }
        };
        Runnable runnable = new Runnable() {
            public void run() {
                reader.read(getApplicationContext());
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
            Toast.makeText(getApplicationContext(), filePath.toString(), Toast.LENGTH_LONG).show();
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


    @SuppressLint("ResourceType")
    @Override
    public void onItemClick(View view, int position) {
        id_post = styles.get(position).getId();
        view.setBackgroundResource(R.layout.border_selected);
        if (!isItemSelected) {
            isItemSelected = true;
        } else
            previouslySelected.setBackgroundResource(R.layout.border);
        previouslySelected = view;
    }

}
