package com.udtech.thinice.model.devices;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by JOkolot on 18.11.2015.
 */
public class Insole extends SugarRecord<Insole> implements Device  {
    private final static int MAX_TEMPERATURE = 40;
    private final static int MIN_TEMPERATURE = 15;
    private float temperature;
    private int charge;
    private long timer;
    private boolean disabled;
    public float getTemperature() {
        return temperature<MIN_TEMPERATURE?MIN_TEMPERATURE:(temperature>MAX_TEMPERATURE?MAX_TEMPERATURE:temperature);
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
    public void setTimer(Date date){
        this.timer = date.getTime();
    }

    public Date getTimer() {
        return new Date(timer);
    }

    @Override
    public int getMax() {
        return MAX_TEMPERATURE;
    }

    @Override
    public int getMin() {
        return MIN_TEMPERATURE;
    }


    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
    @Override
    public boolean equals(Object o) {
        if(o instanceof Insole)
            return ((Insole)o).getId().equals(getId());
        return super.equals(o);
    }
}
