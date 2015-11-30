package com.udtech.thinice.model.devices;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by JOkolot on 18.11.2015.
 */
public class Insole extends SugarRecord<Insole> implements Device  {
    private int temperature;
    private int charge;
    private long timer;
    private boolean disabled;
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
    public void setTimer(Date date){
        this.timer = date.getTime();
    }

    public Date getTimer() {
        return new Date(timer);
    }


    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
