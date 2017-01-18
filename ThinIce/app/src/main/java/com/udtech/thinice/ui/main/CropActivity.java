package com.udtech.thinice.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.theartofdev.edmodo.cropper.CropImageView;
import com.udtech.thinice.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by JOkolot on 27.04.2016.
 */
public class CropActivity extends Activity {
    static final int REQUEST_IMAGE_CAPTURE = 1, REQUEST_SELECT_FILE = 2;
    public static final String TAKE_PHOTO_FLAG = "take";
    public static final String CHOOSE_PHOTO_FLAG = "choose";
    private String pathToImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        findViewById(R.id.rotate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotate();
            }
        });
        ((CropImageView) findViewById(R.id.cropImageView)).setFixedAspectRatio(true);
        if (getIntent().getBooleanExtra(TAKE_PHOTO_FLAG, false)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        } else if (getIntent().getBooleanExtra(CHOOSE_PHOTO_FLAG, false)) {
            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(
                    Intent.createChooser(intent, "Select File"),
                    REQUEST_SELECT_FILE);
        }
        findViewById(R.id.btn_crop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((CropImageView) findViewById(R.id.cropImageView)).getCroppedImage();
                saveBitmap(cropByMask(bitmap));
                Intent intent = new Intent();
                intent.putExtra("path", saveBitmap(cropByMask(bitmap)));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    Bitmap bmp = null;

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                BitmapFactory.Options option = new BitmapFactory.Options();
                option.inPreferredConfig = Bitmap.Config.ARGB_8888;

                bmp = BitmapFactory.decodeFile(data.getData().getPath());
                if (bmp == null)
                    bmp = (Bitmap) data.getExtras().get("data");
                bmp = rotateImage(bmp, 90);
            } else if (requestCode == REQUEST_SELECT_FILE && resultCode == RESULT_OK) {
                Uri selectedImageUri = data.getData();
                try {
                    bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ((CropImageView) findViewById(R.id.cropImageView)).setImageBitmap(bmp);
        } else {
            finish();
        }
    }

    public void rotate() {
        bmp = rotateImage(bmp, 90);
        ((CropImageView) findViewById(R.id.cropImageView)).setImageBitmap(bmp);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        return retVal;
    }

    private String saveBitmap(Bitmap bmp) {
        ContextWrapper cw = new ContextWrapper(this);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File destFile = new File(directory, "profile.jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(destFile);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return destFile.getAbsolutePath();
    }

    private Bitmap scaleBitmapToMask(Bitmap src, Bitmap mask) {
        return Bitmap.createScaledBitmap(src, mask.getWidth(), mask.getHeight(), true);
    }

    private Bitmap cropByMask(Bitmap bmp) {
        Bitmap mask = BitmapFactory.decodeResource(getResources(), R.mipmap.mask);
        bmp = scaleBitmapToMask(bmp, mask);
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            options.inMutable = true;
        }
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap source = bmp;
        Bitmap bitmap;
        if (source.isMutable()) {
            bitmap = source;
        } else {
            bitmap = source.copy(Bitmap.Config.ARGB_8888, true);
            source.recycle();
        }
        bitmap.setHasAlpha(true);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(mask, 0, 0, paint);
        mask.recycle();
        return addBorder(bitmap);
    }

    private Bitmap addBorder(Bitmap bmp) {
        Bitmap mask = BitmapFactory.decodeResource(getResources(), R.mipmap.mask_add);
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            options.inMutable = true;
        }
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap source = bmp;
        Bitmap bitmap;
        if (source.isMutable()) {
            bitmap = source;
        } else {
            bitmap = source.copy(Bitmap.Config.ARGB_8888, true);
            source.recycle();
        }
        bitmap.setHasAlpha(true);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        canvas.drawBitmap(mask, 0, 0, paint);
        mask.recycle();
        return bitmap;
    }

}

