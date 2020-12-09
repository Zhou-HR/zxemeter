package com.gd.basic.common;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 */
public final class BeanFactoryUtil {

    private static BeanFactory beanFactory;

    @Autowired
    public void setBeanFactory(BeanFactory beanFactory) {
        BeanFactoryUtil.beanFactory = beanFactory;
    }

    public static <T> T getBean(Class<T> requiredType) {
        return beanFactory.getBean(requiredType);
    }
}
