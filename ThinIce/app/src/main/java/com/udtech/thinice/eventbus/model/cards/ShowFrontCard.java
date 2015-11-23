package com.udtech.thinice.eventbus.model.cards;

/**
 * Created by JOkolot on 23.11.2015.
 */
public class ShowFrontCard {
    private boolean reversed;

    public ShowFrontCard() {
        reversed = false;
    }

    public ShowFrontCard(boolean reversed) {
        this.reversed = reversed;
    }

    public boolean isReversed() {
        return reversed;
    }
}
