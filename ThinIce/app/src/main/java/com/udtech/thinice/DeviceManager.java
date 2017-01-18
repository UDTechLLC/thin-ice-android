package com.udtech.thinice;

import com.udtech.thinice.model.devices.Device;

/**
 * Created by JOkolot on 11.05.2016.
 */
public class DeviceManager {
    private static Device mDevice;
    public static void initDevice(Device device){
        mDevice = device;
    }

    public static Device getDevice() {
        return mDevice;
    }
}
