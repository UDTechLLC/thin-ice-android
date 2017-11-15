package com.udtech.thinice.model;

import android.graphics.Color;
import android.util.Pair;

import com.orm.SugarRecord;
import com.udtech.thinice.UserSessionManager;
import com.udtech.thinice.model.devices.TShirt;
import com.udtech.thinice.model.users.User;
import com.udtech.thinice.protocol.CaloryesUtils;
import com.udtech.thinice.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

/**
 * Created by JOkolot on 18.11.2015.
 */
public class Day extends SugarRecord<Day> {
    private int gymHours, waterIntake, junkFood, hProteinMeals, hoursSlept, carbsConsumed;
    private Date date;
    private User user;
    private Float lastTemp;
    private Long totalTime;
    private Float totalCallories;
    private boolean isRecalcing = false;

    public float getTotalCallories() {
        return totalCallories;
    }

    public Pair<Long, Long> getAVGTempWithTimeCoficient() {
        List<Session> sessions = Session.find(Session.class, "day = " + getId(), null);
        List<Pair<Long, Long>> result = new ArrayList<>();
        for (Session session : sessions) {
            result.add(new Pair<Long, Long>((long) session.getTemperature(), (long) session.getDutation()));
        }
        long tempSum = 0;
        long timeSum = 0;
        for (Pair<Long, Long> pair : result) {
            tempSum += pair.first * pair.second;
            timeSum += pair.second;
        }
        if (timeSum != 0)
            return new Pair<>(tempSum / timeSum, timeSum);
        return new Pair<Long, Long>((long) 0, (long) 0);
    }

    public void disableStatistics() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Session> sessions = Session.find(Session.class, "day = " + getId(), null);
                if (sessions != null ? sessions.size() > 0 : false) {
                    for (Session session : sessions) {
                        session.clearStatistics();
                    }
                }
            }
        }).start();
    }

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


    public Date getDate() {
        return date;
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof Day)
            return ((Day) o).getDate().getTime() == date.getTime();
        return super.equals(o);
    }

    public synchronized float getTotalCalories() {
        if (totalCallories == null)
            recalc();
        return totalCallories;
    }

    public Pair<Long, Float> getTotalDataForStatistics() {
        long totalTime = 0;
        long stepsCount = 0;
        List<Session> sessions = Session.find(Session.class, "day = " + getId(), null);
        if (sessions != null ? sessions.size() > 0 : false) {
            float totalCalories = 0;
            for (Session session : sessions) {
                if (session.isForStatistics()) {
                    float time = (session.getEndTime().getTime() - session.getStartTime().getTime());
                    totalCalories += (time * CaloryesUtils.getBurningSpeedPerSecond(session.getTemperature()) / 1000f) + (long) ((session.getStep() / 12500f) * 500f);
                    totalTime += time;
                }
            }
            this.totalCallories = (float) totalCalories;

            try {
                totalTime += new SimpleDateFormat("HH mm ss").parse("00 00 00").getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            return new Pair<>((long) 0, (float) 0);
        }
        return new Pair<>(totalTime, totalCallories);
    }

    public void recalc() {
        if (!isRecalcing) {
            isRecalcing = true;
            recalcLastTemp();
            totalTime = Long.valueOf(0);
            this.totalCallories = new Float(Long.valueOf(0));
            List<Session> sessions = Session.find(Session.class, "day = " + getId(), null);
            if (sessions != null ? sessions.size() > 0 : false) {
                float totalCalories = 0;
                for (Session session : sessions) {
                    float time = (session.getEndTime().getTime() - session.getStartTime().getTime());
                    totalCalories += (time * CaloryesUtils.getBurningSpeedPerSecond(session.getTemperature()) / 1000f) + (long) ((session.getStep() / 12500f) * 500f);
                    totalTime += (long) time;
                }
                this.totalCallories = (float) (long) totalCalories;
            }
            try {
                totalTime += new SimpleDateFormat("HH mm ss").parse("00 00 00").getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            isRecalcing = false;
        } else {
            while (isRecalcing) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public synchronized int getLastTemp() {
        if (lastTemp == null)
            recalc();
        return (int) (float) lastTemp;
    }

    public synchronized void recalcLastTemp() {
        List<Session> sessions = Session.find(Session.class, "day = " + getId(), null);
        if (sessions != null ? sessions.size() > 0 : false) {
            int lastTemp = TShirt.MIN_TEMPERATURE;
            long lastDate = 0;
            for (Session session : sessions) {
                if (session.getEndTime().getTime() > lastDate) {
                    lastDate = session.getEndTime().getTime();
                    lastTemp = session.getTemperature();
                }
            }
            this.lastTemp = Float.valueOf(lastTemp);
        } else {
            this.lastTemp = Float.valueOf(TShirt.MIN_TEMPERATURE);
        }
    }

    public synchronized long getTotalTime() {
        if (totalTime == null)
            recalc();
        return totalTime;
    }

    public synchronized int getHeaderColor() {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case 1: {
                return Color.rgb(114, 85, 151);
            }
            case 2: {
                return Color.rgb(81, 76, 148);
            }
            case 3: {
                return Color.rgb(53, 83, 123);
            }
            case 4: {
                return Color.rgb(36, 125, 170);
            }
            case 5: {
                return Color.rgb(70, 163, 151);
            }
            case 6: {
                return Color.rgb(27, 109, 101);
            }
            case 7: {
                return Color.rgb(27, 109, 101);
            }
        }
        return 0;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public void save() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        Calendar calendar2 = new GregorianCalendar();
        Iterator<Day> dayList = Day.findAll(Day.class);
        boolean created = false;
        while (dayList.hasNext()) {
            Day day = dayList.next();
            if (day.getUser().getId() == getUser().getId()) {
                calendar2.setTime(day.date);
                if (calendar.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR) && calendar.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)) {
                    created = true;
                    break;
                }
            }
        }
        if (!created)
            super.save();
    }
}
