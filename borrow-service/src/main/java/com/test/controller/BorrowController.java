package com.test.controller;

import com.test.entity.*;
import com.test.service.BorrowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by YuanJW on 2022/5/29.
 */
@RestController
public class BorrowController {
    @Resource
    BorrowService borrowService;
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    private static final Logger logger = LoggerFactory.getLogger(BorrowController.class);

    @RequestMapping("/borrow/user/{uid}")
    public UserBorrowDetail getBorrowByUser(@PathVariable("uid") Long uid){
        return borrowService.getBorrowByUser(uid);
    }

    @RequestMapping("/borrow/book/{bid}")
    public BookBorrowDetail getBorrowByBook(@PathVariable("bid") Long bid){
        return borrowService.getBorrowByBook(bid);
    }

    @RequestMapping("/borrow")
    public BorrowDetail getBorrow(@RequestParam(value = "uid", required = true) Long uid,
                                  @RequestParam(value = "bid", required = true) Long bid){
        return borrowService.getBorrow(uid, bid);
    }

    @GetMapping("/borrow/log-instance")
    public void logUserInstance() {
        ServiceInstance serviceInstance = this.loadBalancerClient.choose("user-service");
        BorrowController.logger.info("{}:{}:{}", serviceInstance.getServiceId(), serviceInstance.getHost(), serviceInstance.getPort());
    }
}
