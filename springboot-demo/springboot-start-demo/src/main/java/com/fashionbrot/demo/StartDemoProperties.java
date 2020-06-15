package com.fashionbrot.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "com.fashionbrot.demo")
public class StartDemoProperties {

    private String test;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
