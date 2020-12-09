package com.gdiot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gdiot.model.EMCmdsSEQPo;
import com.gdiot.service.AsyncService;
import com.gdiot.service.INBYDEMCmdsService;
import com.gdiot.service.INBYDEMReadService;
import com.gdiot.ssm.redis.RedisUtil;
import com.gdiot.ssm.task.DataSenderTask;
import com.gdiot.ssm.util.ResultObject;
import com.gdiot.ssm.util.SpringContextUtils;

/**
 * @author ZhouHR
 */
@Controller
@RequestMapping("/cmd")
public class CMDAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(CMDAction.class);

    @Autowired()
    private AsyncService asyncService;
    @Autowired()
    private INBYDEMReadService mINBYDEMReadService;
    @Autowired()
    private INBYDEMCmdsService mINBYDEMCmdsService;

    @Autowired
    private RedisUtil redisUtil;


    @RequestMapping("/send_cmd")
    @ResponseBody
    public Map<String, Object> send_cmd(@RequestBody Map<String, String> params) {
        LOGGER.info("send_cmd---------------------------");
        String module_type = null;
        String imei = null;
        String eNum = null;
        String type = null;
        String value = null;
        String operate_type = null;
        if (params != null) {
            if (params.containsKey("module_type")) {
                module_type = params.get("module_type");
            }
            if (params.containsKey("imei")) {
                imei = params.get("imei");
            }
            if (params.containsKey("eNum")) {
                eNum = params.get("eNum");
            }
            if (params.containsKey("type")) {
                type = params.get("type");
            }
            if (params.containsKey("value")) {
                value = params.get("value");
            }
            if (params.containsKey("operate_type")) {
                operate_type = params.get("operate_type");
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        String regex_dev = "^[A-Fa-f0-9]+$";//16 dev_eui
        String regex_imei = "^\\d{15}$";//imei 15
        String regex_yd_dev_id = "^\\d{9}$";//yd_dev_id 9
        if ((imei.matches(regex_dev) && imei.length() == 16)
                || imei.matches(regex_imei)
                || imei.matches(regex_yd_dev_id)) {
            String request_id = imei + "_" + System.currentTimeMillis();
            Map<String, String> map = new HashMap<String, String>();
            map.put("module_type", module_type);
            map.put("imei", imei);
            map.put("eNum", eNum);
            map.put("type", type);
            map.put("value", value);
            map.put("operate_type", operate_type);
            map.put("request_id", request_id);
            DataSenderTask task = new DataSenderTask(map, module_type);
            task.setAsyncService(asyncService);
            task.setRedisUtil(redisUtil);
            asyncService.executeAsync(task);
            LOGGER.info("send_cmd--------------task---done");

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String resultdata = null;
            int count = 0;
            while (resultdata == null && count < 60) {//循环查询
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                resultdata = redisUtil.get(request_id, 0);
//				LOGGER.info("send_cmd--------task---result="+resultdata);
                if (resultdata != null) {
                    //mqtt_2g  下行返回结果result={"errno":0,"error":"succ"}

                    //lora_em   下行返回结果result={"result":{"task_id":"1564041498.863_yMFVEd0NwZpnv7RH8VQOCOUHONKQDLHv",
                    //"request_id":"0000000000009276_1564041498647"},"error":0}

                    //nb  下行返回结果result={"errno":0,"error":"succ"}

                    LOGGER.info("send_cmd--------task---result=" + resultdata);
                    result.put("data", resultdata);
                    return result;
                }
                count++;
            }
        } else {
            result.put("error", "parameter error!");
        }
        return result;
    }

}
