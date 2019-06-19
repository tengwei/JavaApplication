package com.tw.springboot.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceATest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserServiceA userServiceA;

    @Test
    public void update() {
        userService.update(1L);
        userServiceA.update(2L);

    }
}