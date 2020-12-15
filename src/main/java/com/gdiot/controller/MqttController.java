package com.gdiot.controller;

import com.alibaba.fastjson.JSONObject;
import com.gdiot.mqtt.MqttConfig;
import com.gdiot.mqtt.MqttSendCmdsUtil;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author ZhouHR
 */
@RestController
@RequestMapping(value = "/mqtt_2g")
public class MqttController {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(MqttController.class);


    @PostMapping(value = "/pub_data")
    public String sendData(@RequestBody Map<String, String> params) {
//		String topic = null;
        String content = null;
        if (params != null) {
            if (params.containsKey("content")) {
                content = params.get("content");
            }
        }
        String topic = MqttConfig.TOPIC_SERVER;
        logger.info(String.format("sendData,topic:%s,message:%s", topic, content));
        MqttSendCmdsUtil mMqttSendCmdsUtil = new MqttSendCmdsUtil();
        JSONObject errJson = mMqttSendCmdsUtil.MqttSendCmds(topic, content);
        return errJson.toJSONString();
    }

    @PostMapping(value = "/send_cmd")
    public JSONObject PubData(@RequestBody Map<String, String> params) {
        String imei = null;
        String content = null;
        String time = null;
        String type = null;
        if (params != null) {
            if (params.containsKey("imei")) {
                imei = params.get("imei");
            }
            if (params.containsKey("content")) {
                content = params.get("content");
            }
            if (params.containsKey("time")) {
                time = params.get("time");
            }
            if (params.containsKey("type")) {
                type = params.get("type");
            }
        }
        JSONObject msg = new JSONObject();
        msg.put("imei", imei);
        msg.put("time", time);
        msg.put("data", content);
        msg.put("type", type);

        String topic = MqttConfig.TOPIC_SERVER + "/" + imei;
        logger.info(String.format("sendData,topic:%s,message:%s", topic, msg));

        MqttSendCmdsUtil mMqttSendCmdsUtil = new MqttSendCmdsUtil();
        JSONObject errJson = mMqttSendCmdsUtil.MqttSendCmds(topic, msg);
        logger.info("mqtt下行  result=" + errJson);

        return errJson;
    }
}
