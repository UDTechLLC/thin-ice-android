package com.udtech.thinice.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.udtech.thinice.DeviceManager;
import com.udtech.thinice.R;
import com.udtech.thinice.UserSessionManager;
import com.udtech.thinice.eventbus.model.bluetooth.SendMessage;
import com.udtech.thinice.eventbus.model.devices.DeviceChanged;
import com.udtech.thinice.model.users.User;
import com.udtech.thinice.protocol.Protocol;
import com.udtech.thinice.ui.MainActivity;

import de.greenrobot.event.EventBus;

/**
 * Created by JOkolot on 18.11.2015.
 */
public class FragmentDevices extends Fragment {
    private MenuHolder holder;

    @Deprecated
    public FragmentDevices() {
        super();
    }

    public static Fragment getInstance() {
        FragmentDevices fragment = new FragmentDevices();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        holder = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_devices, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        view.findViewById(R.id.power).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DeviceChanged(DeviceManager.getDevice()));
                if (DeviceManager.getDevice() != null)
                    EventBus.getDefault().post(new SendMessage(!DeviceManager.getDevice().isDisabled() ? (new Protocol().getOff()) : (new Protocol().getOn((int) DeviceManager.getDevice().getTemperature()))));
            }
        });
        view.findViewById(R.id.tshirt_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MenuHolder) getActivity()).showMenuItem(MenuHolder.CONTROL);
            }
        });
        check(view);
    }

    public void onEvent(DeviceChanged event) {
        check(getView());
    }

    private void check(final View view) {
        if (getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (view != null && getActivity() != null) {
                        User user = UserSessionManager.getSession(getActivity());
                        ((TextView) view.findViewById(R.id.name)).setText((user.getFirstName() != null ? user.getFirstName() : "") + " " + (user.getLastName() != null ? user.getLastName() : ""));
                        view.findViewById(R.id.separator).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.power).setVisibility(View.VISIBLE);
                        if (DeviceManager.getDevice() != null) {
                            view.findViewById(R.id.tshirt_container).setVisibility(View.VISIBLE);
                            setCharge(view.findViewById(R.id.charge_tshirt), DeviceManager.getDevice().getCharge());
                            ((TextView) view.findViewById(R.id.charge_tshirt_text)).setText(DeviceManager.getDevice().getCharge() + "%");
                            if (DeviceManager.getDevice().isDisabled()) {
                                view.findViewById(R.id.tshirt_container).setAlpha(0.5f);
                                view.findViewById(R.id.power).setBackground(getResources().getDrawable(R.drawable.btn_power_off));
                            } else {
                                view.findViewById(R.id.tshirt_container).setAlpha(1.0f);
                                view.findViewById(R.id.power).setBackground(getResources().getDrawable(R.drawable.btn_power_on));
                            }
                        } else {
                            view.findViewById(R.id.tshirt_container).setVisibility(View.GONE);
                            view.findViewById(R.id.separator).setVisibility(View.GONE);
                        }
                        if (DeviceManager.getDevice() == null) {
                            view.findViewById(R.id.power).setVisibility(View.GONE);
                            view.findViewById(R.id.charge).setVisibility(View.GONE);
                        } else if (DeviceManager.getDevice().isCharging()) {
                            view.findViewById(R.id.power).setVisibility(View.GONE);
                            view.findViewById(R.id.charge).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.charge_container).setVisibility(View.GONE);
                            view.findViewById(R.id.charge_small).setVisibility(View.VISIBLE);
                        } else {
                            view.findViewById(R.id.power).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.charge).setVisibility(View.GONE);
                            view.findViewById(R.id.charge_container).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.charge_small).setVisibility(View.GONE);
                        }
                    }
                }
            });

    }

    private void setCharge(View container, int charge) {
        ((ImageView) ((LinearLayout) container).getChildAt(0)).setImageDrawable(getResources().getDrawable((charge / 70f) >= 1 ? R.mipmap.ic_charge_fill : R.mipmap.ic_charge_empty));
        ((ImageView) ((LinearLayout) container).getChildAt(1)).setImageDrawable(getResources().getDrawable((charge / 50f) >= 1 ? R.mipmap.ic_charge_fill : R.mipmap.ic_charge_empty));
        ((ImageView) ((LinearLayout) container).getChildAt(2)).setImageDrawable(getResources().getDrawable((charge / 10f) >= 1 ? R.mipmap.ic_charge_fill : R.mipmap.ic_charge_empty));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
