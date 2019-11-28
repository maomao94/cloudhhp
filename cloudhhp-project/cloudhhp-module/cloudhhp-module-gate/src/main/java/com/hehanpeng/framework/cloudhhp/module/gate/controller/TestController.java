package com.hehanpeng.framework.cloudhhp.module.gate.controller;

import com.hehanpeng.framework.cloudhhp.api.core.dubbo.service.dto.DictCodeDto;
import com.hehanpeng.framework.cloudhhp.api.core.dubbo.service.dto.DictDto;
import com.hehanpeng.framework.cloudhhp.common.mq.MqMessage;
import com.hehanpeng.framework.cloudhhp.module.gate.cache.DictServiceCacheSubscriber;
import com.hehanpeng.framework.cloudhhp.module.gate.mq.producer.impl.RocketMq2CoreProducer;
import com.hehanpeng.framework.cloudhhp.module.gate.sequence.LogIdUtil;
import com.hehanpeng.framework.cloudhhp.module.gate.service.RpcCommonService;
import com.hehanpeng.framework.cloudhhp.module.gate.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    RocketMq2CoreProducer rocketMq2CoreProducer;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    LogIdUtil logIdUtil;
    @Autowired
    private RpcCommonService rpcCommonService;
    @Autowired
    protected DictServiceCacheSubscriber cacheSubscriber;
    @Autowired
    RedissonClient redissonClient;

    @RequestMapping("/asyncSend")
    public MqMessage asyncSend() throws Exception {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("TestController,{} start", methodName);
        String logId = logIdUtil.nextId();
        MqMessage msg = MqMessage.builder()
                .logId(logId)
                .transnetId("1")
                .bizType("test")
                .build();
        rocketMq2CoreProducer.asyncSend(msg);
        String key = "core" + logId;
        //同步阻塞
        String resp = (String) redisTemplate.opsForList().rightPop(key, 10000, TimeUnit.MILLISECONDS);
        if (resp == null) {
            msg.setDesc("core超时");
        } else
            msg = JsonUtil.getObjectFromJson(resp, MqMessage.class);
        log.info("logId={},position=RecvRsp,currentTime={},receiveRespMsg={}", logId, System.currentTimeMillis(), msg);
        return msg;
    }

    @RequestMapping("/asyncFlowSend")
    public MqMessage asyncFlowSend() throws Exception {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("TestController,{} start", methodName);
        String logId = logIdUtil.nextId();
        MqMessage msg = MqMessage.builder()
                .logId(logId)
                .transnetId("1")
                .bizType("testFlow")
                .build();
        rocketMq2CoreProducer.asyncSend(msg);
        String key = "core" + logId;
        //同步阻塞
        String resp = (String) redisTemplate.opsForList().rightPop(key, 10000, TimeUnit.MILLISECONDS);
        if (resp == null) {
            msg.setDesc("core超时");
        } else
            msg = JsonUtil.getObjectFromJson(resp, MqMessage.class);
        log.info("logId={},position=RecvRsp,currentTime={},receiveRespMsg={}", logId, System.currentTimeMillis(), msg);
        return msg;
    }

    @RequestMapping("/helloDubbo")
    public String helloDubbo() throws Exception {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("TestController,{} start", methodName);
        String resp = null;
        try {
            resp = rpcCommonService.helloDubboService.hello("gate");
        } catch (Exception e) {
            log.error("调用core 异常", e);
            return "调用core 异常";
        }
        return resp;
    }

    @RequestMapping("/testDict")
    public String testDict() throws Exception {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("TestController,{} start", methodName);
        String value = cacheSubscriber.getNameByCode("TestDict", "TestDict1");
        log.info("TestController,value:{} end", value);
        return value;
    }

    @RequestMapping("/addDict")
    public String addDict() throws Exception {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("TestController,{} start", methodName);
        DictDto dictDto = new DictDto();
        dictDto.setDictCode("TestDict");
        dictDto.setDictName("测试数据字典缓存");
        dictDto.setDictRemark("测试数据字典缓存");

        List<DictCodeDto> dictCodeDtoList = new LinkedList<>();

        DictCodeDto dto1 = new DictCodeDto();
        dto1.setDictCode(dictDto.getDictCode());
        dto1.setCode("TestDict1");
        dto1.setName("dict" + logIdUtil.nextId());
        dto1.setRemark("dict" + logIdUtil.nextId());

        DictCodeDto dto2 = new DictCodeDto();
        dto2.setDictCode(dictDto.getDictCode());
        dto2.setCode("TestDict2");
        dto2.setName("dict" + logIdUtil.nextId());
        dto2.setRemark("dict" + logIdUtil.nextId());
        dictCodeDtoList.add(dto1);
        dictCodeDtoList.add(dto2);
        dictDto.setDictCodeDtoList(dictCodeDtoList);
        rpcCommonService.dictService.addDict(dictDto);
        log.info("TestController,{} end", methodName);
        return "success";
    }

    @RequestMapping("/lockId")
    public String lockId() throws Exception {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("TestController,{} start", methodName);
        RLock redissonLock = redissonClient.getLock("redissonLock");
        log.info("forward 是否有锁 " + redissonLock.isLocked());
        log.info("forward 当前线程是否有锁 " + redissonLock.isHeldByCurrentThread());
        redissonLock.tryLock(20000L, 20000L, TimeUnit.MILLISECONDS);
        log.info("forward 获取锁 " + "线程是否中断：" + Thread.currentThread().isInterrupted());
        log.info("forward 当前线程是否有锁 " + redissonLock.isHeldByCurrentThread());
        try {
            redissonLock.unlock();
        } catch (Exception e) {
            log.error("forward 解锁报错", e);
        }
        log.info("forward 解锁 " + "线程是否中断：" + Thread.currentThread().isInterrupted());
        log.info("TestController,{} end", methodName);
        return "success";
    }
}
