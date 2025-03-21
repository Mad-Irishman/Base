package com.ss.topt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.ss")
public class ToptApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToptApplication.class, args);
    }
}
