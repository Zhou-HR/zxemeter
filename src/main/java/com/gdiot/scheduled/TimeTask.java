package com.gdiot.scheduled;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gdiot.mapper.XBEMMapper;
import com.gdiot.model.EMMeterPo;
import com.gdiot.service.AsyncService;
import com.gdiot.task.DataSenderTask;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TimeTask {

	@Autowired
	private AsyncService asyncService;
	
	@Autowired
	private XBEMMapper mXBEMMapper;
	
	/**
	 * 定时任务每天凌晨1点执行一次，统计所有电表当天用电量
	 */
//	@Scheduled(cron = "0 0 0 * * ?")//每天凌晨执行一次
	@Scheduled(cron = "0 0 1 * * ?")//每天凌晨1点执行一次
	public void scheduledTask(){
		// 1，查出所有电表表号
		// 2，循环，进入线程池。
		// 3,根据表号，当前日期，查前24小时内最早的一条记录和最晚的一条记录，两者相减，得出一天的用电量,保存
		log.info("定时任务每天凌晨1点执行一次，统计所有电表当天用电量");
		getDayValue();
	}
	
	@Scheduled(cron = "0 0 0/1 * * ?") //每个整点执行一次：
	public void testTask() {
		log.info("定时任务每小时执行一次");
	}

	@Scheduled(cron = "30 0/10 * * * ?") //10分钟一次
	public void testTask10() {
		log.info("定时任务每10分钟执行一次");
	}
	
	private void getDayValue() {
		// 1，查出所有电表表号
		// 2，循环，进入线程池。
		// 3,根据表号，当前日期，查前24小时内最早的一条记录和最晚的一条记录，两者相减，得出一天的用电量,保存
		long currentTime = System.currentTimeMillis();
		
		List<EMMeterPo> meterList = mXBEMMapper.selectAllEmeter();
		for(EMMeterPo emMeterPo : meterList) {
			String e_num = emMeterPo.getMeterNo();
			log.info("查询表号 e_num="+e_num);
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("time", currentTime);
			map.put("e_num", e_num);
			map.put("EMMeterPo", emMeterPo);
			DataSenderTask task = new DataSenderTask(map,"selectEMDayValue");
    		asyncService.executeAsync(task);
		}
	}
}
