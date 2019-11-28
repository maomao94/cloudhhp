package com.hehanpeng.framework.cloudhhp.module.core.controller;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    RedissonClient redissonClient;

    @RequestMapping("/lockId")
    public String lockId() throws Exception {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("TestController,{} start", methodName);
        RLock redissonLock = redissonClient.getLock("redissonLock");
        redissonLock.tryLock(20000L, 30000L, TimeUnit.MILLISECONDS);
        log.info("core 获取锁 " + "线程是否中断：" + Thread.currentThread().isInterrupted());
        Thread.sleep(20000);
        try {
            redissonLock.unlock();
        } catch (Exception e) {
            log.error("core 解锁报错", e);
        }
        log.info("core 解锁 " + "线程是否中断：" + Thread.currentThread().isInterrupted());
        log.info("TestController,{} end", methodName);
        return "success";
    }
}
