package com.example.SpringSecond.common;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class TrackCoach implements Coach{

    public TrackCoach(){
        System.out.println("In constructor: " + getClass().getSimpleName());
    }
    @Override
    public String getDailyWorkout() {
        return "Runnnnnnnnnnnn Nigga";
    }
}
