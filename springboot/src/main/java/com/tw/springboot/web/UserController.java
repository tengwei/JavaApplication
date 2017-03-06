package com.tw.springboot.web;

import com.tw.springboot.entity.User;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 2017/2/15.
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @RequestMapping(method = RequestMethod.GET)
    public String index() {

        return "index";
    }

//    @RequestMapping(name = "/view2", value = "/{id}", method = RequestMethod.GET)
//    public User view2(@PathVariable("id") Long id) {
//        User user = new User();
//        user.setId(id);
//        user.setName("dfdfd");
//        return user;
//    }

    @RequestMapping("/view1")
    @ResponseBody
    public User view1() {
        User user = new User();
        //user.setId(id);
        user.setName("dfdfd");
        return user;
    }
    @RequestMapping("/view2")
    public User view2() {
        User user = new User();
        //user.setId(id);
        user.setName("dfdfd");
        return user;
    }
}
