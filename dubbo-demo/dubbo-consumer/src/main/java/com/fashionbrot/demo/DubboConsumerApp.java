package com.fashionbrot.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;

/**
 * Hello world!
 *
 */
@Slf4j
@SpringBootApplication
@ImportResource("classpath:consumer.xml")
public class DubboConsumerApp extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DubboConsumerApp.class,args);
        System.out.println("Hello World!");
    }
}