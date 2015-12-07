package com.udtech.thinice.model;

import android.content.Context;
import android.graphics.Color;

import com.orm.SugarRecord;
import com.udtech.thinice.model.users.User;
import com.udtech.thinice.utils.SessionManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by JOkolot on 18.11.2015.
 */
public class Day extends SugarRecord<Day> {
    private int gymHours, waterIntake,junkFood,hProteinMeals,hoursSlept,carbsConsumed;
    private Date date;
    private User user;

    public Day() {
    }
    public Day(User user) {
        date = new Date();
        this.user = user;
//        EventBus.getDefault().register(this);
    }

    public User getUser() {
        return user;
    }


    public int getGymHours() {
        return gymHours;
    }

    public void setGymHours(int gymHours) {
        this.gymHours = gymHours;
    }

    public int getWaterIntake() {
        return waterIntake;
    }

    public void setWaterIntake(int waterIntake) {
        this.waterIntake = waterIntake;
    }

    public int getJunkFood() {
        return junkFood;
    }

    public void setJunkFood(int junkFood) {
        this.junkFood = junkFood;
    }

    public int gethProteinMeals() {
        return hProteinMeals;
    }

    public void sethProteinMeals(int hProteinMeals) {
        this.hProteinMeals = hProteinMeals;
    }

    public int getHoursSlept() {
        return hoursSlept;
    }

    public void setHoursSlept(int hoursSlept) {
        this.hoursSlept = hoursSlept;
    }

    public int getCarbsConsumed() {
        return carbsConsumed;
    }

    public void setCarbsConsumed(int carbsConsumed) {
        this.carbsConsumed = carbsConsumed;
    }

    public int getAverageTemp(Context context) {
        List<Session> sessions = Session.find(Session.class,"day = "+getId(),null);
        if (sessions != null ? sessions.size() > 0 : false) {
            int sumTemp = 0;
            for (Session session : sessions) {
                sumTemp += session.getTemperature();
            }
            return sumTemp / sessions.size();
        } else {
            return 0;
        }
    }

    public Date getDate() {
        return date;
    }

    public int calcCalories() {List<Session> sessions = Session.find(Session.class,"day = "+getId(),null);
        if (sessions != null ? sessions.size() > 0 : false) {
            int sumTemp = 0;
            for (Session session : sessions) {
                sumTemp += SessionManager.getCaloriesRatePerSecondPerSession(session)*(session.getEndTime().getTime()-session.getStartTime().getTime())/1000;
            }
            return sumTemp;
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof  Day)
            return ((Day)o).getDate().getTime() == date.getTime();
        return super.equals(o);
    }

    public float getTotalCalories() {
        List<Session> sessions = Session.find(Session.class,"day = "+getId(),null);
        if (sessions != null ? sessions.size() > 0 : false) {
            float totalCalories = 0;
            for (Session session: sessions){
                float time = (session.getEndTime().getTime() -session.getStartTime().getTime())/1000;
                totalCalories += time*SessionManager.getCaloriesRatePerSecondPerSession(session);
            }
            return totalCalories;
        } else {
            return 0;
        }
    }

    public long getTotalTime() {
        List<Session> sessions = Session.find(Session.class,"day = "+getId(),null);
        if (sessions != null ? sessions.size() > 0 : false) {
            long time = 0;
            for (Session session: sessions){
                time += (session.getEndTime().getTime() -session.getStartTime().getTime());
            }
            return time;
        } else {
            return 0;
        }
    }

    public int getHeaderColor() {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek){
            case 1:{
                return Color.rgb(114,85,151);
            }
            case 2:{
                return Color.rgb(81,76,148);
            }
            case 3:{
                return Color.rgb(53,83,123);
            }
            case 4:{
                return Color.rgb(36,125,170);
            }
            case 5:{
                return Color.rgb(70,163,151);
            }
            case 6:{
                return Color.rgb(27,109,101);
            }
            case 7:{
                return Color.rgb(27,109,101);
            }
        }
        return 0;
    }
}
