package com.test.service.impl;

import com.test.entity.Book;
import com.test.service.BookService;
import org.springframework.stereotype.Component;

/**
 * Created by YuanJW on 2022/6/5.
 */
@Component
public class BookFallBackService implements BookService {
    @Override
    public Book getBookById(Long id) {
        return new Book(-1L, "defaultTitle", "defaultDesc");
    }
}
