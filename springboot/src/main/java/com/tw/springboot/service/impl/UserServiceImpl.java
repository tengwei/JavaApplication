package com.tw.springboot.service.impl;

import com.tw.springboot.entity.User;
import com.tw.springboot.repository.UserRepository;
import com.tw.springboot.service.UserService;
import com.tw.springboot.service.UserServiceA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceA userServiceA;


    @Override
    public User getById(Long id) {
        return userRepository.getOne(id);
    }

    @Transactional
    public void update(Long id) {
        User user = userRepository.findById(id).get();
        user.setName("12345");
        userRepository.save(user);


//        userServiceA.update(2L);
//
        int i = 1 / 0;
    }
}
