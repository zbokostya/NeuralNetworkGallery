package by.bsu.neuralnetworkgallery.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Stack;

import by.bsu.neuralnetworkgallery.R;
import by.bsu.neuralnetworkgallery.values.ImageChangeMode;
import by.bsu.neuralnetworkgallery.utils.ImageScaleListener;
import by.bsu.neuralnetworkgallery.values.PaperPrintFormat;

public class ImageEditActivity extends Activity implements View.OnDragListener, View.OnTouchListener, View.OnClickListener  {

    private TextView textView;
    private Button button2;
    private ImageChangeMode mode = ImageChangeMode.IMAGE_SELECT;
    private Integer selectedElement = null;
    private final Stack<ImageView> operatedViews = new Stack<>();

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        textView = findViewById(R.id.imageEditTextView);
        TextView textView2 = findViewById(R.id.imageEditTextView2);
        TextView textView3 = findViewById(R.id.imageEditTextView3);
        TextView textView4 = findViewById(R.id.imageEditTextView4);
        TextView textView5 = findViewById(R.id.imageEditTextView5);

        ConstraintLayout layout = findViewById(R.id.imageEditLayout);
        ImageView imageView = findViewById(R.id.imageEditImageView);
        try {
            String uri = getIntent().getStringExtra("image_uri");
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(getIntent().getStringExtra("image_uri")));
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setBackgroundResource(R.layout.image_border_selected);
        Button button = findViewById(R.id.imageEditButton);
        button2 = findViewById(R.id.imageEditButton2);

