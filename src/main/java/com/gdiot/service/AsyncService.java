package com.gdiot.service;

/**
 * @author ZhouHR
 */
public interface AsyncService {
    void executeAsync(Runnable runnable);

    void executeMqttAsync(Runnable runnable);
}
