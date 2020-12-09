package com.gdiot.ssm.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;

/**
 * @author ZhouHR
 */
public class MqttSendCmdsUtil {

    private final static Logger logger = LoggerFactory.getLogger(MqttSendCmdsUtil.class);

    public MqttSendCmdsUtil() {

    }

    public JSONObject MqttSendCmds(String topic, String content) {
        JSONObject errJson = new JSONObject();
        logger.info(String.format("sendData,topic:%s,message:%s", topic, content));
        if (MQTTClientFactory.getInstance(null).getClient().pubMessage(topic, content)) {
            errJson.put("errno", 0);
            errJson.put("error", "succ");
        } else {
            errJson.put("errno", 1);
            errJson.put("error", "fail");
        }
        logger.info("MqttSendCmds errJson=" + errJson);
        return errJson;
    }

    public JSONObject MqttSendCmds(String topic, JSONObject msg) {
        JSONObject errJson = new JSONObject();
        logger.info(String.format("sendData,topic:%s,message:%s", topic, msg));
        if (MQTTClientFactory.getInstance(null).getClient().pubMessage(topic, msg)) {
            errJson.put("errno", 0);
            errJson.put("error", "succ");
        } else {
            errJson.put("errno", 1);
            errJson.put("error", "fail");
        }
        logger.info("MqttSendCmds errJson=" + errJson);
        return errJson;
    }

}
