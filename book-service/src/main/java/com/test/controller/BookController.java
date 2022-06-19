package com.test.controller;

import com.test.entity.Book;
import com.test.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by YuanJW on 2022/5/29.
 */
@RestController
public class BookController {
    @Resource
    BookService bookService;

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @RequestMapping("/book/{id}")
    public Book getBookById(@PathVariable("id") Long id, HttpServletRequest request) {
        logger.info("请求头信息：{}", request.getHeader("test"));
        logger.info("根据id获取书籍信息，书籍id为：{}", id);
        return bookService.getBookById(id);
    }
}
