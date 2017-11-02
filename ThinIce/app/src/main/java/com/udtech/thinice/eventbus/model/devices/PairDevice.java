package com.udtech.thinice.eventbus.model.devices;

import com.udtech.thinice.model.devices.Device;

/**
 * Created by JOkolot on 19.02.2016.
 */
public class PairDevice {
    private Device device;
    private String mac;
    private String name;

    public PairDevice(Device device, String mac, String name) {
        this.device = device;
        this.mac = mac;
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public Device getDevice() {
        return device;

    }

    public String getName() {
        return name;
    }
}
