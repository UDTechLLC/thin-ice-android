package com.udtech.thinice.model;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JOkolot on 18.11.2015.
 */
public class Day extends SugarRecord<Day> {
    private List<Session> sessions;
    private int gymHours, waterIntake,junkFood,hProteinMeals,hoursSlept,carbsConsumed;
    public Day() {
        this.sessions = new ArrayList<>();
    }

    public void addSession(Session session) {
        sessions.add(session);
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

    public int getAverageTemp() {
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
}
