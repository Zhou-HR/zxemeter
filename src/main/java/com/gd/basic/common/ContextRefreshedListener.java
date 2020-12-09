package com.gd.basic.common;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author ZhouHR
 */
@Log4j2
@Component
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        String displayName = applicationContext.getDisplayName();

        log.info("{} refresh", displayName);
        log.info("{}共管理{}个bean", displayName, applicationContext.getBeanDefinitionCount());

        TreeMap<String, String> map = new TreeMap<>();
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            map.put(beanDefinitionName, applicationContext.getBean(beanDefinitionName).toString());
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append("\r\n===     ").append(entry.getKey()).append(" ==>      ").append(entry.getValue());
        }

        log.info("列表：{}", sb.toString());
    }
}
