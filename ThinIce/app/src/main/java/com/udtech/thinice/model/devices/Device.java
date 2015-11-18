package com.udtech.thinice.model.devices;

/**
 * Created by JOkolot on 18.11.2015.
 */
public abstract class Device {
    private int temperature;
    private int charge;

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }
}
