package com.gd.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;

@Slf4j
public class DingDingUtil {
	
	private static String dingding_url = (String) PropertiesUtil.getInstance().getProperty("interface.properties", "dingding_url");
	
	public static String send(String userCode,String userID,String msg){
		msg=DateUtil.getTodayTime()+" "+msg;
		Map<String,String> param = new HashMap<String,String>();
		//toDo 正式要被注释
		userID="350002";
		log.info("===============20190709yxl  钉钉发消息 =================="+userCode+" "+userID+" "+msg);
		if(StringUtils.isEmpty(userID)){
			return "钉钉userId为空";
		}
			
		param.put("userID", userID);
		param.put("msg", msg);
		
		String result="超时";
		try{
			result = HttpClientUtil.postJson(dingding_url, param, "UTF-8");
			
			return result;
//			Map result_map = JSONObject.parseObject(result);
		}catch(Exception e){
			e.printStackTrace();
			
		}finally{
		}
		
		return result;
		
	}

	public static void main(String[] args) throws MessagingException, Exception {
		// TODO Auto-generated method stub
//		send("","汉");
	}

}
