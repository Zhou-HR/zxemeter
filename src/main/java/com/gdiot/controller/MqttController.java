package com.gdiot.controller;

import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gdiot.mqtt.MQTTClientFactory;
import com.gdiot.mqtt.MqttConfig;
import com.gdiot.mqtt.MqttSendCmdsUtil;

/**
 * @author ZhouHR
 */
@Controller
@RequestMapping(value = "/mqtt_2g")
public class MqttController {
    private org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MqttController.class);

    @ResponseBody
    @RequestMapping(value = "/pub_data", method = RequestMethod.POST)
    public String sendData(@RequestBody Map<String, String> params) {
//		String topic = null;
        String content = null;
        if (params != null) {
            if (params.containsKey("content")) {
                content = params.get("content");
            }
        }
        String topic = MqttConfig.TOPIC_SERVER;
        LOGGER.info(String.format("sendData,topic:%s,message:%s", topic, content));
        MqttSendCmdsUtil mMqttSendCmdsUtil = new MqttSendCmdsUtil();
        JSONObject errJson = mMqttSendCmdsUtil.MqttSendCmds(topic, content);
		
		/*JSONObject errJson = new JSONObject();
		
		if(MQTTClientFactory.getInstance(null).getClient().pubMessage(MqttConfig.TOPIC_SERVER, content)) {
			errJson.put("content", content);
			errJson.put("code", "0");
		}else {
			errJson.put("content", content);
			errJson.put("code", "1");
		}*/

        return errJson.toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "/send_cmd", method = RequestMethod.POST)
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
        LOGGER.info(String.format("sendData,topic:%s,message:%s", topic, msg));

        MqttSendCmdsUtil mMqttSendCmdsUtil = new MqttSendCmdsUtil();
        JSONObject errJson = mMqttSendCmdsUtil.MqttSendCmds(topic, msg);
        LOGGER.info("mqtt下行  result=" + errJson);
		
		
		/*JSONObject errJson = new JSONObject();
		if(MQTTClientFactory.getInstance(null).getClient().pubMessage(topic, msg)) {
			errJson.put("errno", 0);
			errJson.put("error", "succ");
		}else {
			errJson.put("errno", 1);
			errJson.put("error", "fail");
		}*/
        return errJson;
    }
}
