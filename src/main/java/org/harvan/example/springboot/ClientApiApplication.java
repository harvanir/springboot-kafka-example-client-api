/*
 * Copyright 2018-2018 the original author or authors.
 */

package org.harvan.example.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * @author Harvan Irsyadi
 * @version 1.0.0
 * @since 1.0.0 (6 May 2018)
 */
@EnableKafka
@SpringBootApplication(exclude = KafkaAutoConfiguration.class)
public class ClientApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(new Class<?>[]{ClientApiApplication.class}, args);
    }
}