package com.udtech.thinice.eventbus.model.cards;

import com.udtech.thinice.model.Day;

/**
 * Created by JOkolot on 23.11.2015.
 */
public class UpdateCard {
    private Day day;

    public UpdateCard(Day day) {
        this.day = day;
    }

    public Day getDay() {
        return day;
    }
}
