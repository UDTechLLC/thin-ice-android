package com.udtech.thinice.eventbus.model;

import com.udtech.thinice.model.devices.Device;

/**
 * Created by JOkolot on 19.02.2016.
 */
public class BluetoothCommand {
    public final static int START = 1;
    public final static int STOP = 2;
    public final static int SET_TEMP = 3;
    public final static int GET_TEMP = 4;

    private int command;
    private int value;
    private Device device;
    public BluetoothCommand(Device device) {
        this.device = device;
    }

    public void setTemperature( int value){
        this.command = SET_TEMP;
        this.value = value;
    }
    public void setStartCommand(){
        command = START;
    }
    public void setStopCommand(){
        command = STOP;
    }
    public void getTemperatureCommand(){
        command = GET_TEMP;
    }
    public int getCommand() {
        return command;
    }

    public int getValue() {
        return value;
    }

    public Device getDevice() {
        return device;
    }
}
