package com.gdiot.controller;

import com.gdiot.service.AsyncService;
import com.gdiot.ssm.redis.RedisUtil;
import com.gdiot.ssm.task.DataSenderTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZhouHR
 */
@RestController
@RequestMapping("/cmd")
public class CMDAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(CMDAction.class);

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private RedisUtil redisUtil;


    @RequestMapping("/send_cmd")
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
        Map<String, Object> result = new HashMap<>();

        //16 dev_eui
        String regex_dev = "^[A-Fa-f0-9]+$";

        //imei 15
        String regex_imei = "^\\d{15}$";

        //yd_dev_id 9
        String regex_yd_dev_id = "^\\d{9}$";

        Boolean isMatch = (imei.matches(regex_dev) && imei.length() == 16) || imei.matches(regex_imei) || imei.matches(regex_yd_dev_id);
        if (isMatch) {
            String request_id = imei + "_" + System.currentTimeMillis();
            Map<String, String> map = new HashMap<>();
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

            //循环查询
            while (resultdata == null && count < 60) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                resultdata = redisUtil.get(request_id, 0);
                if (resultdata != null) {

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