package com.udtech.thinice.model;

/**
 * Created by Sofi on 01.12.2015.
 */
public class Achievement {
    private int resourceSrc;
    private String name;
    private String description;
    private boolean opened;
    private int id;
    private int bigResourceSrc;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBigResourceSrc() {
        return bigResourceSrc;
    }

    public void setBigResourceSrc(int bigResourceSrc) {
        this.bigResourceSrc = bigResourceSrc;
    }

    public int getResourceSrc() {
        return resourceSrc;
    }

    public void setResourceSrc(int resourceSrc) {
        this.resourceSrc = resourceSrc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public class Type {
        public static final int FRESH_START = 0;
        public static final int MOVING_FORWARD = 1;
        public static final int THE_ENTHUSIAST = 2;
        public static final int THE_MOTIVATED = 3;
        public static final int THE_MARATHONER = 4;
        public static final int THE_DABBLER = 5;
        public static final int THE_SCHEMER = 6;
        public static final int THE_STRATEGIST = 7;
        public static final int FIRESTARTER = 8;
        public static final int FEELIN_THE_BURN = 9;
        public static final int GETTING_LEAN = 10;
        public static final int SEEING_RESULTS = 11;
        public static final int THE_BUTTON_PRESSER = 12;
        public static final int FRESH_FACE = 13;
        public static final int THE_TRACKER = 14;
        public static final int RESULTS_ORIENTED = 15;
        public static final int RESULTS_OBSESSED = 16;
    }
}
