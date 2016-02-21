package com.udtech.thinice.utils;

import android.os.Handler;

import com.udtech.thinice.device.controll.DeviceController;
import com.udtech.thinice.eventbus.model.BluetoothCommand;
import com.udtech.thinice.eventbus.model.devices.DeviceChanged;
import com.udtech.thinice.model.devices.Device;
import com.udtech.thinice.ui.MainActivity;

import java.util.Date;

import de.greenrobot.event.EventBus;

/**
 * Created by JOkolot on 04.12.2015.
 */
public class DelayedDeviceStart {
    private Device device;
    private CancelableRunnable runnable;
    private static DelayedDeviceStart insoles, tshirt;

    public static DelayedDeviceStart getInsolesInstance(Device device) {
        if (insoles == null)
            insoles = new DelayedDeviceStart(device);
        return insoles;
    }

    public static DelayedDeviceStart getTShirtInstance(Device device) {
        if (tshirt == null)
            tshirt = new DelayedDeviceStart(device);
        return tshirt;
    }

    public DelayedDeviceStart(Device device) {
        this.device = device;
    }

    public void start(int timeOffset, MainActivity activity) {
        if (runnable != null) {
            runnable.cancel();
        }
        runnable = new CancelableRunnable(device,activity);
        new Handler().postDelayed(runnable, timeOffset);
    }

    public void cancel() {
        if (runnable != null) {
            runnable.cancel();
        }
    }

    private class CancelableRunnable implements Runnable {
        private Device device;
        private boolean cancel;
        private MainActivity activity;
        public CancelableRunnable(Device device, MainActivity activity) {
            this.device = device;
            cancel = false;
            this.activity = activity;
        }

        public void cancel() {
            cancel = true;
        }

        @Override
        public void run() {
            if (!cancel) {
                device.setDisabled(false);
                device.setTimer(new Date(0));
                device.save();
                DeviceController.getInstance(activity).on(device);
            }
        }
    }
}
