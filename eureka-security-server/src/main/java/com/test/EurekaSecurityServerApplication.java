package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Created by YuanJW on 2022/5/30.
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaSecurityServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaSecurityServerApplication.class, args);
    }
}
