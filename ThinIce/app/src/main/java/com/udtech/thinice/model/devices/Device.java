package com.udtech.thinice.model.devices;

import java.util.Date;

/**
 * Created by JOkolot on 18.11.2015.
 */
public abstract interface Device {
    public float getTemperature();

    public void setTemperature(float temperature);

    public int getCharge();

    public void setCharge(int charge);

    public void setTimer(Date date);

    public Date getTimer();
    public int getMax();
    public int getMin();
    public boolean isDisabled();
    public Long getId();
    public void setDisabled(boolean disabled);
    public void save();
    public void delete();
    @Override
    public boolean equals(Object o);

}