        textView.setOnClickListener(this);
        textView2.setOnClickListener(this);
        textView3.setOnClickListener(this);
        textView4.setOnClickListener(this);
        textView5.setOnClickListener(this);
        layout.setOnDragListener(this);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);

        createField(getIntent().getIntExtra("list_width", PaperPrintFormat.A4_WIDTH), getIntent().getIntExtra("list_height", PaperPrintFormat.A4_HEIGHT));
        operatedViews.push(imageView);
        initImageViews();
    }

    private void createField(int X, int Y){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        int y_times = (int) Math.round(0.9 * height / Y);
        int x_times = (int) Math.round(0.9 * width / X);
        int times = Math.min(x_times, y_times);
        int finalX = times * X;
        int finalY = times * Y;
        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        layoutParams.width = finalX;
        layoutParams.height = finalY;
        textView.setLayoutParams(layoutParams);
    }

    public void initImageViews() {
        for (ImageView view : operatedViews) {
            view.setOnClickListener(this);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            int viewWidth = view.getDrawable().getMinimumWidth();
            int viewHeight = view.getDrawable().getMinimumHeight();

            int width1 = (int) (textView.getLayoutParams().width * 0.8);
            int height1 = (int) (1.0 * viewHeight / viewWidth * width1);
            if (height1 >= textView.getLayoutParams().height) {
                int height2 = (int) (textView.getLayoutParams().height * 0.8);
                int width2 = (int) (1.0 * viewWidth / viewHeight * height2);
                layoutParams.height = height2;
                layoutParams.width = width2;
            } else {
                layoutParams.height = height1;
                layoutParams.width = width1;
            }
            view.setLayoutParams(layoutParams);
        }
    }

    public void saveResult() {
        final int w = textView.getWidth() / 2;
        final int h = textView.getHeight() / 2;
        final int x = (int) textView.getX() / 2;
        final int y = (int) textView.getY() / 2;

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(w, h, conf);


        for (int ii = 0; ii < w; ii++)
            for (int jj = 0; jj < h; jj++)
                bitmap.setPixel(ii, jj, Color.argb(255, 255, 255, 255));

        for (ImageView view : operatedViews)
            writeBitmap(view, bitmap, w, h, x, y);

        writeFile(bitmap);

    }

    public Bitmap writeBitmap(ImageView from, Bitmap to, int w, int h, int x, int y) {
        final int w1 = (from.getWidth() - 2 * from.getPaddingLeft()) / 2;
        final int h1 = (from.getHeight() - 2 * from.getPaddingTop()) / 2;
        final int x1 = (int) ((from.getX() + from.getPaddingLeft()) / 2);
        final int y1 = (int) (from.getY() + from.getPaddingTop()) / 2;

        int a, b, c, d, i, k;

        if (x >= x1) {
            a = x - x1;
            i = 0;
        } else {
            a = 0;
            i = x1 - x;
        }
        if (y >= y1) {
            c = y - y1;
            k = 0;
        } else {
            c = 0;
            k = y1 - y;
        }
        if (x1 + w1 >= x + w)
            b = w + x - x1;
        else
            b = w1;
        if (y1 + h1 >= y + h)
            d = h + y - y1;
        else
            d = h1;

        BitmapDrawable drawable = (BitmapDrawable) from.getDrawable();
        Bitmap imageBitmap = drawable.getBitmap();

        Bitmap copy = Bitmap.createScaledBitmap(imageBitmap, w1, h1, true);

        for (int ii = 0; ii < b - a; ii++)
            for (int jj = 0; jj < d - c; jj++)
                to.setPixel(i + ii, k + jj, copy.getPixel(a + ii, c + jj));
        return to;
    }

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
            File dir = new File(file + "/Pictures/Gallery/");
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

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_ENDED:
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                return true;
            case DragEvent.ACTION_DRAG_ENTERED:
                return true;
            case DragEvent.ACTION_DRAG_STARTED:
                return true;
            case DragEvent.ACTION_DROP:
                View tvState = (View) event.getLocalState();
                ViewGroup tvParent = (ViewGroup) tvState.getParent();
                tvParent.removeView(tvState);
                ConstraintLayout container = (ConstraintLayout) v;
                container.addView(tvState);
                tvParent.removeView(tvState);
                tvState.setX(event.getX() - (tvState.getWidth() / 2));
                tvState.setY(event.getY() - (tvState.getHeight() / 2));
                ((ConstraintLayout) v).addView(tvState);
                v.setVisibility(View.VISIBLE);
                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                return true;
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View.DragShadowBuilder builder = new View.DragShadowBuilder(v);
            v.startDragAndDrop(null, builder, v, 0);
            return true;
        }
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageEditButton:
                saveResult();
                break;
            case R.id.imageEditTextView:
            case R.id.imageEditTextView2:
            case R.id.imageEditTextView3:
            case R.id.imageEditTextView4:
            case R.id.imageEditTextView5:
                if (selectedElement != null) {
                    findViewById(selectedElement).setPadding(0, 0, 0, 0);
                    if (mode != ImageChangeMode.IMAGE_SELECT)
                        findViewById(selectedElement).setOnTouchListener(null);
                    selectedElement = null;
                    for (View operatedView : operatedViews) {
                        operatedView.setOnTouchListener(null);
                        operatedView.setOnClickListener(this);
                    }
                    button2.setVisibility(View.GONE);
                    Toast.makeText(this, "Select mode ON", Toast.LENGTH_SHORT).show();
                    mode = ImageChangeMode.IMAGE_SELECT;
                }
                break;
            case R.id.imageEditButton2:
                if (mode == ImageChangeMode.IMAGE_DRAG) {
                    button2.setText("Scale");
                    button2.setBackgroundColor(Color.GREEN);
                    mode = ImageChangeMode.IMAGE_SCALE;
                    Toast.makeText(this, "Scale mode ON", Toast.LENGTH_SHORT).show();

                    textView.setOnClickListener(null);
                    findViewById(selectedElement).setOnTouchListener(null);
                    ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(this, new ImageScaleListener(findViewById(selectedElement)));
                    textView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            scaleGestureDetector.onTouchEvent(event);
                            return true;
                        }
                    });

                } else if (mode == ImageChangeMode.IMAGE_SCALE) {
                    textView.setOnTouchListener(null);
                    textView.setOnClickListener(this);
                    findViewById(selectedElement).setOnTouchListener(this);
                    button2.setText("Drag");
                    button2.setBackgroundColor(Color.BLUE);
                    mode = ImageChangeMode.IMAGE_DRAG;
                    Toast.makeText(this, "Drag mode ON", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                if (operatedViews.contains(v)) {
                    if (selectedElement != null) {
                        findViewById(selectedElement).setPadding(0, 0, 0, 0);
                    }
                    textView.setOnTouchListener(null);
                    textView.setOnClickListener(this);
                    v.setPadding(1, 1, 1, 1);
                    selectedElement = v.getId();
                    operatedViews.remove(v);
                    operatedViews.push((ImageView) v);
                    for (View operatedView : operatedViews) {
                        operatedView.setOnTouchListener(null);
                        operatedView.setOnClickListener(this);
                    }
                    findViewById(selectedElement).setOnClickListener(null);
                    findViewById(selectedElement).setOnTouchListener(this);

                    button2.setVisibility(View.VISIBLE);
                    mode = ImageChangeMode.IMAGE_DRAG;
                    button2.setText("Drag");
                    button2.setBackgroundColor(Color.BLUE);
                    Toast.makeText(this, "Drag mode ON", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
