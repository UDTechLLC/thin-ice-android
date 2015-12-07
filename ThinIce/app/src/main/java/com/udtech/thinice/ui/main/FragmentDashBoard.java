package com.udtech.thinice.ui.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.udtech.thinice.R;
import com.udtech.thinice.UserSessionManager;
import com.udtech.thinice.model.devices.Insole;
import com.udtech.thinice.model.devices.TShirt;
import com.udtech.thinice.model.users.User;
import com.udtech.thinice.ui.MainActivity;
import com.udtech.thinice.ui.main.cards.FragmentCards;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JOkolot on 18.11.2015.
 */
public class FragmentDashBoard extends Fragment {
    private MenuHolder holder;
    private Pair<TShirt, Insole> devices;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        holder = (MainActivity) getActivity();
        devices = new Pair<>(new TShirt(), new Insole());
        devices.first.setCharge(88);
        devices.second.setCharge(28);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    private void updateAvatar(String avatarUrl, final ImageView avatar) {
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        holder.openPosition(MenuHolder.DASHBOARD);
        getChildFragmentManager().beginTransaction().replace(R.id.device_container, FragmentDevices.getInstance(devices)).commit();
        initDays(view);
        User user = UserSessionManager.getSession(getActivity());
        updateAvatar(user.getImageUrl(), (ImageView) view.findViewById(R.id.avatar));
    }

    private void initDays(View view) {
        getChildFragmentManager().beginTransaction().replace(R.id.day_container, new FragmentCards()).commit();
    }

    @OnClick(R.id.menu)
    void showMenu() {
        holder.show();
    }
}
