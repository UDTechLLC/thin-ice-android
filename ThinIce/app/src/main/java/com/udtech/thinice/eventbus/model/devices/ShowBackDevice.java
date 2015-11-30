package com.udtech.thinice.eventbus.model.devices;

import com.udtech.thinice.model.devices.Device;

/**
 * Created by Sofi on 30.11.2015.
 */
public class ShowBackDevice {
    private Device device;
    private boolean reverse;
    public ShowBackDevice(Device device) {
        this.device = device;
        reverse = false;
    }
    public ShowBackDevice(Device device, boolean reverse) {
        this.device = device;
        this.reverse = reverse;
    }

    public Device getDevice() {
        return device;
    }

    public boolean isReverse() {
        return reverse;
    }
}
