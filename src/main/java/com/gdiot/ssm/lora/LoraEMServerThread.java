package com.gdiot.ssm.lora;

import org.slf4j.Logger;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gdiot.SpMeterDataApplication;
import com.gdiot.ssm.util.LoraConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(value=1)
public class LoraEMServerThread  extends Thread {
	
	public LoraEMServerThread() {
		
	}
	public void run() {
		// TODO Auto-generated method stub
		log.info("LoraEMServerThread run--------");
		String currentTimeStr = String.valueOf(System.currentTimeMillis());
		String tokenStr = LoraConfig.getMD5(currentTimeStr + LoraConfig.em_appEUI + LoraConfig.userSec);
		log.info("======================LoraEMServerThread currentTimeStr="+currentTimeStr);
		log.info("======================LoraEMServerThread tokenStr="+tokenStr);
		String url = LoraConfig.baseUrl+"appEUI=" + LoraConfig.em_appEUI + "&token=" + tokenStr + "&time_s="
				+ currentTimeStr;
		
		LoraTask mLoraTask = new LoraTask("lora_em",url);
		LoraClientFactory.getInstance(mLoraTask);
		mLoraTask.run();
		log.info("LoraEMServerThread run end--------");
	}
}