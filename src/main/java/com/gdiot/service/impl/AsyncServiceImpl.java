package com.gdiot.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.gdiot.service.AsyncService;


/**
 * @author ZhouHR
 */
@Service(value = "AsyncService")
public class AsyncServiceImpl implements AsyncService {

    @Override
    @Async("asyncServiceExecutor")
    public void executeAsync(Runnable runnable) {
        runnable.run();
    }

    @Override
    @Async("asyncMqttServiceExecutor")
    public void executeMqttAsync(Runnable runnable) {
        runnable.run();
    }
}
