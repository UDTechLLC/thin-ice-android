package com.udtech.thinice.model;

import android.content.Context;

import com.orm.SugarRecord;
import com.udtech.thinice.model.users.User;

import java.util.ArrayList;
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
    private Date timeUsed;
    private User user;
    public Day() {
        date = new Date();
        EventBus.getDefault().register(this);
    }
    public Day(User user) {
        date = new Date();
        this.user = user;
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
        List<Session> sessions = Session.find(Session.class,"date = "+getId(),null);
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

}
