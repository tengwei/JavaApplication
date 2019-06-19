package com.tw.springboot.service;

import com.tw.springboot.entity.User;

public interface UserService {
    User getById(Long id);

    void update(Long id);
}
