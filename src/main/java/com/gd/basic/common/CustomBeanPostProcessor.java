package com.gd.basic.common;

import com.gd.basic.common.dict.DictFormatter;
import com.gd.basic.security.CustomExceptionTranslationFilter;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;

import java.util.Iterator;
import java.util.List;

/**
 * 对Spring初始化bean之前或在bean初始化后进行其他处理
 *
 * @author ZhouHR
 */
@Component
@Log4j2
public class CustomBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ConversionService) {
            DefaultFormattingConversionService conversionService = (DefaultFormattingConversionService) bean;
            conversionService.addFormatter(new DictFormatter());
            return conversionService;
        } else if (bean instanceof FilterChainProxy) {
            return getSimpleFilterChainProxy((FilterChainProxy) bean);
        } else {
            return bean;
        }
    }

    /**
     * 根据需要对spring创建的filterChainProxy对象进行精简，因为其默认配置的部分过滤器在一般的使用场景里是用不到的
     *
     * @param filterChainProxy 最初的对象
     * @return 精简后的对象
     */
    private Object getSimpleFilterChainProxy(FilterChainProxy filterChainProxy) {
        List<SecurityFilterChain> filterChains = filterChainProxy.getFilterChains();

        for (SecurityFilterChain filterChain : filterChains) {
            List<Filter> filters = filterChain.getFilters();

            if (filters.size() > 0) {
                // 在目前的配置中，我们自定义了一个filterSecurityInterceptor，在这里把默认添加的filterSecurityInterceptor给删除掉
                filters.remove(filters.size() - 1);
            }

            Iterator<Filter> iterator = filters.iterator();
            while (iterator.hasNext()) {
                Filter filter = iterator.next();
                if (filter instanceof WebAsyncManagerIntegrationFilter) {// 删除异步支持，这里暂时用不到
                    iterator.remove();
                    log.info("删除WebAsyncManagerIntegrationFilter");
                } else if (filter instanceof ExceptionTranslationFilter && !(filter instanceof CustomExceptionTranslationFilter)) {
                    iterator.remove();
                    log.info("删除默认的ExceptionTranslationFilter");
                }
            }
            log.info("路径：{}，过滤器链：{}", ((DefaultSecurityFilterChain) filterChain).getRequestMatcher(), filters.toString().replaceAll(",", "\n"));
        }

        return filterChainProxy;
    }
}
