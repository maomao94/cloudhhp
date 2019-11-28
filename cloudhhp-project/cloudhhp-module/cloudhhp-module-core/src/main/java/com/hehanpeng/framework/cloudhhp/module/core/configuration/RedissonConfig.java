package com.hehanpeng.framework.cloudhhp.module.core.configuration;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class RedissonConfig {

	@Autowired
    Environment env;
	
	@Bean
    public RedissonClient redissonClient() throws IOException {
//    	return Redisson.create(Config.fromYAML(new ClassPathResource("redisson-"+ env.getProperty("spring.profiles.active") +".yml").getInputStream()));
    	return Redisson.create(Config.fromYAML(new ClassPathResource("redisson.yml").getInputStream()));
    }
}
