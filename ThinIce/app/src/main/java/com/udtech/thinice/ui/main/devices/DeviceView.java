package com.udtech.thinice.ui.main.devices;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.DialogInterface;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.udtech.thinice.DeviceManager;
import com.udtech.thinice.R;
import com.udtech.thinice.device.controll.DeviceController;
import com.udtech.thinice.eventbus.model.bluetooth.SendMessage;
import com.udtech.thinice.eventbus.model.devices.DeleteDevice;
import com.udtech.thinice.eventbus.model.devices.DeviceChanged;
import com.udtech.thinice.eventbus.model.devices.ShowBackDevice;
import com.udtech.thinice.model.Settings;
import com.udtech.thinice.model.devices.Device;
import com.udtech.thinice.protocol.Protocol;
import com.udtech.thinice.utils.DelayedDeviceStart;

import java.text.SimpleDateFormat;

import de.greenrobot.event.EventBus;

/**
 * Created by Sofi on 30.11.2015.
 */
public class DeviceView extends FrameLayout {
    final GestureDetector gdt;
    private volatile Device device;
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
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                device = DeviceManager.getDevice();
                if (device == null) {
                    return;
                }
                ((TextView) findViewById(R.id.name)).setText("Thin Ice Vest");
                settings = new Settings().fetch(getContext());
                findViewById(R.id.switchCard).setOnClickListener(new OnClickListener() {
                                                                     @Override
                                                                     public void onClick(View v) {
                                                                         switchCards();
                                                                     }
                                                                 }
                );
                if(DeviceManager.getDevice().isDisabled()){
                    ((TextView)findViewById(R.id.button)).setText("Turn on");
                }else{
                    ((TextView)findViewById(R.id.button)).setText("Turn off");
                }
                findViewById(R.id.button).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(DeviceManager.getDevice().isDisabled()){
                            ((TextView)findViewById(R.id.button)).setText("Turn off");
                            EventBus.getDefault().post(new SendMessage(new Protocol().getOn((int) DeviceManager.getDevice().getTemperature())));
                        }else{
                            ((TextView)findViewById(R.id.button)).setText("Turn on");
                            EventBus.getDefault().post(new SendMessage(new Protocol().getDisable()));
                        }
                    }
                });
                ((ImageView) findViewById(R.id.ic_type)).setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_tshirt_large));
                setCharge(findViewById(R.id.charge), device.getCharge());
                ((TextView) findViewById(R.id.textView14)).setText(device.getCharge() + "%");
                ((TextView) findViewById(R.id.temperature)).setText(Math.round((settings.isTemperature() ? Settings.convertTemperatureToFaringeite(device.getTemperature()) : device.getTemperature())) + (settings.isTemperature() ? "°F" : "°C"));
                findViewById(R.id.delete).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EventBus.getDefault().post(new DeleteDevice(device));
                    }
                });
                findViewById(R.id.plus).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int newTempPosition = settings.isTemperature()?settings.getFarenheitPosition(getContext(), (int) device.getTemperature()):settings.getCelsiumPosition(getContext(), (int) device.getTemperature());
                        newTempPosition++;
                        int newTemp = settings.getTemperatureByPosition(getContext(),newTempPosition,false);
                        if (newTemp <= device.getMin())
                            findViewById(R.id.minus).setAlpha((float) 0.5);
                        else
                            findViewById(R.id.minus).setAlpha((float) 1);
                        if (newTemp >= device.getMax())
                            findViewById(R.id.plus).setAlpha((float) 0.5);
                        else
                            findViewById(R.id.plus).setAlpha((float) 1);
                        if(DeviceManager.getDevice().isDisabled()){
                            DeviceManager.getDevice().setTemperature(newTemp);
                            EventBus.getDefault().post(new DeviceChanged(DeviceManager.getDevice()));
                        }else
                        EventBus.getDefault().post(new SendMessage(new Protocol().setTemp(newTemp)));
                    }
                });
                findViewById(R.id.minus).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int newTempPosition = settings.isTemperature()?settings.getFarenheitPosition(getContext(), (int) device.getTemperature()):settings.getCelsiumPosition(getContext(), (int) device.getTemperature());
                        newTempPosition--;
                        int newTemp = settings.getTemperatureByPosition(getContext(),newTempPosition,false);
                            if (newTemp <= device.getMin())
                                findViewById(R.id.minus).setAlpha((float) 0.5);
                            else
                                findViewById(R.id.minus).setAlpha((float) 1);
                            if (newTemp >= device.getMax())
                                findViewById(R.id.plus).setAlpha((float) 0.5);
                            else
                                findViewById(R.id.plus).setAlpha((float) 1);
                        if(DeviceManager.getDevice().isDisabled()){
                            DeviceManager.getDevice().setTemperature(newTemp);
                            EventBus.getDefault().post(new DeviceChanged(DeviceManager.getDevice()));
                        }else
                        EventBus.getDefault().post(new SendMessage(new Protocol().setTemp(newTemp)));
                    }
                });

            }
        });
    }

    public void switchCards() {
        //EventBus.getDefault().post(new ShowBackDevice(device));
    }

    public void reverseSwitchCards() {
        //EventBus.getDefault().post(new ShowBackDevice(device, true));
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
