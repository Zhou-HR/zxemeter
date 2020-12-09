package com.gdiot.lora;

import org.slf4j.Logger;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gdiot.SpMeterDataApplication;
import com.gdiot.util.LoraConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(value=1)
public class LoraSmokeAlertServerThread  extends Thread {
	
	public LoraSmokeAlertServerThread() {
		
	}
	public void run() {
		// TODO Auto-generated method stub
		log.info("LoraWMServerThread run--------");
		String currentTimeStr = String.valueOf(System.currentTimeMillis());
		String tokenStr = LoraConfig.getMD5(currentTimeStr + LoraConfig.smoke_alarm_appEUI + LoraConfig.smoke_alarm_userSec);
		log.info("======================LoraWMServerThread currentTimeStr="+currentTimeStr);
		log.info("======================LoraWMServerThread tokenStr="+tokenStr);
		String url = LoraConfig.baseUrl+"appEUI=" + LoraConfig.smoke_alarm_appEUI + "&token=" + tokenStr + "&time_s="
				+ currentTimeStr;
		
		LoraTask mLoraTask = new LoraTask("lora_smoke_alert",url);
		LoraClientFactory.getInstance(mLoraTask);
		mLoraTask.run();
		log.info("LoraWMServerThread run end--------");
	}
}