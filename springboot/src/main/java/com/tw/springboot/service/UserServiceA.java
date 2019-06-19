package com.tw.springboot.service;

import com.tw.springboot.entity.User;
import com.tw.springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceA {
    @Autowired
    private UserRepository userRepository;

    public User getById(Long id) {
        return userRepository.getOne(id);
    }

    @Transactional
    public void update(Long id) {
        User user = userRepository.findById(id).get();
        user.setName("12345");
        userRepository.save(user);
    }
}
