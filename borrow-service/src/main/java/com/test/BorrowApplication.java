package com.test;

import com.test.config.LoadBalancerConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientConfiguration;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Created by YuanJW on 2022/5/28.
 */
@SpringBootApplication
@MapperScan("com.test.dao")
@EnableDiscoveryClient
@LoadBalancerClients(
        value = {
                @LoadBalancerClient(value = "user-service", configuration = LoadBalancerConfig.class),
                @LoadBalancerClient(value = "book-service", configuration = LoadBalancerConfig.class)
        }, defaultConfiguration = LoadBalancerClientConfiguration.class
)
@EnableHystrix
@EnableFeignClients/*启用Feign的客户端功能*/
public class BorrowApplication {
    public static void main(String[] args) {
        SpringApplication.run(BorrowApplication.class, args);
    }
    
    /*注册RestTemplate对象*/
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
