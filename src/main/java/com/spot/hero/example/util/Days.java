package com.spot.hero.example.util;

import java.time.DayOfWeek;

public enum Days {
    mon,
    tues,
    wed,
    thurs,
    fri,
    sat,
    sun;

    public static String fromDayOfWeek(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY: return mon.name();
            case TUESDAY: return tues.name();
            case WEDNESDAY: return wed.name();
            case THURSDAY: return thurs.name();
            case FRIDAY: return fri.name();
            case SATURDAY: return sat.name();
            case SUNDAY: return sun.name();
            default: return null;
        }

    }
}