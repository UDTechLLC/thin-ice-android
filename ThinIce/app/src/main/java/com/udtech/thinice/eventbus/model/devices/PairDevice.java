package com.udtech.thinice.eventbus.model.devices;

import android.bluetooth.BluetoothDevice;

import com.udtech.thinice.model.devices.Device;

/**
 * Created by JOkolot on 19.02.2016.
 */
public class PairDevice {
    private Device device;
    private BluetoothDevice bluetoothDevice;

    public PairDevice(Device device, BluetoothDevice bluetoothDevice) {
        this.device = device;
        this.bluetoothDevice = bluetoothDevice;
    }

    public Device getDevice() {
        return device;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }
}
