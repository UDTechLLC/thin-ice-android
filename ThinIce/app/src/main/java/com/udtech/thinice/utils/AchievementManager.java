package com.udtech.thinice.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.udtech.thinice.R;
import com.udtech.thinice.UserSessionManager;
import com.udtech.thinice.model.Achievement;
import com.udtech.thinice.model.Day;
import com.udtech.thinice.model.Session;
import com.udtech.thinice.model.users.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import de.greenrobot.event.EventBus;

/**
 * Created by Sofi on 01.12.2015.
 */
public class AchievementManager {
    private static final String NAME = "acvmnt";
    private static final String TEMP = "acvmnt";
    private static final String COMPLETED = "compltd";

    private static final String TIME = "time";
    private static final String TARGETS = "targets";
    private static final String CALORIES = "calories";
    private static final String STATISTICS = "statistics";

    private static final int FRESH_START_COUNT = 30;
    private static final int MOVING_FORWARD_COUNT = 600;
    private static final int THE_MOTIVATED_COUNT = 6000;
    private static final int THE_ENTHUSIAST_COUNT = 30000;
    private static final int THE_MARATHONER_COUNT = 60000;
    private static final int THE_DABBLER_COUNT = 1;
    private static final int THE_SCHEMER_COUNT = 5;
    private static final int THE_STRATEGIST_COUNT = 20;
    private static final int FIRESTARTER_COUNT = 100;
    private static final int FEELIN_THE_BURN_COUNT = 1000;
    private static final int GETTING_LEAN_COUNT = 10000;
    private static final int SEEING_RESULTS_COUNT = 50000;
    private static final int THE_BUTTON_PRESSER_COUNT = 1;
    private static final int FRESH_FACE_COUNT = 1;
    private static final int THE_TRACKER_COUNT = 5;
    private static final int RESULTS_ORIENTED_COUNT = 50;
    private static final int RESULTS_OBSESSED_COUNT = 500;

    private volatile static AchievementManager instance;

    private AtomicLong timeSpending;
    private AtomicInteger targets;
    private AtomicInteger calories;
    private AtomicInteger statistics;

    public static AchievementManager getInstance(Context context) {
        if (instance == null)
            instance = new AchievementManager(context);
        return instance;
    }

    private AchievementManager(Context context) {
        User user = UserSessionManager.getSession(context);
        SharedPreferences sPref = context.getSharedPreferences(TEMP + user.getId(), Context.MODE_PRIVATE);
        this.timeSpending = new AtomicLong(sPref.getLong(TIME, 0));
        this.targets = new AtomicInteger(sPref.getInt(TARGETS, 0));
        this.calories = new AtomicInteger(sPref.getInt(CALORIES, 0));
        this.statistics = new AtomicInteger(sPref.getInt(STATISTICS, 0));
    }

