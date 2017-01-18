package com.udtech.thinice.utils;

import android.os.Handler;

import com.udtech.thinice.eventbus.model.bluetooth.SendMessage;
import com.udtech.thinice.model.devices.Device;
import com.udtech.thinice.protocol.Protocol;
import com.udtech.thinice.ui.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;

/**
 * Created by JOkolot on 04.12.2015.
 */
public class DelayedDeviceStart {
    private static DelayedDeviceStart insoles, tshirt;
    private Device device;
    private CancelableRunnable runnable;
    private long startTime;

    public DelayedDeviceStart(Device device) {
        this.device = device;
    }

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

    public void start(int timeOffset, MainActivity activity) {
        if (runnable != null) {
            runnable.cancel();
        }
        startTime = new Date().getTime() + timeOffset;
        runnable = new CancelableRunnable(device, activity);
        new Handler().postDelayed(runnable, timeOffset);
    }

    public long getTimeLeft() {
        try {
            if (startTime != 0)
                return startTime - new Date().getTime() + new SimpleDateFormat("HH mm ss").parse("00 00 00").getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void cancel() {
        startTime = 0;
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
                EventBus.getDefault().post(new SendMessage(new Protocol().getOn((int) device.getTemperature())));
            }
        }
    }
}
