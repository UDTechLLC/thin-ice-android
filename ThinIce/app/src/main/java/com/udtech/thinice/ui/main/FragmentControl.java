package com.udtech.thinice.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udtech.thinice.DeviceManager;
import com.udtech.thinice.R;
import com.udtech.thinice.eventbus.model.devices.DeleteDevice;
import com.udtech.thinice.eventbus.model.devices.DeviceChanged;
import com.udtech.thinice.eventbus.model.devices.SearchDevices;
import com.udtech.thinice.eventbus.model.devices.ShowBackDevice;
import com.udtech.thinice.eventbus.model.devices.ShowFrontDevice;
import com.udtech.thinice.model.devices.Device;
import com.udtech.thinice.ui.MainActivity;
import com.udtech.thinice.ui.main.devices.DeviceControl;
import com.udtech.thinice.ui.main.devices.DeviceView;
import com.udtech.thinice.ui.widgets.animation.FlipAnimation;
import com.udtech.thinice.ui.widgets.animation.RevertAnimation;

import de.greenrobot.event.EventBus;

/**
 * Created by Sofi on 30.11.2015.
 */
public class FragmentControl extends Fragment {
    DeviceControl back;
    private FlipAnimation swipe;
    private RevertAnimation reverse;
    private View front;
    private MenuHolder holder;

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        holder = (MainActivity) getActivity();
        EventBus.getDefault().register(this);
    }

    public void switchCards(boolean reversed, Device device) {
        back.setDevice(device);
        if (reversed)
            reverseSwitchCards();
        else
            switchCards();
    }

    public void switchCards(boolean reversed) {
        if (reversed)
            reverseSwitchCards();
        else
            switchCards();
    }

    public void switchCards() {
        if (front.getVisibility() == View.GONE || swipe.isReversed(front)) {
            reverse.reverse();
            swipe.reverse();
        }
        getView().findViewById(R.id.container).startAnimation(swipe);
    }

    public void reverseSwitchCards() {
        if (front.getVisibility() == View.GONE || reverse.isReversed(front)) {
            reverse.reverse();
            swipe.reverse();
        }
        getView().findViewById(R.id.container).startAnimation(reverse);
    }

    public void onEvent(ShowBackDevice event) {
        switchCards(event.isReverse(), event.getDevice());
    }

    public void onEvent(ShowFrontDevice event) {
        switchCards(event.isReverse());
    }

    private Boolean isDevice = null;

    public void onEvent(DeviceChanged event) {
        if (getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (getView() != null && !isDevice.equals(DeviceManager.getDevice() != null)) {
                        isDevice = DeviceManager.getDevice() != null;
                        if (isDevice) {
                            front = new DeviceView(getActivity());
                            ((DeviceView) front).setDevice(DeviceManager.getDevice());
                            back = new DeviceControl(getContext());
                            back.setDevice(DeviceManager.getDevice());
                            ((ViewGroup) getView().findViewById(R.id.container)).addView(back);
                            ((ViewGroup) getView().findViewById(R.id.container)).addView(front);
                            back.setVisibility(View.GONE);
                            swipe = new FlipAnimation(front, back);
                            reverse = new RevertAnimation(front, back);
                        } else {
                            if (((ViewGroup) getView().findViewById(R.id.container)) != null)
                                ((ViewGroup) getView().findViewById(R.id.container)).removeAllViews();
                        }
                    }
                }
            });

    }

    public void onEvent(DeleteDevice event) {
        if (getView() != null && getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    getView().findViewById(R.id.action).setVisibility(View.VISIBLE);
                }
            });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_control, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isDevice = DeviceManager.getDevice() != null;
        if (isDevice) {
            view.findViewById(R.id.action).setVisibility(View.GONE);
        }
        holder.openPosition(MenuHolder.CONTROL);
        view.findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.show();
            }
        });
        view.findViewById(R.id.action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).addToBackStack(null).add(R.id.fragment_container, new FragmentAddWear()).commit();
            }
        });
        if (DeviceManager.getDevice() != null) {
            front = new DeviceView(getActivity());
            ((DeviceView) front).setDevice(DeviceManager.getDevice());
            back = new DeviceControl(getContext());
            back.setDevice(DeviceManager.getDevice());
            ((ViewGroup) view.findViewById(R.id.container)).addView(back);
            ((ViewGroup) view.findViewById(R.id.container)).addView(front);
            back.setVisibility(View.GONE);
            swipe = new FlipAnimation(front, back);
            reverse = new RevertAnimation(front, back);
        }
    }

    public void onEvent(SearchDevices event) {
        if (getView() != null && getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getView().findViewById(R.id.action).performClick();
                }
            });
    }
}
