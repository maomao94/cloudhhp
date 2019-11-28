package com.hehanpeng.framework.cloudhhp.module.forward.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

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
        return context.getBean(beanName, requiredType);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public final static <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }


    /**
     * @param requiredType 服务类名
     * @param methodName   方法名称
     * @param params       参数
     * @return
     * @throws Exception
     */
    public static Object springInvokeMethod(Class requiredType, String methodName, Object[] params) throws Exception {
        Object service = context.getBean(requiredType);
        Class<? extends Object>[] paramClass = null;
        if (params != null) {
            int paramsLength = params.length;
            paramClass = new Class[paramsLength];
            for (int i = 0; i < paramsLength; i++) {
                paramClass[i] = params[i].getClass();
            }
        }
        // 找到方法
        Method method = ReflectionUtils.findMethod(service.getClass(), methodName, paramClass);
        // 执行方法
        return ReflectionUtils.invokeMethod(method, service, params);
    }
}
