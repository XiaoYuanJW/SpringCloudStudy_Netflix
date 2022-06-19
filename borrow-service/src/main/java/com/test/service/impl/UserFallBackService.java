package com.test.service.impl;

import com.test.entity.User;
import com.test.service.UserService;
import org.springframework.stereotype.Component;

/**
 * Created by YuanJW on 2022/6/5.
 */
@Component
public class UserFallBackService implements UserService {
    @Override
    public User getUserById(Long id) {
        return new User(-1L, "defaultName", "defaultSex");
    }
}
