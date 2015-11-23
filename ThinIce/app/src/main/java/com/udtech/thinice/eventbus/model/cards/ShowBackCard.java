package com.udtech.thinice.eventbus.model.cards;

import com.udtech.thinice.model.Day;

/**
 * Created by JOkolot on 23.11.2015.
 */
public class ShowBackCard {
    private Day day;
    private boolean reversed;

    public ShowBackCard(Day day, boolean reversed) {
        this.day = day;
        this.reversed = reversed;
    }

    public ShowBackCard(Day day) {
        this.day = day;
        reversed = false;
    }

    public boolean isReversed() {
        return reversed;
    }

    public Day getDay() {
        return day;
    }
}
