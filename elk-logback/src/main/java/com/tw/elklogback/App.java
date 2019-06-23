package com.tw.elklogback;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Administrator on 2017/2/15.
 */
@SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
public class App {
    private static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        SpringApplication.run(App.class, args);

//        int i = 0;
//        while (i++ < 10000) {
//            try {
//                logger.debug("debug" + String.valueOf(i));
//                logger.info("info" + String.valueOf(i));
//                logger.warn("warn" + String.valueOf(i));
//                int j = 1 / 0;
//            } catch (Exception ex) {
//                logger.error("info" + String.valueOf(i), ex);
//            }
//        }
    }


}
