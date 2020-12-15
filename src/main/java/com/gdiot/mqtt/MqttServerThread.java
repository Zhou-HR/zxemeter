package com.gdiot.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gdiot.SpMeterDataApplication;

/**
 * @author ZhouHR
 */ //@Component
public class MqttServerThread extends Thread {
    private static final Logger log = LoggerFactory.getLogger(SpMeterDataApplication.class);

    public MqttServerThread() {

    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        log.info("MqttServerThread run--------");
        MqttTask mqttTask = new MqttTask(MqttConfig.HOST, MqttConfig.CLIENTID, MqttConfig.USERNAME,
                MqttConfig.PASSWORD, MqttConfig.TOPIC_CLIENT);
        MQTTClientFactory.getInstance(mqttTask);
        mqttTask.run();
        log.info("MqttServerThread run--------mqttTask=" + mqttTask);
    }

}
