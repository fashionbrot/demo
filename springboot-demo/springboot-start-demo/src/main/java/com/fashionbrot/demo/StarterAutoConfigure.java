package com.fashionbrot.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(StarterTest.class)
@EnableConfigurationProperties(StartDemoProperties.class)
public class StarterAutoConfigure {

    @Autowired
    private StartDemoProperties startDemoProperties;

    /**
     * @ConditionalOnClass，当classpath下发现该类的情况下进行自动配置。
     * @ConditionalOnMissingBean，当Spring Context中不存在该Bean时。
     * @ConditionalOnProperty(prefix = "example.service",value = "enabled",havingValue = "true")，
     * 当配置文件中example.service.enabled=true时。
     */

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "com.fashionbrot.demo",value = "test",havingValue = "true")
    public void test(){
        System.out.println(startDemoProperties.getTest());
    }

}
