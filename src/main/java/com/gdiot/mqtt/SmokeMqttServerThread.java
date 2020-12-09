package com.gdiot.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gdiot.SpMeterDataApplication;
import com.gdiot.mqtt.MqttTask;

//@Component
public class SmokeMqttServerThread  extends Thread  {
	private static Logger log = LoggerFactory.getLogger(SpMeterDataApplication.class);
	
	public SmokeMqttServerThread() {
		
	}
	public void run() {
		// TODO Auto-generated method stub
		log.info("SmokeMqttServerThread run--------");
		MqttTask mqttTask = new  MqttTask(MqttConfig.SMOKE_HOST, MqttConfig.SMOKE_clientid,MqttConfig.SMOKE_userName,
				MqttConfig.SMOKE_passWord, MqttConfig.SMOKE_TOPIC_SERVER);
    	MQTTClientFactory.getInstance(mqttTask);
    	mqttTask.run();
    	log.info("SmokeMqttServerThread run--------mqttTask="+ mqttTask);
	}

}
