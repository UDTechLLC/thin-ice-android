package com.udtech.thinice.model;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by JOkolot on 05.11.2015.
 */
public class Session extends SugarRecord<Session> {
    private Day day;
    private Date startTime;
    private Date endTime;
    private int temperature;

    public Session(Day day) {
        this.day = day;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public int getTemperature() {
        return temperature;
    }

    public void openSession(int temperature){
        startTime = new Date();
        this.temperature = temperature;
    }
}
