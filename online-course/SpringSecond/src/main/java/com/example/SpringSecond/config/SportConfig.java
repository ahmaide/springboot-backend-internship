package com.example.SpringSecond.config;

import com.example.SpringSecond.common.Coach;
import com.example.SpringSecond.common.SwimCoach;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SportConfig {

    @Bean("fateh")
    public Coach swimCoach(){
        return new SwimCoach();
    }
}
