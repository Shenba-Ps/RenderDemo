package com.renderdeployment.renderdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RenderDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RenderDemoApplication.class, args);
    }

}
