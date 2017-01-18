package com.udtech.thinice.model.devices;

import com.udtech.thinice.protocol.Protocol;

import java.util.Date;

/**
 * Created by JOkolot on 18.11.2015.
 */
public abstract interface Device {
    public float getTemperature();

    public void setTemperature(float temperature);

    public int getCharge();

    public void setCharge(int charge);

    public Date getTimer();

    public void setTimer(Date date);

    public int getMax();

    public int getMin();

    public boolean isDisabled();

    public void setDisabled(boolean disabled);

    public Long getId();

    public void save();

    public void delete();

    public boolean isCharging();

    public String getName();
    public void setName(String string);

    public void fetch(Protocol protocol);

    @Override
    public boolean equals(Object o);

}
