package com.udtech.thinice.model.devices;

import com.orm.SugarRecord;
import com.udtech.thinice.protocol.Protocol;

import java.util.Date;

/**
 * Created by JOkolot on 18.11.2015.
 */
public class TShirt implements Device {
    public final static int MAX_TEMPERATURE = 20;
    public final static int MIN_TEMPERATURE = 10;
    private float temperature;
    private int charge;
    private long timer;
    private boolean disabled;
    private boolean isCharging;
    private String name;

    public float getTemperature() {
        return temperature < MIN_TEMPERATURE ? MIN_TEMPERATURE : (temperature > MAX_TEMPERATURE ? MAX_TEMPERATURE : temperature);
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public Date getTimer() {
        return new Date(timer);
    }

    public void setTimer(Date date) {
        this.timer = date.getTime();
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public Long getId() {
        return Long.valueOf(1);
    }

    @Override
    public void save() {

    }

    @Override
    public void delete() {

    }

    @Override
    public boolean isCharging() {
        return isCharging;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String string) {
        name  = string;
    }

    @Override
    public void fetch(Protocol protocol) {
        isCharging = protocol.isCharging();
        temperature = protocol.getTemp();
        charge = protocol.getCharge();
        disabled = !protocol.getState();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TShirt)
            return ((TShirt) o).getId().equals(getId());
        return super.equals(o);
    }

    @Override
    public int getMax() {
        return MAX_TEMPERATURE;
    }

    @Override
    public int getMin() {
        return MIN_TEMPERATURE;
    }
}
