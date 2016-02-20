package com.udtech.thinice.ui.main.devices;

import android.content.Context;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rey.material.widget.Switch;
import com.udtech.thinice.R;
import com.udtech.thinice.eventbus.model.BluetoothCommand;
import com.udtech.thinice.eventbus.model.devices.DeleteDevice;
import com.udtech.thinice.eventbus.model.devices.DeviceChanged;
import com.udtech.thinice.eventbus.model.devices.ShowFrontDevice;
import com.udtech.thinice.model.Notification;
import com.udtech.thinice.model.devices.Device;
import com.udtech.thinice.model.devices.Insole;
import com.udtech.thinice.model.devices.TShirt;
import com.udtech.thinice.utils.DelayedDeviceStart;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Sofi on 30.11.2015.
 */
public class DeviceControl extends FrameLayout {
    private Device device;
    private boolean deviceRunned;
    private int selectedPosition = 1;

    public DeviceControl(Context context) {
        super(context);
        addView(inflate(getContext(), R.layout.item_wear_control, null));
    }

    public void setDevice(Device device) {
        if (device instanceof TShirt)
            this.device = TShirt.findById(TShirt.class, ((TShirt) device).getId());
        else
            this.device = Insole.findById(Insole.class, ((Insole) device).getId());
        deviceRunned = !device.isDisabled();
        initView();
    }

    private void initView() {
        ((Switch) findViewById(R.id.disable)).setChecked(device.isDisabled());
        ((Switch) findViewById(R.id.disable)).setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                device.setDisabled(checked);
                ((TextView) findViewById(R.id.disable_text)).setTextColor(checked ? Color.WHITE : getResources().getColor(R.color.textViewColor));
            }
        });
        ((Switch) findViewById(R.id.timer_enabled)).setChecked(device.getTimer().getTime() != 0);
        ((Switch) findViewById(R.id.timer_enabled)).setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                device.setTimer(checked ? new Date(new Date().getTime() + 3600000/*hour*/ / (selectedPosition == 2 ? 1 : 2)) : new Date(0));
                ((TextView) findViewById(R.id.enale_timer_text)).setTextColor(checked ? Color.WHITE : getResources().getColor(R.color.textViewColor));
                if (checked)
                    ((Switch) findViewById(R.id.disable)).setChecked(checked);
            }
        });
        findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCards();
            }
        });
        findViewById(R.id.save).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (device.getTimer().getTime() != 0) {
                    if (device instanceof Insole)
                        DelayedDeviceStart.getInsolesInstance(device).start((int) (device.getTimer().getTime() - new Date().getTime()));
                    else
                        DelayedDeviceStart.getTShirtInstance(device).start((int) (device.getTimer().getTime() - new Date().getTime()));
                } else {
                    if (device instanceof Insole)
                        DelayedDeviceStart.getInsolesInstance(device).cancel();
                    else
                        DelayedDeviceStart.getTShirtInstance(device).cancel();
                }
                switchCards();
                device.save();
                if(deviceRunned==device.isDisabled()){
                    BluetoothCommand command = new BluetoothCommand(device);
                    if(device.isDisabled()) command.setStopCommand();
                    else command.setStartCommand();
                    EventBus.getDefault().post(command);
                }
            }
        });

        final View selection = findViewById(R.id.selection);
        final ViewGroup parent = ((ViewGroup) selection.getParent());
        ((TextView) findViewById(R.id.disable_text)).setTextColor(device.isDisabled() ? Color.WHITE : getResources().getColor(R.color.textViewColor));
        ((TextView) findViewById(R.id.enale_timer_text)).setTextColor(device.getTimer().getTime() == 0 ? Color.WHITE : getResources().getColor(R.color.textViewColor));
        findViewById(R.id.one).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                parent.removeAllViews();
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.weight = 1;
                parent.addView(selection, lp);
                setTextColor(1);
                Notification.getInstance(getContext()).setTimer(1);
                selectedPosition = 1;
                device.setTimer(((Switch) findViewById(R.id.timer_enabled)).isChecked() ? new Date(new Date().getTime() + 3600000/*hour*/ / (selectedPosition == 2 ? 1 : 2)) : new Date(0));
            }
        });
        findViewById(R.id.two).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                parent.removeAllViews();
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.weight = 2;
                parent.addView(selection, lp);
                setTextColor(2);
                Notification.getInstance(getContext()).setTimer(1);
                selectedPosition = 2;
                device.setTimer(((Switch) findViewById(R.id.timer_enabled)).isChecked() ? new Date(new Date().getTime() + 3600000/*hour*/ / (selectedPosition == 2 ? 1 : 2)) : new Date(0));
            }
        });
        findViewById(R.id.delete).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCards();
                EventBus.getDefault().post(new DeleteDevice(device));
                EventBus.getDefault().post(new DeviceChanged(device));
            }
        });
    }

    public void setTextColor(int count) {
        List<View> views = Arrays.asList(new View[]{findViewById(R.id.one), findViewById(R.id.two)});
        for (int i = 0; i < views.size(); i++) {
            views.get(i).setAlpha(1f);
        }
        for (int i = count; i < views.size(); i++) {
            views.get(i).setAlpha(0.5f);
        }
    }

    public void switchCards() {
        EventBus.getDefault().post(new ShowFrontDevice());
    }

    public void reverseSwitchCards() {
        EventBus.getDefault().post(new ShowFrontDevice(true));
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 30;
        private static final int SWIPE_THRESHOLD_VELOCITY = 60;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                reverseSwitchCards();
                return false; // Right to left
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                switchCards();
                return false; // Right to left
            }

            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Bottom to top
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Top to bottom
            }
            return true;
        }

    }
}
