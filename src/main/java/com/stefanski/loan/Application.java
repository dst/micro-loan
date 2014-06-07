package com.stefanski.loan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Dariusz Stefanski
 */
@EnableAutoConfiguration
@ComponentScan("com.stefanski.loan")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
