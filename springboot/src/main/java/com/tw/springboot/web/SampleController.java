package com.tw.springboot.web;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/2/15.
 */
@Controller
@RequestMapping("/jdbc")
public class SampleController {
    @RequestMapping("/home1")
    @ResponseBody
    String home1() {
        return "hello worldewtrewrew1";
    }
    @RequestMapping("/home2")
    @ResponseBody
    String home2() {
        return "hello worldewtrewrew2";
    }
}
