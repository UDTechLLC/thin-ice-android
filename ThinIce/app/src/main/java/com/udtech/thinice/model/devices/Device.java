package com.udtech.thinice.model.devices;

import java.util.Date;

/**
 * Created by JOkolot on 18.11.2015.
 */
public abstract interface Device {
    public int getTemperature();

    public void setTemperature(int temperature);

    public int getCharge();

    public void setCharge(int charge);

    public void setTimer(Date date);

    public Date getTimer();

    public boolean isDisabled();
    public Long getId();
    public void setDisabled(boolean disabled);
    public void save();
    public void delete();
    @Override
    public boolean equals(Object o);

}
