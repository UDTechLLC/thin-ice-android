package com.udtech.thinice.device.controll;

import android.util.Pair;

import com.udtech.thinice.bluetooth.activity.BluetoothActivityInterface;
import com.udtech.thinice.eventbus.model.devices.DeviceChanged;
import com.udtech.thinice.model.devices.Device;
import com.udtech.thinice.protocol.Protocol;
import com.udtech.thinice.protocol.ProtocolV1;
import com.udtech.thinice.ui.MainActivity;

import java.io.UnsupportedEncodingException;
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
    private Protocol protocol;
    private BluetoothActivityInterface activity;
    private static DeviceController instance;

    public static DeviceController getInstance(BluetoothActivityInterface activity) {
        if (instance == null)
            instance = new DeviceController(activity);
        return instance;
    }

    private DeviceController(BluetoothActivityInterface activity) {
        this.protocol = new ProtocolV1();
        this.activity = activity;
    }

    public void addDevice(Device device, String mac) {
        if (deviceList == null) deviceList = new ArrayList<>();
        if (deviceAddressList == null) deviceAddressList = new ArrayList<>();
        deviceList.add(device);
        deviceAddressList.add(mac);
        device.save();
        EventBus.getDefault().post(new DeviceChanged(device));
    }

    public void addDevice(Pair<Device, String> device) {
        if (deviceList == null) deviceList = new ArrayList<>();
        if (deviceAddressList == null) deviceAddressList = new ArrayList<>();
        deviceList.add(device.first);
        deviceAddressList.add(device.second);
    }

    public void checkCurrentDevice(Device device) {
        if (current == null ? true : !current.first.getId().equals(device.getId())) {
            for (int i = 0; i < deviceList.size(); i++) {
                if (device.getId().equals(deviceList.get(i).getId())) {
                    current = new Pair<>(deviceList.get(i), deviceAddressList.get(i));
                    activity.createClient(current.second);
                    break;
                }
            }
        }
    }


    public synchronized void setTemperatureInCelsium(Device device, final float temperature) {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        checkCurrentDevice(device);
        try {
            final String command = new String(protocol.setTemperature((int) temperature));
            activity.sendMessage(command.toUpperCase());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public synchronized void setTemperatureInFaringeite(Device device, final float temperature) {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        checkCurrentDevice(device);
        final String command = new String(protocol.setRawTemperature((int) temperature));
        activity.sendMessage(command.toUpperCase());
    }

    public void on(Device device) {
        checkCurrentDevice(device);
        try {
            String command = new String(protocol.on());
            activity.sendMessage(command);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void off(Device device) {
        checkCurrentDevice(device);
        try {
            String command = new String(protocol.off());
            activity.sendMessage(command);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
                    activity.resetCurrentClient();
                    break;
                }
            }
        } else if (current != null) {
            int index = deviceList.indexOf(current.first);
            deviceList.remove(index);
            deviceAddressList.remove(index);
            activity.resetCurrentClient();
        }
        current = null;
    }
}
