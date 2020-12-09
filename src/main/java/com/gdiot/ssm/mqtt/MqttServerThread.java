package com.gdiot.ssm.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gdiot.SpMeterDataApplication;
import com.gdiot.ssm.mqtt.MqttTask;

//@Component
public class MqttServerThread  extends Thread  {
	private static Logger log = LoggerFactory.getLogger(SpMeterDataApplication.class);
	
	public MqttServerThread() {
		
	}
	public void run() {
		// TODO Auto-generated method stub
		log.info("MqttServerThread run--------");
		MqttTask mqttTask = new  MqttTask(MqttConfig.HOST, MqttConfig.clientid,MqttConfig.userName,
				MqttConfig.passWord, MqttConfig.TOPIC_CLIENT);
    	MQTTClientFactory.getInstance(mqttTask);
    	mqttTask.run();
    	log.info("MqttServerThread run--------mqttTask="+ mqttTask);
	}

}
