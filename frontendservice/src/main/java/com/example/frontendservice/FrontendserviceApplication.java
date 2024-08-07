package com.example.frontendservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class FrontendserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrontendserviceApplication.class, args);
    }
}
