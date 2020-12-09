package com.gdiot.service;

public interface AsyncService {
	void executeAsync(Runnable runnable );
	void executeMqttAsync(Runnable runnable );
}
