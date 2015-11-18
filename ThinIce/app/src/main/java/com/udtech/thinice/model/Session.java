package com.udtech.thinice.model;

import java.util.Date;

/**
 * Created by JOkolot on 05.11.2015.
 */
public class Session {
    private Date startTime;
    private Date endTime;
    private int temperature;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
