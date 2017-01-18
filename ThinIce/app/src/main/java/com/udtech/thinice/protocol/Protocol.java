package com.udtech.thinice.protocol;

import com.udtech.thinice.DeviceManager;
import com.udtech.thinice.eventbus.model.devices.DeleteDevice;
import com.udtech.thinice.eventbus.model.devices.DeviceChanged;
import com.udtech.thinice.model.devices.Device;

import de.greenrobot.event.EventBus;

/**
 * Created by JOkolot on 28.04.2016.
 */
public class Protocol {
    public static final byte STATE_WAITING = 0x01, STATE_SLEEP = 0x02, STATE_ON = 0x03;
    private byte[] serverResponse;

    public Protocol(byte[] serverResponse) {
        if (serverResponse.length == 16) {
            this.serverResponse = serverResponse;
        }
    }

    public Protocol() {
    }

    public float getTemp() {
        return (int)serverResponse[12]+((int)((serverResponse[13])&0x7f))/100f;
    }

    public boolean getState() {
        if ((serverResponse[11]&0x03) == 2) {
            EventBus.getDefault().post(new DeleteDevice(DeviceManager.getDevice()));
        }
        return getBit(serverResponse[11], 0) > 0;
    }

    public boolean isCharging() {
        return getBit(serverResponse[11], 5) > 0 ? true : false;
    }

    public byte[] getOn(int temp) {
        return new byte[]{(byte) temp, 0x03, 0x00};
    }

    public byte[] getOff() {
        return new byte[]{0x01, 0x00};
    }

    public byte[] setName(String name) {
        byte[] nameBytes = name.getBytes();
        byte[] result = new byte[nameBytes.length + 4];
        result[0] = 0x0A;
        for (int i = 1; i < nameBytes.length + 1; i++)
            result[i] = nameBytes[i-1];
        result[nameBytes.length + 1] = 0x0d;
        result[nameBytes.length + 2] = 0x0a;
        result[nameBytes.length + 3] = 0x00;
        return result;

    }

    public byte[] getDeviceStatus() {
        return new byte[]{0x04, 0x00};
    }

    public byte[] setPass(String pass) {
        byte[] passBytes = pass.getBytes();
        byte[] result = new byte[passBytes.length + 1];
        result[result.length - 1] = 0x0A;
        for (int i = 0; i < passBytes.length; i++)
            result[i] = passBytes[i];
        return result;
    }

    public byte[] setTemp(float temp) {
        int secPart = (int) ((temp -((int)temp))*100);
        secPart = secPart | (1 << 7);
        byte[] tempCommand = new byte[4];
        tempCommand[0] = (byte) temp;
        tempCommand[1] = (byte)secPart;
        tempCommand[2] = 0x03;
        tempCommand[3] = 0x00;
        return tempCommand;
    }

    public int getCharge() {
        return serverResponse[14];
    }

    private static int getBit(byte id, int position) {
        return (id >> position) & 1;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Protocol) {
            Protocol op = (Protocol) o;
            return op.getTemp() == this.getTemp() && op.getState() == this.getState() && op.getDeviceStatus() == this.getDeviceStatus();
        } else if (o instanceof Device) {
            Device device = (Device) o;
            boolean temperature, isCharging, state, charge;
            temperature = Math.abs(this.getTemp() - device.getTemperature()) < 0.01;
            isCharging = this.isCharging() == device.isCharging();
            state = this.getState() != device.isDisabled();
            charge = this.getCharge() == device.getCharge();
            return temperature && isCharging && state && charge;
        }
        return super.equals(o);
    }

    public byte[] getDisable() {
        return new byte[]{0x02, 0x00};
    }
}
