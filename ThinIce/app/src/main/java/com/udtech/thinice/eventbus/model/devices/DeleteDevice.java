package com.udtech.thinice.eventbus.model.devices;

import com.udtech.thinice.model.devices.Device;

/**
 * Created by JOkolot on 30.11.2015.
 */
public class DeleteDevice {
    private Device device;

    public Device getDevice() {
        return device;
    }

    public DeleteDevice(Device device) {

        this.device = device;
    }
}
