package com.xiaohe.dynamic.common.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.Annotation;
import java.util.Map;

public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext CONTEXT;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHolder.CONTEXT = applicationContext;
    }

    public static ApplicationContext getInstance() {
        return CONTEXT;
    }

    public static  <T> T getBean(Class<T> clazz) {
        return CONTEXT.getBean(clazz);
    }

    public static Object getBean(String beanName) {
        return CONTEXT.getBean(beanName);
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        return CONTEXT.getBean(beanName, clazz);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> beanType) {
        return CONTEXT.getBeansOfType(beanType);
    }

    /**
     * 寻找bean上加的注解
     * @param beanName
     * @param annotationType
     * @return
     * @param <A>
     */
    public static <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) {
        return CONTEXT.findAnnotationOnBean(beanName, annotationType);
    }
}
