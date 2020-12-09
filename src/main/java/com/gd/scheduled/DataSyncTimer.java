package com.gd.scheduled;

import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gd.redis.InitLoadToRedis;

//@Component
@Slf4j
public class DataSyncTimer {
	
//	@Autowired
//	Warning warning;
	
	//定时更新 公司基站信息 用户信息
	@Scheduled(cron = "0 10/30 * * * ?") //30s
//	@Scheduled(cron = "0/5 * * * * ?") //30s
    public void syncSchedule() throws InterruptedException {
		log.info("emeter start sync data!!!");
		
		InitLoadToRedis.sync();
		
		log.info("end emeter start sync data successfully!!!");
	}
	
//	@Scheduled(cron = "0 5 * * * ?") //30s
//    public void syncWarningCheck() throws InterruptedException {
//		log.info("emeter start WarningCheck!!!");
//		
//		warning.check();
//		
//		log.info("end emeter WarningCheck successfully!!!");
//	}
}
