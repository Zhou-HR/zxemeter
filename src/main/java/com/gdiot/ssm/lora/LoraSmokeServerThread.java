package com.gdiot.ssm.lora;

import com.gdiot.ssm.util.LoraConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author ZhouHR
 */
@Slf4j
@Component
@Order(value = 1)
public class LoraSmokeServerThread extends Thread {

    public LoraSmokeServerThread() {

    }

    public void run() {
        // TODO Auto-generated method stub
        log.info("LoraWMServerThread run--------");
        String currentTimeStr = String.valueOf(System.currentTimeMillis());
        String tokenStr = LoraConfig.getMD5(currentTimeStr + LoraConfig.smoke_appEUI + LoraConfig.userSec);
        log.info("======================LoraWMServerThread currentTimeStr=" + currentTimeStr);
        log.info("======================LoraWMServerThread tokenStr=" + tokenStr);
        String url = LoraConfig.baseUrl + "appEUI=" + LoraConfig.smoke_appEUI + "&token=" + tokenStr + "&time_s="
                + currentTimeStr;

        LoraTask mLoraTask = new LoraTask("lora_smoke", url);
        LoraClientFactory.getInstance(mLoraTask);
        mLoraTask.run();
        log.info("LoraWMServerThread run end--------");
    }
}
