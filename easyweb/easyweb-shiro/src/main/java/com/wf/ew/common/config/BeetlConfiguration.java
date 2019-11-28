package com.wf.ew.common.config;

import com.wf.ew.common.shiro.ShiroExt;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Beetl配置
 * Created by wangfan on 2018-02-22 上午 11:29.
 */
@Configuration
public class BeetlConfiguration {
    @Value("${beetl.templatesPath:templates}")
    private String templatesPath;

    @Bean(name = "beetlConfig")
    public BeetlGroupUtilConfiguration getBeetlGroupUtilConfiguration() {
        BeetlGroupUtilConfiguration beetlGroupUtilConfiguration = new BeetlGroupUtilConfiguration();
        beetlGroupUtilConfiguration.setResourceLoader(new ClasspathResourceLoader(BeetlConfiguration.class.getClassLoader(), templatesPath));
        Map functionPackages = new HashMap();
        functionPackages.put("so", new ShiroExt());
        beetlGroupUtilConfiguration.setFunctionPackages(functionPackages);
        beetlGroupUtilConfiguration.init();
        return beetlGroupUtilConfiguration;
    }

    @Bean(name = "beetlViewResolver")
    public BeetlSpringViewResolver getBeetlSpringViewResolver(@Qualifier("beetlConfig") BeetlGroupUtilConfiguration beetlGroupUtilConfiguration) {
        BeetlSpringViewResolver beetlSpringViewResolver = new BeetlSpringViewResolver();
        beetlSpringViewResolver.setConfig(beetlGroupUtilConfiguration);
        beetlSpringViewResolver.setContentType("text/html;charset=UTF-8");
        beetlSpringViewResolver.setOrder(0);
        return beetlSpringViewResolver;
    }

}

