package com.udtech.thinice.eventbus.model.devices;

import com.udtech.thinice.model.devices.Device;

/**
 * Created by Sofi on 30.11.2015.
 */
public class ShowFrontDevice {
    private boolean reverse;
    public ShowFrontDevice() {
        reverse = false;
    }
    public ShowFrontDevice(boolean reverse) {
        this.reverse = reverse;
    }

    public boolean isReverse() {
        return reverse;
    }
}
