package com.tw.elklogback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    private static Logger logger = LoggerFactory.getLogger(TestController.class);

    @GetMapping
    public String get() {
        int i = 0;
        while (i++ < 10) {
            try {
                //logger.debug("debug" + String.valueOf(i));
                //logger.info("info" + String.valueOf(i));
                //logger.warn("warn" + String.valueOf(i));
                int j = 1 / 0;
            } catch (Exception ex) {
                logger.error("info" + String.valueOf(i), ex);
            }
        }

        return "ertyui";
    }
}
