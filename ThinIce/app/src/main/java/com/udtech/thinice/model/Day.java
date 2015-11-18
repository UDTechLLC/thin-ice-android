package com.udtech.thinice.model;

import java.util.List;

/**
 * Created by JOkolot on 18.11.2015.
 */
public class Day {
    private List<Session> sessions;

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
