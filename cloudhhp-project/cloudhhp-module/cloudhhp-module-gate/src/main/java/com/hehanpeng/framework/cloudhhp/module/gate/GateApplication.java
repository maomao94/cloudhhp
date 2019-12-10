package com.hehanpeng.framework.cloudhhp.module.gate;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.hehanpeng.framework.cloudhhp.common.config.redis.appevent.EnableAppeventSubscriber;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

// 扫描mybatis哪些包里面的接口
@MapperScan("com.hehanpeng.framework.cloudhhp.module.gate.dao")
@EnableDubbo
@PropertySource(value = "classpath:/consumer-config.properties")
@SpringBootApplication
@EnableFeignClients// (defaultConfiguration = GlobalFeignConfiguration.class)
@EnableAppeventSubscriber//开启redis事件订阅
public class GateApplication {

    public static void main(String[] args) {
        SpringApplication.run(GateApplication.class, args);
    }

    @Bean
    @LoadBalanced
    @SentinelRestTemplate
    public RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate();
        return template;
    }
}
