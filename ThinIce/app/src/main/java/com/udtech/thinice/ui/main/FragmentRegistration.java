package com.udtech.thinice.ui.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.udtech.thinice.R;
import com.udtech.thinice.model.users.User;
import com.udtech.thinice.ui.authorization.adapters.FragmentAdapterRegistration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JOkolot on 04.11.2015.
 */
public class FragmentRegistration extends UserDataForm {
    static final int REQUEST_IMAGE_CAPTURE = 1, REQUEST_SELECT_FILE = 2;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.tabs)
    SmartTabLayout tabs;
    @Bind(R.id.avatar)
    ImageView avatar;
    private UserDataForm account, info;
    private String avatarUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().onBackPressed();
                    getActivity().onBackPressed();
                    return true;
                }
                return false;
            }
        });
        ButterKnife.bind(this, view);
        account = new FragmentAccount();
        info = new FragmentInfo();
        FragmentAdapterRegistration adapter = new FragmentAdapterRegistration(getChildFragmentManager(), Arrays.asList(new Fragment[]{account, info}));
        viewPager.setAdapter(adapter);
        tabs.setViewPager(viewPager);
    }

    @Override
    User collectData(User user) {
        user.setImageUrl(avatarUrl);
        user = account.collectData(user);
        if (user == null) {
            viewPager.setCurrentItem(1);
        } else {
            user = account.collectData(user);
            if (user == null) {
                viewPager.setCurrentItem(2);
            }
        }
        return user;
    }

    @OnClick(R.id.avatar)
    void getAvatar() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    takePhoto();
                } else if (items[item].equals("Choose from Library")) {
                    selectFromGallery();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void selectFromGallery() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(intent, "Select File"),
                REQUEST_SELECT_FILE);
    }

    private String saveBitmap(Bitmap bmp) {
        ContextWrapper cw = new ContextWrapper(getActivity());
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

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (resultCode == getActivity().RESULT_OK) {
                    Bitmap bmp = null;
                    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
                        Bundle extras = data.getExtras();
                        bmp = (Bitmap) extras.get("data");
                        avatarUrl = saveBitmap(cropByMask(bmp));
                    } else if (requestCode == REQUEST_SELECT_FILE && resultCode == getActivity().RESULT_OK) {
                        Uri selectedImageUri = data.getData();
                        try {
                            bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                            avatarUrl = saveBitmap(cropByMask(bmp));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    updateView();
                }
            }
        }).start();
    }

    private Bitmap cropSquare(Bitmap bitmap) {
        Bitmap dstBmp = null;
        if (bitmap.getWidth() >= bitmap.getHeight()) {

            dstBmp = Bitmap.createBitmap(
                    bitmap,
                    bitmap.getWidth() / 2 - bitmap.getHeight() / 2,
                    0,
                    bitmap.getHeight(),
                    bitmap.getHeight()
            );

        } else {

            dstBmp = Bitmap.createBitmap(
                    bitmap,
                    0,
                    bitmap.getHeight() / 2 - bitmap.getWidth() / 2,
                    bitmap.getWidth(),
                    bitmap.getWidth()
            );
        }
        return dstBmp;
    }

    private Bitmap scaleBitmapToMask(Bitmap src, Bitmap mask) {
        return Bitmap.createScaledBitmap(src, mask.getWidth(), mask.getHeight(), true);
    }

    private Bitmap cropByMask(Bitmap bmp) {
        Bitmap mask = BitmapFactory.decodeResource(getResources(), R.mipmap.mask);
        bmp = scaleBitmapToMask(cropSquare(bmp), mask);
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

    private void updateView() {
        if (avatarUrl != null) {
            File bitmap = new File(avatarUrl);
            try {
                final Bitmap b = BitmapFactory.decodeStream(new FileInputStream(bitmap));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        avatar.setImageBitmap(b);
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
