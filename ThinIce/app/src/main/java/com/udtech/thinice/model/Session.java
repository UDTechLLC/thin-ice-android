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

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public void save() {
        super.save();
        day.recalc();
    }
    public void saveWithoutRecalc(){
        super.save();
    }
    public void addStep() {
        step++;
    }

    public long getStep() {
        return step;
    }

    public Integer getDutation() {
        return (int)(getEndTime().getTime()-getStartTime().getTime())/1000;
    }
    public Session merge(Session session){
        if(session.getEndTime()==null){
            return this;
        }
        session.step += step;
        session.endTime = new Date(session.endTime.getTime()+(getEndTime().getTime()-getStartTime().getTime()));
        session.temperature = (session.getDutation()*session.temperature+getDutation()*getTemperature())/(session.getDutation()+getDutation());
        this.delete();
        return session;
    }

    public void setSteps(int step) {
        this.step = step;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