    private void saveAchievement(Achievement achievement, Context context) {
        User user = UserSessionManager.getSession(context);
        SharedPreferences sPref = context.getSharedPreferences(NAME + user.getId(), Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(COMPLETED + achievement.getId(), achievement.isOpened());
        ed.commit();
    }

    public synchronized void sessionClosed(Context context, Session session) {
        User user = UserSessionManager.getSession(context);
        long timeDif = (session.getEndTime().getTime() - session.getStartTime().getTime());
        timeSpending.addAndGet(timeDif);
        if ((timeSpending.get() / 60000) > FRESH_START_COUNT)
            showAchievement(getAchievement(context, Achievement.Type.FRESH_START),context);
        if ((timeSpending.get() / 60000) > MOVING_FORWARD_COUNT)
            showAchievement(getAchievement(context, Achievement.Type.MOVING_FORWARD),context);
        if ((timeSpending.get() / 60000) > THE_MOTIVATED_COUNT)
            showAchievement(getAchievement(context, Achievement.Type.THE_MOTIVATED),context);
        if ((timeSpending.get() / 60000) > THE_ENTHUSIAST_COUNT)
            showAchievement(getAchievement(context, Achievement.Type.THE_ENTHUSIAST),context);
        if ((timeSpending.get() / 60000) > THE_MARATHONER_COUNT)
            showAchievement(getAchievement(context, Achievement.Type.THE_MARATHONER),context);
        List<Day> days = new ArrayList<>();
        Iterator<Day> daysIterator = Day.findAll(Day.class);
        while (daysIterator.hasNext()) {
            Day day = daysIterator.next();
            if (user.equals(day.getUser())) {
                days.add(day);
            }
        }
        int totalCalories = 0;
        for (Day day : days)
            totalCalories += day.calcCalories();
        if(totalCalories > FIRESTARTER_COUNT )
            showAchievement(getAchievement(context, Achievement.Type.FIRESTARTER),context);
        if(totalCalories > FEELIN_THE_BURN_COUNT)
            showAchievement(getAchievement(context, Achievement.Type.FEELIN_THE_BURN),context);
        if(totalCalories > GETTING_LEAN_COUNT )
            showAchievement(getAchievement(context, Achievement.Type.GETTING_LEAN),context);
        if(totalCalories > SEEING_RESULTS_COUNT )
            showAchievement(getAchievement(context, Achievement.Type.SEEING_RESULTS),context);

    }
    public synchronized void settingsChanged(Context context){
        showAchievement(getAchievement(context, Achievement.Type.THE_BUTTON_PRESSER),context);
    }
    public synchronized void registrationCompleted(Context context){
        showAchievement(getAchievement(context, Achievement.Type.FRESH_FACE),context);
    }
    public synchronized void statisticsOpened(Context context){
        statistics.incrementAndGet();
        if(statistics.get()>THE_TRACKER_COUNT)
            showAchievement(getAchievement(context, Achievement.Type.THE_TRACKER),context);
        if(statistics.get()>RESULTS_ORIENTED_COUNT)
            showAchievement(getAchievement(context, Achievement.Type.RESULTS_ORIENTED),context);
        if(statistics.get()>RESULTS_OBSESSED_COUNT)
            showAchievement(getAchievement(context, Achievement.Type.RESULTS_OBSESSED),context);
    }
    public synchronized void dayChanged(Context context){
        int count = 0;
        User user = UserSessionManager.getSession(context);
        List<Day> days = new ArrayList<>();
        Iterator<Day> daysIterator = Day.findAll(Day.class);
        while (daysIterator.hasNext()) {
            Day day = daysIterator.next();
            if (user.equals(day.getUser())) {
                days.add(day);
            }
        }
        for(Day day: days){
            if(day.getGymHours()!=0)
                count++;
            if(day.gethProteinMeals()!=0)
                count++;
            if(day.getWaterIntake()!=0)
                count++;
            if(day.getHoursSlept()!=0)
                count++;
            if(day.getJunkFood()!=0)
                count++;
            if(day.getCarbsConsumed()!=0)
                count++;
        }
        if(count>0){
            showAchievement(getAchievement(context, Achievement.Type.THE_DABBLER),context);
        }
        if(count>=5){
            showAchievement(getAchievement(context, Achievement.Type.THE_SCHEMER),context);
        }
        if(count>20){
            showAchievement(getAchievement(context, Achievement.Type.THE_STRATEGIST),context);
        }
    }

    public void commit(Context context) {
        User user = UserSessionManager.getSession(context);
        SharedPreferences sPref = context.getSharedPreferences(TEMP + user.getId(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putLong(TIME, timeSpending.get());
        editor.putInt(TARGETS, targets.get());
        editor.putInt(CALORIES, calories.get());
        editor.putInt(STATISTICS, statistics.get());
    }

    public List<Achievement> getAchievements(Context context) {
        List<Achievement> achievements = new ArrayList<>();
        achievements.add(getAchievement(context, Achievement.Type.FEELIN_THE_BURN));
        achievements.add(getAchievement(context, Achievement.Type.FIRESTARTER));
        achievements.add(getAchievement(context, Achievement.Type.FRESH_FACE));
        achievements.add(getAchievement(context, Achievement.Type.FRESH_START));
        achievements.add(getAchievement(context, Achievement.Type.GETTING_LEAN));
        achievements.add(getAchievement(context, Achievement.Type.MOVING_FORWARD));
        achievements.add(getAchievement(context, Achievement.Type.RESULTS_OBSESSED));
        achievements.add(getAchievement(context, Achievement.Type.RESULTS_ORIENTED));
        achievements.add(getAchievement(context, Achievement.Type.SEEING_RESULTS));
        achievements.add(getAchievement(context, Achievement.Type.THE_ENTHUSIAST));
        achievements.add(getAchievement(context, Achievement.Type.THE_MOTIVATED));
        achievements.add(getAchievement(context, Achievement.Type.THE_MARATHONER));
        achievements.add(getAchievement(context, Achievement.Type.THE_DABBLER));
        achievements.add(getAchievement(context, Achievement.Type.THE_SCHEMER));
        achievements.add(getAchievement(context, Achievement.Type.THE_STRATEGIST));
        achievements.add(getAchievement(context, Achievement.Type.THE_BUTTON_PRESSER));
        achievements.add(getAchievement(context, Achievement.Type.THE_TRACKER));
        Collections.sort(achievements, new Sorter());
        return achievements;
    }

    public Achievement getAchievement(Context context, int id) {
        User user = UserSessionManager.getSession(context);
        SharedPreferences sPref = context.getSharedPreferences(NAME + user.getId(), Context.MODE_PRIVATE);
        Achievement achievement = new Achievement();
        achievement.setId(id);
        achievement.setName(getName(id));
        achievement.setResourceSrc(getSourceId(id));
        achievement.setOpened(sPref.getBoolean(COMPLETED + id, false));
        achievement.setDescription(getDescription(id));
        achievement.setBigResourceSrc(getBigSourceId(id));
        return achievement;
    }

    private int getSourceId(int id) {
        int resId = 0;
        switch (id) {
            case 0: {
                resId = R.mipmap.ic_fresh_start;
                break;
            }
            case 1: {
                resId = R.mipmap.ic_moving_forward;
                break;
            }
            case 2: {
                resId = R.mipmap.ic_the_enthusiast;
                break;
            }
            case 3: {
                resId = R.mipmap.ic_the_motivated;
                break;
            }
            case 4: {
                resId = R.mipmap.ic_the_marathoner;
                break;
            }
            case 5: {
                resId = R.mipmap.ic_the_dubbler;
                break;
            }
            case 6: {
                resId = R.mipmap.ic_the_schemer;
                break;
            }
            case 7: {
                resId = R.mipmap.ic_the_strategist;
                break;
            }
            case 8: {
                resId = R.mipmap.ic_firestarter;
                break;
            }
            case 9: {
                resId = R.mipmap.ic_feeling_burn;
                break;
            }
            case 10: {
                resId = R.mipmap.ic_getting_lean;
                break;
            }
            case 11: {
                resId = R.mipmap.ic_seeng_results;
                break;
            }
            case 12: {
                resId = R.mipmap.ic_the_button_presser;
                break;
            }
            case 13: {
                resId = R.mipmap.ic_the_fresh_face;
                break;
            }
            case 14: {
                resId = R.mipmap.ic_the_tracker;
                break;
            }
            case 15: {
                resId = R.mipmap.ic_result_oriented;
                break;
            }
            case 16: {
                resId = R.mipmap.ic_results_obsessed;
                break;
            }
        }
        return resId;
    }

    private int getBigSourceId(int id) {
        int resId = 0;
        switch (id) {
            case 0: {
                resId = R.mipmap.ic_fresh_start_big;
                break;
            }
            case 1: {
                resId = R.mipmap.ic_moving_forward_big;
                break;
            }
            case 2: {
                resId = R.mipmap.ic_the_enthusiast_big;
                break;
            }
            case 3: {
                resId = R.mipmap.ic_the_motivated_big;
                break;
            }
            case 4: {
                resId = R.mipmap.ic_the_marathoner_big;
                break;
            }
            case 5: {
                resId = R.mipmap.ic_the_dubbler_big;
                break;
            }
            case 6: {
                resId = R.mipmap.ic_the_schemer_big;
                break;
            }
            case 7: {
                resId = R.mipmap.ic_the_strategist_big;
                break;
            }
            case 8: {
                resId = R.mipmap.ic_firestarter_big;
                break;
            }
            case 9: {
                resId = R.mipmap.ic_feeling_burn_big;
                break;
            }
            case 10: {
                resId = R.mipmap.ic_getting_lean_big;
                break;
            }
            case 11: {
                resId = R.mipmap.ic_seeng_results_big;
                break;
            }
            case 12: {
                resId = R.mipmap.ic_the_button_presser_big;
                break;
            }
            case 13: {
                resId = R.mipmap.ic_the_fresh_face_big;
                break;
            }
            case 14: {
                resId = R.mipmap.ic_the_tracker_big;
                break;
            }
            case 15: {
                resId = R.mipmap.ic_result_oriented_big;
                break;
            }
            case 16: {
                resId = R.mipmap.ic_results_obsessed_big;
                break;
            }
        }
        return resId;
    }

    private String getName(int id) {
        String name = "";
        switch (id) {
            case 0: {
                name = "Fresh Start";
                break;
            }
            case 1: {
                name = "Moving Forward";
                break;
            }
            case 2: {
                name = "The Enthusiast";
                break;
            }
            case 3: {
                name = "The Motivated";
                break;
            }
            case 4: {
                name = "The Marathoner";
                break;
            }
            case 5: {
                name = "The Dabbler";
                break;
            }
            case 6: {
                name = "The Schemer";
                break;
            }
            case 7: {
                name = "The Strategist";
                break;
            }
            case 8: {
                name = "Firestarter";
                break;
            }
            case 9: {
                name = "Feelin’ the Burn";
                break;
            }
            case 10: {
                name = "Gettin’ Lean";
                break;
            }
            case 11: {
                name = "Seeing Results!";
                break;
            }
            case 12: {
                name = "The Button Presser";
                break;
            }
            case 13: {
                name = "Fresh Face";
                break;
            }
            case 14: {
                name = "The Tracker";
                break;
            }
            case 15: {
                name = "Results Oriented";
                break;
            }
            case 16: {
                name = "Results Obsessed";
                break;
            }
        }
        return name;
    }

    private String getDescription(int id) {
        String description = "";
        switch (id) {
            case 0: {
                description = "You used Thin Ice clothing for your very first 30 minutes!";
                break;
            }
            case 1: {
                description = "You have used Thin Ice clothing for 10 hours!";
                break;
            }
            case 2: {
                description = "You have used Thin Ice clothing for 500 hours!";
                break;
            }
            case 3: {
                description = "You have used Thin Ice clothing for 100 hours!";
                break;
            }
            case 4: {
                description = "You have used Thin Ice clothing for 1000 hours!";
                break;
            }
            case 5: {
                description = "You have successfully created a personal goal!";
                break;
            }
            case 6: {
                description = "You have successfully created 5 personal goals!";
                break;
            }
            case 7: {
                description = "You have successfully created 20 personal goals!";
                break;
            }
            case 8: {
                description = "You have burnt your first 100 calories with Thin Ice clothing!";
                break;
            }
            case 9: {
                description = "You have burnt 1000 calories with Thin Ice clothing!";
                break;
            }
            case 10: {
                description = "You have burnt 10,000 calories with Thin Ice clothing!";
                break;
            }
            case 11: {
                description = "You have burnt 50,000 calories with Thin Ice clothing!";
                break;
            }
            case 12: {
                description = "You have changed a setting!";
                break;
            }
            case 13: {
                description = "You have registered your first Thin Ice profile!";
                break;
            }
            case 14: {
                description = "You’ve checked the tracking screen 5 times!";
                break;
            }
            case 15: {
                description = "You’ve checked the tracking screen 50 times!";
                break;
            }
            case 16: {
                description = "You’ve checked the tracking screen 500 times!";
                break;
            }
        }
        return description;
    }

    private void showAchievement(Achievement achievement,Context context) {
        if(!achievement.isOpened()){
            achievement.setOpened(true);
            saveAchievement(achievement, context);
            EventBus.getDefault().postSticky(achievement);
        }
    }

    private static class Sorter implements Comparator<Achievement> {

        @Override
        public int compare(Achievement lhs, Achievement rhs) {
            return lhs.getId() == rhs.getId() ? 0 : (lhs.getId() > rhs.getId() ? 1 : -1);
        }

        @Override
        public boolean equals(Object object) {
            return false;
        }
    }

}
