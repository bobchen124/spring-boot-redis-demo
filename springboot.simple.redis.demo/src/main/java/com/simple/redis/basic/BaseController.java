package com.simple.redis.basic;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenbo@guworks.cc
 * @title xxx
 * @date 2018年09月25日
 * @since v1.0.0
 */
@RestController
@RequestMapping("/redis")
public class BaseController {

    @Autowired
    RedisBase redisBase;

    @GetMapping("/set")
    public void set() {
        redisBase.setValue("test", "test==");
        //boolean ret = redisBase.setNxPx("test", String.valueOf(System.currentTimeMillis()), 60);
        boolean ret = redisBase.script("test", "test==");
        System.out.println(ret);
    }

    @GetMapping("/get")
    public String get() {
        return redisBase.getValue("test");
    }

    @GetMapping("/send")
    public void send() {
        redisBase.sendMsg("test-channel", "hello redis");
    }

}
