package com.example.SpringSecond.rest;

import com.example.SpringSecond.common.Coach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    private Coach myCoach;
    @Autowired
    public DemoController(@Qualifier("fateh") Coach coach){
        System.out.println("In constructor: " + getClass().getSimpleName());
        this.myCoach =  coach;
    }

    @GetMapping("/dailyworkout")
    public String getDailyWorkout(){
        return myCoach.getDailyWorkout();
    }

}
