package com.gdiot.ssm.service;

public interface AsyncService {
	void executeAsync(Runnable runnable );
	void executeMqttAsync(Runnable runnable );
}
