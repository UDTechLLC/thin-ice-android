package com.udtech.thinice.model;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by JOkolot on 05.11.2015.
 */
public class Session extends SugarRecord {
    private Day day;
    private Date startTime;
    private Date endTime;
    private int temperature;
    private long step;
    private boolean statistics;
    public void clearStatistics(){
        statistics = false;
        save();
    }
    public boolean isForStatistics() {
        return statistics;
    }

    public Session() {
    }

    public Session(Day day) {
        this.day = day;
        statistics = true;
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


    public Day getDay() {
        return day;
    }

    public void openSession(int temperature) {
        startTime = new Date();
        this.temperature = temperature;
    }

    public void closeSession() {
        if (startTime != null) {
            endTime = new Date();
            save();
        }
    }

    @Override
    public long save() {
        long id = super.save();
        day.recalc();
        return id;
    }

    public void addStep() {
        step++;
    }

    public long getStep() {
        return step;
    }
}
