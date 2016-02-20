package com.udtech.thinice.model;

import com.orm.SugarRecord;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JOkolot on 08.02.2016.
 */
public class Steps extends SugarRecord<Steps> {
    private String date;
    private long steps;

    public static Steps createNewTodaySteps() {
        Steps steps = new Steps();
        steps.date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return steps;
    }

    public Steps() {
    }

    public void incrementSteps() {
        steps++;
    }

    public long getSteps() {
        return steps;
    }

    public String getDate() {
        return date;
    }
}
