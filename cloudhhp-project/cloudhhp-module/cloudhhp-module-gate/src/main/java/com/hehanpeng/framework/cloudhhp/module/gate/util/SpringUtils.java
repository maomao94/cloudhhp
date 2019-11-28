package com.hehanpeng.framework.cloudhhp.module.gate.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringUtils implements ApplicationContextAware {

	private static ApplicationContext context;

	public static ApplicationContext getContext() {
		return context;
	}

	public final static Object getBeanObj(String beanName) {
		return context.getBean(beanName);
	}

	public final static Object getBeanObj(String beanName, Class<?> requiredType) {
           return context.getBean(beanName,requiredType);
    }

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}
	
	public final static <T> T getBean(Class<T> requiredType) {
		return context.getBean(requiredType);
	}
}
