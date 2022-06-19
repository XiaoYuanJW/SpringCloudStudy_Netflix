package com.test.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.test.dao.BorrowMapper;
import com.test.entity.*;
import com.test.service.BookService;
import com.test.service.BorrowService;
import com.test.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by YuanJW on 2022/5/29.
 */
@Service
public class BorrowServiceImpl implements BorrowService {
    @Resource
    BorrowMapper borrowMapper;
    @Resource
    UserService userService;
    @Resource
    BookService bookService;
    @Autowired
    RestTemplate restTemplate;
    @Value("${service-url.user-service}")
    private String userServiceUrl;
    @Value("${service-url.book-service}")
    private String bookServiceUrl;

    private static final Logger logger = LoggerFactory.getLogger(BorrowServiceImpl.class);

    @Override
    @HystrixCommand(fallbackMethod = "getDefaultUserBorrowDetail")
    public UserBorrowDetail getBorrowByUser(Long uid) {
        List<Borrow> borrows = borrowMapper.getBorrow(uid,null);
        User user = restTemplate.getForObject(userServiceUrl + "/user/" + uid, User.class);
        List<Book> books = borrows.stream().map(borrow -> restTemplate.getForObject(bookServiceUrl + "/book/" + borrow.getBid(), Book.class)).collect(Collectors.toList());
        return new UserBorrowDetail(user, books);
    }

    public UserBorrowDetail getDefaultUserBorrowDetail(@PathVariable Long uid) {
        logger.info("开启Hystrix服务降级");
        return new UserBorrowDetail(null, Collections.emptyList());
    }

    @Override
    public BookBorrowDetail getBorrowByBook(Long bid) {
        List<Borrow> borrows = borrowMapper.getBorrow(null,bid);
        List<User> users = borrows.stream().map(borrow -> restTemplate.getForObject(userServiceUrl + "/user/" + borrow.getUid(), User.class)).collect(Collectors.toList());
        ResponseEntity<Book> entity = restTemplate.getForEntity(bookServiceUrl + "/book/"+bid, Book.class);
        if (entity.getStatusCode().is2xxSuccessful()) {
            return new BookBorrowDetail(users, entity.getBody());
        } else {
            return new BookBorrowDetail(users, null);
        }
    }

    @Override
    public BorrowDetail getBorrow(Long uid, Long bid) {
        List<Borrow> borrows = borrowMapper.getBorrow(uid, bid);
        List<User> users =  borrows.stream().map(borrow -> userService.getUserById(borrow.getUid())).collect(Collectors.toList());
        List<Book> books =  borrows.stream().map(borrow -> bookService.getBookById(borrow.getBid())).collect(Collectors.toList());
        return new BorrowDetail(users, books);
    }
}
