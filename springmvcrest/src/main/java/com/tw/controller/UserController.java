package com.tw.controller;

import com.tw.entity.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2017/2/15.
 */
@RestController
public class UserController {
    @RequestMapping(value="/{id}",method= RequestMethod.GET)
    public User view(@PathVariable("id") Long id){
        User user=new User();
        user.setId(id);
        user.setName("dfdfd");
        return user;
    }
}
