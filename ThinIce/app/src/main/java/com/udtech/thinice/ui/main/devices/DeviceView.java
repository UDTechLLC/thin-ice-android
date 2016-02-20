package com.udtech.thinice.ui.main.devices;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.udtech.thinice.R;
import com.udtech.thinice.eventbus.model.BluetoothCommand;
import com.udtech.thinice.eventbus.model.devices.DeviceChanged;
import com.udtech.thinice.eventbus.model.devices.ShowBackDevice;
import com.udtech.thinice.model.Settings;
import com.udtech.thinice.model.devices.Device;
import com.udtech.thinice.model.devices.Insole;

import java.text.SimpleDateFormat;

import de.greenrobot.event.EventBus;

/**
 * Created by Sofi on 30.11.2015.
 */
public class DeviceView extends FrameLayout {
    final GestureDetector gdt;
    private Device device;
    private Settings settings;

    public DeviceView(Context context) {
        super(context);
        gdt = new GestureDetector(context, new GestureListener());
        addView(inflate(getContext(), R.layout.item_wear, null));
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gdt.onTouchEvent(event);
                return true;
            }
        });
        EventBus.getDefault().register(this);
    }

    public void onEvent(DeviceChanged event) {
        if (device.equals(event.getDevice()))
            device = event.getDevice();
        initView();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        EventBus.getDefault().unregister(this);
    }

    public void setDevice(Device device) {
        this.device = device;
        initView();
    }

    public void initView() {
        if (device.isDisabled()) {
            setAlpha(0.5f);
        } else {
            setAlpha(1.0f);
        }
        if (device instanceof Insole)
            ((TextView) findViewById(R.id.name)).setText("Thin Ice Insoles");
        else
            ((TextView) findViewById(R.id.name)).setText("Thin Ice T Shirt");
        settings = new Settings().fetch(getContext());
        findViewById(R.id.switchCard).setOnClickListener(new OnClickListener() {
                                                             @Override
                                                             public void onClick(View v) {
                                                                 switchCards();
                                                             }
                                                         }
        );
        if (device instanceof Insole)
            ((ImageView) findViewById(R.id.ic_type)).setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_insoles_large));
        else
            ((ImageView) findViewById(R.id.ic_type)).setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_tshirt_large));
        setCharge(findViewById(R.id.charge), device.getCharge());
        ((TextView) findViewById(R.id.temperature)).setText((settings.isTemperature() ? Settings.convertTemperature(device.getTemperature()) : device.getTemperature()) + (settings.isTemperature() ? "°F" : "°C"));
        findViewById(R.id.plus).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!device.isDisabled()) {
                    ((TextView) findViewById(R.id.temperature)).setText((settings.isTemperature() ? Settings.convertTemperature(device.getTemperature()) : device.getTemperature()) + (settings.isTemperature() ? "°F" : "°C"));
                    BluetoothCommand command = new BluetoothCommand(device);
                    command.setTemperature(device.getTemperature()+1);
                    EventBus.getDefault().post(command);
                }
            }
        });
        findViewById(R.id.minus).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!device.isDisabled()) {
                    BluetoothCommand command = new BluetoothCommand(device);
                    command.setTemperature(device.getTemperature()+1);
                    EventBus.getDefault().post(command);
                    ((TextView) findViewById(R.id.temperature)).setText((settings.isTemperature() ? Settings.convertTemperature(device.getTemperature()) : device.getTemperature()) + (settings.isTemperature() ? "°F" : "°C"));
                }
            }
        });
        if (device.getTimer().getTime() != 0) {
            ((TextView) findViewById(R.id.timer)).setText(new SimpleDateFormat("h:mm a").format(device.getTimer()));
            ((TextView) findViewById(R.id.timer)).setVisibility(VISIBLE);
        } else {
            ((TextView) findViewById(R.id.timer)).setVisibility(INVISIBLE);
        }

    }

    public void switchCards() {
        EventBus.getDefault().post(new ShowBackDevice(device));
    }

    public void reverseSwitchCards() {
        EventBus.getDefault().post(new ShowBackDevice(device, true));
    }

    private void setCharge(View container, int charge) {
        ((ImageView) ((LinearLayout) container).getChildAt(0)).setImageDrawable(getResources().getDrawable((charge / 70) >= 1 ? R.mipmap.ic_charge_grey_fill : R.mipmap.ic_charge_grey_empty));
        ((ImageView) ((LinearLayout) container).getChildAt(1)).setImageDrawable(getResources().getDrawable((charge / 50) >= 1 ? R.mipmap.ic_charge_grey_fill : R.mipmap.ic_charge_grey_empty));
        ((ImageView) ((LinearLayout) container).getChildAt(2)).setImageDrawable(getResources().getDrawable((charge / 10) >= 1 ? R.mipmap.ic_charge_grey_fill : R.mipmap.ic_charge_grey_empty));
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
