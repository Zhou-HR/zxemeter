package com.gdiot.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.gdiot.SpMeterDataApplication;
import com.gdiot.service.AsyncService;

/**
 * @author ZhouHR
 */
public class MqttTask implements Runnable, MqttCallback, MqttCallbackExtended {
    private static final Logger LOGGER = LoggerFactory.getLogger(MqttTask.class);
    //	private InitUtil mInitUtil = new InitUtil();
    private MqttClient mqttClient = null;

    private final String mqttUrl;
    private final String mqttClientid;
    private final String mqttUsername;
    private final String mqttPassword;
    private final String mqttTopic;

    private AsyncService asyncService;
//	private RedisUtil redisUtil;

    public MqttTask(String mqttUrl, String mqttClientid, String mqttUsername, String mqttPassword, String mqttTopic) {
        super();
        this.mqttUrl = mqttUrl;
        this.mqttClientid = mqttClientid;
        this.mqttUsername = mqttUsername;
        this.mqttPassword = mqttPassword;
        this.mqttTopic = mqttTopic;
    }

    private boolean connectMqttServer() {
        MemoryPersistence persistence = new MemoryPersistence();
        MqttConnectOptions connOpts = new MqttConnectOptions();
        String[] uris = {mqttUrl, mqttUrl};
        connOpts.setCleanSession(false);
        connOpts.setUserName(mqttUsername);
        connOpts.setPassword(mqttPassword.toCharArray());

        connOpts.setConnectionTimeout(10);
        connOpts.setKeepAliveInterval(20);
        connOpts.setAutomaticReconnect(true);
        //connOpts.setServerURIs(uris);
        //connOpts.setWill(topic, "close".getBytes(), 2, true);
        try {
            mqttClient = new MqttClient(mqttUrl, mqttClientid, persistence);
        } catch (MqttException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (mqttClient != null) {
            try {
                mqttClient.setCallback(this);
                mqttClient.connect(connOpts);
                return true;
            } catch (MqttException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return false;
    }


    private boolean subMqttServer() {
        int[] Qos = {0};
        String[] topics = {mqttTopic};
        try {
            mqttClient.subscribe(topics, Qos);
            return true;
        } catch (MqttException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }


    public boolean pubMessage(String topic, String content) {
        if (mqttClient != null && mqttClient.isConnected()) {
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(0);
            message.setRetained(false);
            try {
                mqttClient.publish(topic, message);
                LOGGER.info(String.format("pubMessage,topic:%s,message:%s", topic, content));
                return true;
            } catch (MqttException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean pubMessage(String topic, JSONObject data) {
        if (mqttClient != null && mqttClient.isConnected()) {
            MqttMessage message = new MqttMessage(data.toString().getBytes());
            message.setQos(0);
            message.setRetained(false);
            try {
                mqttClient.publish(topic, message);
                LOGGER.info(String.format("pubMessage,topic:%s,message:%s", topic, message));
                return true;
            } catch (MqttException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void run() {
        LOGGER.info("MqttTask run");

        while (!connectMqttServer()) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LOGGER.info("MqttTask run over!!!!");
        }

    }


    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        while (!subMqttServer()) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        LOGGER.info("connectComplete");
    }

    @Override
    public void connectionLost(Throwable cause) {
        cause.printStackTrace();
        LOGGER.info("connectionLost");
    }


    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        LOGGER.info(String.format("messageArrived,topic:%s,message:%s", topic, message.toString()));
//		DataSenderTask  task = new DataSenderTask(topic,message.toString());
//		asyncService.executeAsync(task);
        SpMeterDataApplication mSpMeterDataApplication = new SpMeterDataApplication();
        if ((MqttConfig.CLIENTID).equals(this.mqttClientid)) {
            mSpMeterDataApplication.MqttHanderMessage(message.toString());
        } else if ((MqttConfig.SMOKE_clientid).equals(this.mqttClientid)) {
            mSpMeterDataApplication.MqttHanderSmokeMessage(message.toString());
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        try {
            LOGGER.info(String.format("deliveryComplete,message:%s", token.getMessage()));
        } catch (MqttException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public AsyncService getAsyncService() {
        return asyncService;
    }

    public void setAsyncService(AsyncService asyncService) {
        this.asyncService = asyncService;
    }
//	public RedisUtil getRedisUtil() {
//		return redisUtil;
//	}
//
//	public void setRedisUtil(RedisUtil redisUtil) {
//		this.redisUtil = redisUtil;
//	}


}
