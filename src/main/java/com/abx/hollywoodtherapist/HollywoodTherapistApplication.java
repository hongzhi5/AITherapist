package com.abx.hollywoodtherapist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class HollywoodTherapistApplication {

    public static void main(String[] args) {
        SpringApplication.run(HollywoodTherapistApplication.class, args);
    }
}
