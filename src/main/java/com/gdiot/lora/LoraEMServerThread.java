package com.gdiot.lora;

import com.gdiot.util.LoraConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author ZhouHR
 */
@Slf4j
@Component
@Order(value = 1)
public class LoraEMServerThread extends Thread {

    public LoraEMServerThread() {

    }

    public void run() {
        // TODO Auto-generated method stub
        log.info("LoraEMServerThread run--------");
        String currentTimeStr = String.valueOf(System.currentTimeMillis());
        String tokenStr = LoraConfig.getMD5(currentTimeStr + LoraConfig.em_appEUI + LoraConfig.userSec);
        log.info("======================LoraEMServerThread currentTimeStr=" + currentTimeStr);
        log.info("======================LoraEMServerThread tokenStr=" + tokenStr);
        String url = LoraConfig.baseUrl + "appEUI=" + LoraConfig.em_appEUI + "&token=" + tokenStr + "&time_s="
                + currentTimeStr;

        LoraTask mLoraTask = new LoraTask("lora_em", url);
        LoraClientFactory.getInstance(mLoraTask);
        mLoraTask.run();
        log.info("LoraEMServerThread run end--------");
    }
}
