package com.udtech.thinice.model.devices;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by JOkolot on 18.11.2015.
 */
public class TShirt extends SugarRecord<TShirt> implements Device {
    private final static int MAX_TEMPERATURE = 50;
    private final static int MIN_TEMPERATURE = 15;
    private int temperature;
    private int charge;
    private long timer;
    private boolean disabled;
    public int getTemperature() {

        return temperature<MIN_TEMPERATURE?MIN_TEMPERATURE:(temperature>MAX_TEMPERATURE?MAX_TEMPERATURE:temperature);
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
    @Override
    public boolean equals(Object o) {
        if(o instanceof TShirt)
            return ((TShirt)o).getId().equals(getId());
        return super.equals(o);
    }
}
