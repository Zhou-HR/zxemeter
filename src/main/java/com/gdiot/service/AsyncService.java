package com.gdiot.service;

/**
 * @author ZhouHR
 */
public interface AsyncService {
    /**
     * @param runnable
     */
    void executeAsync(Runnable runnable);

    /**
     * @param runnable
     */
    void executeMqttAsync(Runnable runnable);
}
