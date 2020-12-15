package com.gdiot.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ZhouHR
 */
public class MQTTClientFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(MQTTClientFactory.class);
    private final MqttTask client;

    private static volatile MQTTClientFactory mqttPushClient = null;

    public static MQTTClientFactory getInstance(MqttTask t) {
        LOGGER.info("MQTTClientFactory getInstance run");
        if (null == mqttPushClient) {
            synchronized (MQTTClientFactory.class) {
                if (null == mqttPushClient) {
                    //LOGGER.info("MQTTClientFactory new");
                    mqttPushClient = new MQTTClientFactory(t);
                }
            }
        }
        return mqttPushClient;
    }

    private MQTTClientFactory(MqttTask t) {
        client = t;
        //LOGGER.info("MQTTClientFactory new---client="+client);
    }

    public MqttTask getClient() {
        return client;
    }

}
