package com.tw.springmvc.web;

import com.tw.springmvc.entity.User;
import com.tw.springmvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * Created by Administrator on 2017/2/15.
 */
@Controller
@RequestMapping("/account")
public class AccountController {
    final UserService userService;

    @Autowired
    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/home1/{id}/{name}", method = RequestMethod.GET)
    @ResponseBody
    User home1(@PathVariable("id") Long idd, String name) {
        User user = new User();
        user.setId(idd);
        user.setName(name);
        return user;
    }

    @RequestMapping("/home2")
    String home2() {
        return "index";
    }

    @RequestMapping(value = "/home3/{id}/{name}", method = RequestMethod.GET)
    @ResponseBody
    User home3(Long id, String name) {
        return userService.getUserById();
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    String login(Map map) {
        map.put("id",1212);
        map.put("name","是违法");
        return "login";
    }
    @RequestMapping(value = "/login1", method = RequestMethod.GET)
    String login1(@RequestParam("idd") Long id,ModelMap modelMap) {
        modelMap.put("user",userService.getUserById());
        return "login1";
    }
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    User login(@RequestBody User user) {

        return user;
    }
}
