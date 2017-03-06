package com.tw.springmvc.service.serviceImpl;

import com.tw.springmvc.entity.User;
import com.tw.springmvc.service.UserService;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/2/17.
 */
@Service
public class UserServiceImpl implements UserService {
    public User getUserById() {
        User user=new User();
        user.setId(new Long(1));
        user.setName("上房屋分割威锋网");
        return user;
    }
}
