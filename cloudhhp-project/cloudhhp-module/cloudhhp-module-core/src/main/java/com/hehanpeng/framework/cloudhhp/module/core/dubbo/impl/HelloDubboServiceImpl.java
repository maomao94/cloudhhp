package com.hehanpeng.framework.cloudhhp.module.core.dubbo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.hehanpeng.framework.cloudhhp.api.core.dubbo.service.HelloDubboService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;

@Slf4j
@Service(version = "${demo.service.version}")
public class HelloDubboServiceImpl implements HelloDubboService {
    @Value("${demo.service.name}")
    private String serviceName;

    @Override
    public String hello(String name) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("HelloDubboService name={},{} start", name, methodName);
        RpcContext rpcContext = RpcContext.getContext();
        return String.format("Service [name :%s , port : %d] %s(\"%s\") : Hello,%s",
                serviceName,
                rpcContext.getLocalPort(),
                rpcContext.getMethodName(),
                name,
                name);
    }

    @Override
    public void testFwd(HashMap<String, Object> map) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("HelloDubboService name={},{} start", map, methodName);
    }
}
