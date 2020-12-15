package com.gdiot.mqtt;

import com.gdiot.SpMeterDataApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ZhouHR
 */ //@Component
public class SmokeMqttServerThread extends Thread {
    private static final Logger log = LoggerFactory.getLogger(SpMeterDataApplication.class);

    public SmokeMqttServerThread() {

    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        log.info("SmokeMqttServerThread run--------");
        MqttTask mqttTask = new MqttTask(MqttConfig.SMOKE_HOST, MqttConfig.SMOKE_clientid, MqttConfig.SMOKE_userName,
                MqttConfig.SMOKE_passWord, MqttConfig.SMOKE_TOPIC_SERVER);
        MQTTClientFactory.getInstance(mqttTask);
        mqttTask.run();
        log.info("SmokeMqttServerThread run--------mqttTask=" + mqttTask);
    }

}
