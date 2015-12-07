package com.udtech.thinice.utils;

import android.os.Handler;

import com.udtech.thinice.eventbus.model.devices.DeviceChanged;
import com.udtech.thinice.model.devices.Device;

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

    public void start(int timeOffset) {
        if (runnable != null) {
            runnable.cancel();
        }
        runnable = new CancelableRunnable(device);
        new Handler().postDelayed(runnable, timeOffset);
    }

    public void cancel() {
        if (runnable != null) {
            runnable.cancel();
        }
    }

    private static class CancelableRunnable implements Runnable {
        private Device device;
        private boolean cancel;

        public CancelableRunnable(Device device) {
            this.device = device;
            cancel = false;
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
                EventBus.getDefault().postSticky(new DeviceChanged(device));
            }
        }
    }
}
