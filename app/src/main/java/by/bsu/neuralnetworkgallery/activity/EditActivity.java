package by.bsu.neuralnetworkgallery.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

import by.bsu.neuralnetworkgallery.R;
import by.bsu.neuralnetworkgallery.adapter.StyleAdapter;
import by.bsu.neuralnetworkgallery.dao.ImageWriter;
import by.bsu.neuralnetworkgallery.dao.ServerConnector;
import by.bsu.neuralnetworkgallery.dao.StyleReader;
import by.bsu.neuralnetworkgallery.entity.Style;

public class EditActivity extends AppCompatActivity implements StyleAdapter.ItemClickListener {

    Bitmap bitmap;
    ServerConnector connection;
    Bitmap styleBitmap;
    String imageName;

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
        image = findViewById(R.id.imageView);
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(getIntent().getStringExtra("image_uri")));
            imageName = getIntent().getStringExtra("image_name");
            image.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Button change = findViewById(R.id.change);
        recyclerView = findViewById(R.id.recycler_styles);
        progressBar = findViewById(R.id.progressBar);
        connection = new ServerConnector(getApplicationContext(), image);

        init();
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRequest();
            }
        });

    }


    private void performRequest() {
        progressBar.setVisibility(View.VISIBLE);
        if (id_post.isEmpty())
            Toast.makeText(getApplicationContext(), "Select style first!", Toast.LENGTH_LONG).show();
        else if (inProgress)
            Toast.makeText(getApplicationContext(), "Wait!", Toast.LENGTH_LONG).show();
        else {
            final ServerConnector connection = new ServerConnector(getApplicationContext(), image);
            inProgress = true;
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
                    String path = writer.writeFile(bitmap, EditActivity.this);
                    Intent move = new Intent(EditActivity.this, FolderActivity.class);
                    move.putExtra("folderPath", path);
                    move.putExtra("folderName", "Gallery");
                    startActivity(move);
                }
            };
            if (Integer.parseInt(id_post) == 0) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String origName = imageName;
                        connection.postImageByStyleBitmap(styleBitmap, origName, imageName + "style.jpg");
                        long startTime = System.currentTimeMillis();
                        while (!connection.isImageChanged(origName)) {
                            if (System.currentTimeMillis() - startTime > 60000) break;
                            synchronized (this) {
                                try {
                                    wait(5000);
                                } catch (Exception e) {
                                }
                            }
                        }
                        connection.getImage(origName);
                        startTime = System.currentTimeMillis();
                        while (!connection.isReady()) {
                            if (System.currentTimeMillis() - startTime > 60000) break;
                            synchronized (this) {
                                try {
                                    wait(5000);
                                } catch (Exception e) {
                                }
                            }
                        }

                        bitmap = connection.getBitmap();
//                    image.setImageBitmap(connection.getBitmap());
                        handler.sendEmptyMessage(0);
                    }
                });
                thread.start();
            } else {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        connection.postImageByStyleId(imageName, Integer.parseInt(id_post));
                        long startTime = System.currentTimeMillis();
                        while (!connection.isImageChanged(imageName)) {
                            if (System.currentTimeMillis() - startTime > 60000) break;
                            synchronized (this) {
                                try {
                                    wait(5000);
                                } catch (Exception e) {
                                }
                            }
                        }
                        connection.getImage(imageName);
                        startTime = System.currentTimeMillis();
                        while (!connection.isReady()) {
                            if (System.currentTimeMillis() - startTime > 60000) break;
                            synchronized (this) {
                                try {
                                    wait(5000);
                                } catch (Exception e) {
                                }
                            }
                        }

                        bitmap = connection.getBitmap();
//                    image.setImageBitmap(connection.getBitmap());
                        handler.sendEmptyMessage(0);
                    }
                });
                thread.start();
//                final Handler handler = new Handler() {
//                    @SuppressLint("ResourceType")
//                    @Override
//                    public void handleMessage(Message msg) {
//                        id_post = "";
//                        progressBar.setVisibility(View.INVISIBLE);
//                        image.setImageBitmap(bitmap);
//                        inProgress = false;
//                        isItemSelected = false;
//                        previouslySelected.setBackgroundResource(R.layout.border);
//                        previouslySelected = null;
//                        ImageWriter writer = new ImageWriter();
//                        writer.writeFile(bitmap);
//                        Toast.makeText(getApplicationContext(), "Successfully saved to " + Environment.getExternalStorageDirectory() + "/Pictures/Gallery/", Toast.LENGTH_LONG).show();
//                    }
//                };
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        connection.postImage();
//                        while (!connection.isReady()) {
//                            synchronized (this) {
//                                try {
//                                    wait(1000);
//                                } catch (Exception e) {
//                                }
//                            }
//                        }
//                        bitmap = connection.result();
//                        handler.sendEmptyMessage(0);
//                    }
//                });
//
//                thread.start();
            }
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


    @SuppressLint("ResourceType")
    @Override
    public void onItemClick(View view, int position) {
        if (position == 0) {
            Intent pickPhoto = new Intent(Intent.ACTION_GET_CONTENT);
            pickPhoto.setType("image/*");
            startActivityForResult(Intent.createChooser(pickPhoto, "Select Picture"), 1);
        }
        id_post = styles.get(position).getId();
        view.setBackgroundResource(R.layout.border_selected);
        if (!isItemSelected) {
            isItemSelected = true;
        } else
            previouslySelected.setBackgroundResource(R.layout.border);
        previouslySelected = view;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 1) {
                Uri selectedImageUri = data.getData();
                try {
                    styleBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
