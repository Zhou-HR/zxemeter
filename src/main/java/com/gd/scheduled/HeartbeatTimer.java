package com.gd.scheduled;

import java.util.Date;

import javax.mail.MessagingException;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gd.mapper.MeterWarningMapper;
import com.gd.mapper.UserMapper;
import com.gd.redis.JedisUtil;
import com.gd.util.EmailUtil;
import com.gd.util.JdbcUtil;
import com.gd.util.PropertiesUtil;



//@Component
@Slf4j
public class HeartbeatTimer {
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private MeterWarningMapper meterWarningMapper;
	//离线电表报警数量
	final static int OFFLINE_DIFF=40;
	//默认离线电表数量
	final static int OFFLINE_BASELINE=80;
	
	static int OFFLINE_REPORT_NUM=0;
	
	//检查离线电表数量，报警
//	@Scheduled(cron = "0 0/10 * * * ?")
	@Scheduled(cron = "0 * * * * ?")
	public void offlineWarningSchedule() throws MessagingException, Exception {
		int num=JdbcUtil.getOfflineNum();
		if(num-OFFLINE_BASELINE>OFFLINE_DIFF){
			
			String key="emeter-"+"offLineReportNUm20191107";
			if(JedisUtil.get(key)==null){
				OFFLINE_REPORT_NUM++;
				sendEmail("离线电表数量报警：离线数量已经达到 "+num+" 台!!!");
				if(OFFLINE_REPORT_NUM>5){//发送6条
					JedisUtil.set(key, OFFLINE_REPORT_NUM,3*3600);
					OFFLINE_REPORT_NUM=0;
				}
			}else{
				OFFLINE_REPORT_NUM=0;
			}
			
		}
		
//		EmailUtil.send("qqemailsucks"+str,"qqemailsucks"+str,"2250948944@qq.com");
		
    }
	
	
	@Scheduled(cron = "0 0 7 * * ?")
//	@Scheduled(cron = "0/5 * * * * ?")
    public void dayReportSchedule() throws MessagingException, Exception {
		String str=JdbcUtil.dayReport();
		sendEmail(str);
		EmailUtil.send("qqemailsucks"+str,"qqemailsucks"+str,"2250948944@qq.com");
		
    }
	
	@Scheduled(cron = "0 0 7,10,14,17 * * ?")
//	@Scheduled(cron = "0/5 * * * * ?")
	public void getEmSchedularLog() throws MessagingException, Exception {
		String str=JdbcUtil.getEmSchedularLog();
		EmailUtil.send(str,str,"yu.xiaolei@gd-iot.com;2250948944@qq.com");
	}
	
	//现在每分钟检查一次
	@Scheduled(cron = "0 * * * * ?")
//	@Scheduled(cron = "0/5 * * * * ?")
    public void meterInstallAlert() {
		int num=JdbcUtil.meterInstallAlert();
		log.info("在3分钟内，后台成功扫描postgre电表安装表："+num+"次！");
		String key="emeter-heartbeart-meterInstallAlert--time";
		Integer value=(Integer) JedisUtil.get(key);
		if(value!=null&&value>=3) return;
		if(value==null) value=0;
		if(num>0)
			JedisUtil.delete(key);
		if(num==0){
			value++;
			JedisUtil.set(key, value,3*3600);
//			String str=
			sendEmail(new Date()+"警报：过去5分钟内，后台扫描postgre电表安装表失败，请检查服务是否启动!!!");
		}
		
    }
	
	//现在每分钟检查一次
	@Scheduled(cron = "0 * * * * ?")
//	@Scheduled(cron = "0/5 * * * * ?")
    public void hourReportCheckSchedule() {
		String heartBeatLastReportTime=PropertiesUtil.getValue("heartBeatLastReportTime");
		if(StringUtils.isEmpty(heartBeatLastReportTime)) heartBeatLastReportTime="5";
		int num=JdbcUtil.getHourReportNum(heartBeatLastReportTime);
		log.info("在"+heartBeatLastReportTime+"分钟内，共上报数据："+num);
		String key="emeter-heartbeart-email-time";
		Integer value=(Integer) JedisUtil.get(key);
		if(value!=null&&value>=3) return;
		if(value==null) value=0;
		if(num>0)
			JedisUtil.delete(key);
		if(num==0){
			value++;
			JedisUtil.set(key, value,3*3600);
			sendEmail(new Date()+"警报：过去"+heartBeatLastReportTime+"分钟内，无数据上报，请检查服务是否启动!!!");
		}
		if(num<0){
			value++;
			JedisUtil.set(key, value,3*3600);
			sendEmail(new Date()+"警报：数据库连接失败！！！");
		}
    }
	
	private void sendEmail(String str){
		try {
			log.info("send Email:"+str);	
			EmailUtil.send(str, str, null);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.error("fail to send Email:"+str);
		}
		log.info("send Email successfully!!!");
	}

}
