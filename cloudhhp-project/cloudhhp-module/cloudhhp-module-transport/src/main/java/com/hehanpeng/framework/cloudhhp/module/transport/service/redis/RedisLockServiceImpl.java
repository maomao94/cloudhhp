package com.hehanpeng.framework.cloudhhp.module.transport.service.redis;

import com.hehanpeng.framework.cloudhhp.common.exception.BizException;
import com.hehanpeng.framework.cloudhhp.common.mq.MqMessage;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisLockServiceImpl implements RedisLockService {

    @Autowired
    RedissonClient redissonClient;

    @Override
    public void lockLogId(MqMessage msg) {
        //获取锁
        String lockKey = msg.getLogId();
        RLock lock = redissonClient.getLock(lockKey);

        //尝试上锁
        try {
            boolean flag = lock.tryLock(0L, 20000L, TimeUnit.MILLISECONDS);
            if (!flag) {
                log.error("logId={},coreId={},lock coreId fail", msg.getLogId(), msg.getCoreId());
                throw new BizException("锁失败");
            }
        } catch (InterruptedException e) {
            log.error("logId={},coreId={},lock coreId fail", msg.getLogId(), msg.getCoreId(), e);
            throw new BizException("锁失败");
        }
    }

    @Override
    public void unlockLogId(MqMessage msg) {
        String lockKey = msg.getLogId();
        RLock lock = redissonClient.getLock(lockKey);
        if (lock.isHeldByCurrentThread()) {
            try {
                lock.unlock();
            } catch (Exception e) {
                log.error("logId={},coreId={},lock coreId fail", msg.getLogId(), msg.getCoreId(), e);
            }
        }
    }
}
