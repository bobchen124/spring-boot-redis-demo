package com.simple.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author chenbo@guworks.cc
 * @title xxx
 * @date 2018年09月25日
 * @since v1.0.0
 */
@SpringBootApplication
public class SimpleAppStart {

    public static void main(String[] args) {
        // Close the context so it doesn't stay awake listening for redis
        SpringApplication.run(SimpleAppStart.class, args);
    }

}
