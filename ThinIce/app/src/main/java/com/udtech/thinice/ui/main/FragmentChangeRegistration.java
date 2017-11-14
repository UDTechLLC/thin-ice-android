package com.udtech.thinice.ui.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.udtech.thinice.R;
import com.udtech.thinice.UserSessionManager;
import com.udtech.thinice.eventbus.model.devices.DeleteDevice;
import com.udtech.thinice.eventbus.model.user.SaveUser;
import com.udtech.thinice.model.users.User;
import com.udtech.thinice.ui.LoginActivity;
import com.udtech.thinice.ui.MainActivity;
import com.udtech.thinice.ui.authorization.adapters.FragmentAdapterRegistration;
import com.udtech.thinice.utils.AchievementManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Sofi on 30.11.2015.
 */
public class FragmentChangeRegistration extends UserDataForm {
    static final int REQUEST_IMAGE_CAPTURE = 1, REQUEST_SELECT_FILE = 2;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    SmartTabLayout tabs;
    @BindView(R.id.avatar)
    ImageView avatar;
    private UserDataForm account, info;
    private String avatarUrl;
    private MenuHolder holder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
        holder = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_account, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        holder.openPosition(MenuHolder.ACCOUNT);
        view.findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.show();
            }
        });
        view.findViewById(R.id.action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DeleteDevice(null));
                AchievementManager.getInstance(getContext()).commit(getContext());
                Intent logout = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(logout);
                getActivity().finish();
                UserSessionManager.clearSession(getContext());
            }
        });
        ButterKnife.bind(this, view);
        account = new FragmentAccount();
        info = new FragmentInfo();
        FragmentAdapterRegistration adapter = new FragmentAdapterRegistration(getChildFragmentManager(), Arrays.asList(new Fragment[]{account, info}));
        viewPager.setAdapter(adapter);
        tabs.setViewPager(viewPager);
        initView();
    }

    public void initView() {
        User user = UserSessionManager.getSession(getActivity());
        if (user != null) {
            avatarUrl = user.getImageUrl();
        }
        updateView();
    }

    public void onEvent(SaveUser event) {
        User user = UserSessionManager.getSession(getActivity());
        if (event.isAccount()) {
            user = account.collectData(user);
        } else {
            user = info.collectData(user);
        }

        if (user != null) {
            user.setImageUrl(avatarUrl);
            user.save();
            getActivity().onBackPressed();
        }

    }

    @Override
    User collectData(User user) {
        return user;
    }

    @OnClick(R.id.avatar)
    void getAvatar() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Change photo");
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
        Intent intent = new Intent(getActivity(), CropActivity.class);
        intent.putExtra(CropActivity.TAKE_PHOTO_FLAG, true);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    private void selectFromGallery() {
        Intent intent = new Intent(getActivity(), CropActivity.class);
        intent.putExtra(CropActivity.CHOOSE_PHOTO_FLAG, true);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }


    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            avatarUrl = data.getStringExtra("path");
            UserSessionManager.getSession(getContext()).setImageUrl(avatarUrl);
            UserSessionManager.getSession(getContext()).save();
            updateView();
        }
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
