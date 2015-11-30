package com.udtech.thinice.eventbus.model.cards;

import com.udtech.thinice.model.Day;

/**
 * Created by Sofi on 24.11.2015.
 */
public class TouchedCard {
    private Day day;
    public TouchedCard(Day day) {
        this.day = day;
    }

    public Day getDay() {
        return day;
    }
}
