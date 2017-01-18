package com.udtech.thinice.device.controll;

import android.util.Pair;

import com.udtech.thinice.eventbus.model.devices.PairDevice;
import com.udtech.thinice.model.devices.Device;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by JOkolot on 20.02.2016.
 */
public class DeviceController {
    List<Device> deviceList;
    List<String> deviceAddressList;
    public Pair<Device, String> current;
    private static DeviceController instance;

    public static DeviceController getInstance() {
        if (instance == null)
            instance = new DeviceController();
        return instance;
    }

    private DeviceController() {
    }

    public void addDevice(Device device, String mac) {
        if (deviceList == null) deviceList = new ArrayList<>();
        if (deviceAddressList == null) deviceAddressList = new ArrayList<>();
        deviceList.add(device);
        deviceAddressList.add(mac);
        device.save();
    }

    public void checkCurrentDevice(Device device) {
    }


    public synchronized void setTemperatureInCelsium(Device device, final float temperature) {
        checkCurrentDevice(device);
    }

    public synchronized void setTemperatureInFaringeite(Device device, final float temperature) {
        checkCurrentDevice(device);

    }

    public void on(Device device) {
        checkCurrentDevice(device);

    }

    public void off(Device device) {
        checkCurrentDevice(device);
    }

    public boolean contain(String mac) {
        for (String collectedMac : deviceAddressList)
            if (collectedMac.equals(mac))
                return true;
        return false;
    }

    public void delete(Device device) {
        if (current == null ? true : !current.first.getId().equals(device.getId())) {
            for (int i = 0; i < deviceList.size(); i++) {
                if (device.getId().equals(deviceList.get(i).getId())) {
                    current = new Pair<>(deviceList.get(i), deviceAddressList.get(i));
                    int index = deviceList.indexOf(current);
                    deviceList.remove(index);
                    deviceAddressList.remove(index);
                    //todo remove
                    break;
                }
            }
        } else if (current != null) {
            int index = deviceList.indexOf(current.first);
            deviceList.remove(index);
            deviceAddressList.remove(index);
            //todo remove
        }
        current = null;
    }
}
