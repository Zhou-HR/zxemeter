package com.gdiot.ssm.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SpringContextUtils implements ApplicationContextAware {
	
	@Autowired
	private static ApplicationContext  applicationContext;
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		log.info("setApplicationContext-----------------------");
		this.applicationContext = applicationContext;
	}
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	public static <T> T getBean(Class<T> classz) {
		return getApplicationContext().getBean(classz);
	}
}