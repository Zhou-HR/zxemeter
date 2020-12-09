package com.gdiot.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gdiot.model.*;
import com.gdiot.service.*;
import com.gdiot.util.*;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;

import com.alibaba.fastjson.JSON;
import com.gdiot.lora.LoraClientFactory;
import com.gdiot.lora.LoraSendCmds;
import com.gdiot.lora.LoraSendCmdsUtils;
import com.gdiot.mqtt.MqttConfig;
import com.gdiot.mqtt.MqttSendCmdsUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataSenderTask implements Runnable{
//	private static final Logger log =  LoggerFactory.getLogger(DataSenderTask.class);
	private String data;
	private String data_type;
	private Map<String,Object> mapData;
	private AsyncService asyncService;
	
	private INBYDEMStatusService nbYDEMStatusService;
	private INBYDEMEventService mINBYDEMEventService;
	private INBYDEMReadService mINBYDEMReadService;
	private IXBEMDataService mIXBEMDataService;
	private IWMDataService mIWMDataService;
	private ISmokeDataService mISmokeDataService;
	private IXBEMService mIXBEMService;
	private IAKREMDataService mIAKREMDataService;
    private KTDataService ktDataService;
	
	public DataSenderTask(String data,String type) {
		super();
		this.data = data;
		this.data_type = type;
//		LOGGER.info("task: DataSenderTask data receive: data:"+data);
	}
	public DataSenderTask(Map<String,Object> data,String type) {
		super();
		this.mapData = data;
		this.data_type = type;
//		LOGGER.info("task: DataSenderTask data receive: data:"+data);
	}

	public void run() {
		log.info("task: DataSenderTask run-data :"+data);
		log.info("task: DataSenderTask run-data_type :"+data_type);
		switch(data_type) {
		case "nb":
			try {
				NBAnalysis();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
		case "mqtt_2g":
			Mqtt2gAnalysis();
			break;
		case "lora_em":
			LoraAnalysis();
			break;
		case "lora_wm":
			LoraWMAnalysis();
			break;
		case "lora_smoke":
			LoraSmokeAnalysis();
			break;
		case "lora_wm_send_cmd":
//			String request_id = dev_id + "_" +System.currentTimeMillis();
//			LoraSendCmds mLoraSendCmds = new LoraSendCmds();
//    		String resultData = mLoraSendCmds.sendMsgToWM(value, dev_id,request_id);
			break;
		case "udp_wm":
            String ip = (String)mapData.get("IP");
            int port = (Integer)mapData.get("port");
            String dataStr = (String)mapData.get("data");
			UdpWMAnalysis(dataStr,ip,port);
			break;
		case "selectEMDayValue":
			selectEMDayValue(mapData);
			break;
		case "akr_nb_em":
			try {
				AKRNBEMeterAnalysis();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
            case "akr_nb_em_single":
                try {
                    AKRNBEMetersingl();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case "kt_nb_em":
                try {
                    KTNBEMeterAnalysis();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
		case "akr_2g_em"://后续不接入2G电表，运营商停止新增2G物联网用户
			AKR2GEMeterAnalysis();
			break;
		case "lora_smoke_alert"://华南理工烟感集中报警演示
			LoraSmokeAlertDataAnalysis();
			break;
		case "mqtt_smoke_alert":
			MqttSmokeAnalysis();
			break;
			default:
				break;
		}
	}
	
	/**
	 * NB电表数据接收解析
	 * @throws JSONException 
	 */
	private void NBAnalysis() throws JSONException {
		YDUtil.BodyObj obj = YDUtil.resolveBody(data, false);
		org.json.JSONObject data = (org.json.JSONObject) obj.getMsg();
//		LOGGER.info("task: data receive: dev_id:"+data.getLong("dev_id"));
		int type = data.getInt("type");
		if(type == 1) {//value 数据点消息
			// "msg":{"at":1553224758882,"imei":"866971030389733","type":1,"ds_id":"3200_0_5750",
			//"value":"800464000000000000FF2C70063400120103191005010000000020030100003102272241036300005002000080020000810200008201000F16",
			//"dev_id":520156945}
			
	        log.info("task: data receive: imei:"+data.getString("imei"));
	        String ori_value = data.getString("value");
	        String dev_id = data.getLong("dev_id")+"";
	        String ds_id = (data.getString("ds_id").equals("")) ? "": data.getString("ds_id");
	        String imei = data.getString("imei");
	        long time = data.getLong("at");
	        
	        log.info("task: data receive: ori_value:"+ori_value);
	        String regex="^[A-Fa-f0-9]+$";//是16进制数
        	if(ori_value.matches(regex)){
//        		YDEMeterValuePo mYDEMeterValuePo = AnalysisData15(ori_value,mYDEMeterDataPo);//AnalysisData(ori_value);
        		int len = ori_value.length();
        		String orig_code = ori_value.substring(0, len).trim().replaceAll(" ", "");
//        		LOGGER.info("AnalysisData15: orig_code:"+orig_code);
        		byte[] binaryData = Utilty.hexStringToBytes(orig_code);
        		len = binaryData.length;
        		String startbyte = Integer.toHexString(binaryData[0] & 0xFF).toUpperCase() ;
        		String endbyte = Integer.toHexString(binaryData[len - 1] & 0xFF) ;
        		log.info("analysis: startbyte:"+ startbyte );
        		log.info("analysis: endbyte:"+ endbyte);
        		if ("80".equals(startbyte) && "16".equals(endbyte)) {//电表启动发出的是8X，电表响应后台的是CX
//        			System.out.println("AnalysisData15: 80----------------");
        			//表号
        			String e_num = Utilty.convertByteToString(binaryData, 3, 8);
        			log.info("task: 80 analysis: e_num:" + e_num);
    				String regex_eNum ="^\\d{12}$";//e_num 12
    				if(!e_num.matches(regex_eNum)) {//验证表号是否合法
    					log.error("e_num error");
    					return ;
    				}
        			
        			String e_fac = orig_code.substring(16, 18);
        			log.info("task: 80 analysis: e_fac="+ e_fac);
        			
        			String dataType = orig_code.substring(18, 20);
        			log.info("task: 80 analysis: dataType="+ dataType);
        			
        			byte dataLen = binaryData[10];
        			log.info("task: 80 analysis: data Length="+ dataLen);
        			//数据域
        			String valueD = orig_code.substring(22, 22+dataLen*2);
        			//700633081201031910051602000000200375090031024223410315953450021D968002000081020000820100
//        			LOGGER.info("AnalysisData15: valueD:"+ valueD);
        			switch(dataType) {
        			case "A1"://定时上报数据
        			case "FF"://定时上报数据
        				Map<String ,String> emMap = new HashMap<String,String>(); 
    					emMap.put("imei", imei);
    					emMap.put("dev_id", imei);
    					emMap.put("ds_id", ds_id);
    					emMap.put("time", String.valueOf(time));
    					emMap.put("type", String.valueOf(type));
	        			emMap.put("ori_value", ori_value);
    					emMap.put("source", data_type);
    					emMap.put("e_num", e_num);
    					emMap.put("e_fac", e_fac);
    					emMap.put("dataType", dataType);
        				emMap.put("flag_reload", "0");
        				SaveXBEMDataToDB(emMap,valueD);//20190618
				        log.info("task: 80 insert into SQL end!");
				        
        				break;
        			case "B1"://停电事件  返回 C90148000000031900B1005316
        			case "B3"://拉合闸事件  返回 C90147000000031900B3005216
        			case "B5"://过流事件	CA0148000000031900B5004E16
        			case "B7"://过压事件	
        			case "B9"://欠压事件
        			case "BB"://重启记录最近十次  E80D48000000031900BB22BA0281177006313315240419FA030503FFBA0281177006213515240419FA0304FFFFFF16
        			case "BD"://编程记录最近十次	CC0148000000031900BD004416 
        				Map<String ,String> eventMap = new HashMap<String,String>(); 
        				eventMap.put("dev_id", imei);
        				eventMap.put("imei", imei);
        				eventMap.put("e_fac", e_fac);
        				eventMap.put("e_num", e_num);
        				eventMap.put("time", String.valueOf(time));
        				eventMap.put("ori_value", ori_value);
        				eventMap.put("source", data_type);
        				eventMap.put("flag_reload", "0");//上报
        				eventMap.put("data_type", dataType);
        				eventMap.put("deal_flag", "0");
        				SaveEventDataToDB(eventMap,valueD);//zjq 20190719
        				log.info("task: insert nb event into SQL end!");
        				
        				break;
    				default:
    					break;
        			}
        		}else if ( !"80".equals(endbyte) /*&& ifUpDateSeq(startbyte)*/ && "16".equals(endbyte)){//电表响应后台的是CX  "C0".equals(startbyte)
        			//C00100000000000000F801205816
//        			byte[] binaryData = Utilty.hexStringToBytes(data);
        			
        			String e_num = Utilty.convertByteToString(binaryData, 3, 8);
        			log.info("task: C0 analysis: e_num:" + e_num);
        			
        			String e_fac = orig_code.substring(16, 18);
        			log.info("task: C0 analysis: e_fac="+ e_fac);
        			
        			String dataType = orig_code.substring(18, 20);
        			log.info("task: C0 analysis: dataType="+ dataType);
        			
        			byte dataLen = binaryData[10];
        			log.info("task: C0 analysis: data Length="+ dataLen);
        			
        			//数据域
        			String valueD = orig_code.substring(22, 22+dataLen*2);
        			
        			switch(dataType) {
        			//定时上报数据 冻结数据
        			case "A1"://读取丢帧项
        			case "FF"://读取多项数据
        			case "A3"://第几次日冻结数据
        			case "A5"://第几次月冻结数据
        				//C00D39000000031900A332A20201007006071111150419100400000000F8012120030000003102632241030700005002000080021800810200808201AA5516
        				//C00D47000000031900A134A003340200700627421811041910050000000000F8012420030000003102762241030700005002000080020000810200808201551516
        				//加flag
        				
        				Map<String ,String> emMap = new HashMap<String,String>(); 
    					emMap.put("imei", imei);
    					emMap.put("dev_id", imei);
    					emMap.put("ds_id", ds_id);
    					emMap.put("time", String.valueOf(time));
    					emMap.put("type", String.valueOf(type));
	        			emMap.put("ori_value", ori_value);
    					emMap.put("source", data_type);
    					emMap.put("e_num", e_num);
    					emMap.put("e_fac", e_fac);
    					emMap.put("dataType", dataType);
        				emMap.put("flag_reload", "1");
        				SaveXBEMDataToDB(emMap,valueD);//20190618
				        log.info("task: CX insert into SQL end!");
				        
        				break;
        			//事件数据
        			case "B1"://停电事件
            		case "B3"://拉合闸事件 
            		case "B5"://过流事件
        			case "B7"://过压事件
        			case "B9"://欠压事件
            		case "BB"://重启记录最近十次
            		case "BD"://编程记录最近十次
            			
            			Map<String ,String> eventMap = new HashMap<String,String>(); 
        				eventMap.put("dev_id", imei);
        				eventMap.put("imei", imei);
        				eventMap.put("e_fac", e_fac);
        				eventMap.put("e_num", e_num);
        				eventMap.put("time", String.valueOf(time));
        				eventMap.put("ori_value", ori_value);
        				eventMap.put("source", data_type);
        				eventMap.put("flag_reload", "1");//下行
        				eventMap.put("data_type", dataType);
        				eventMap.put("deal_flag", "0");
        				SaveEventDataToDB(eventMap,valueD);//zjq 20190719
        				log.info("task: insert nb event into SQL end!");
        				
        				break;
        			//事件次数
        			case "B0"://停电事件总次数 //C00147000000031900B00203005916
        			case "B2"://拉合闸总次数 //C00147000000031900B20209005116
        			case "B4"://过流总数 //C00147000000031900B40200005816
        			case "B6"://过压事件总数 //C00147000000031900B60200005616
        			case "B8"://欠压事件次数 //C00147000000031900B80200005416
        			case "BA"://模块重启次数 //C00147000000031900BA0200005216
        			case "BC"://编程记录次数
        			case "A0"://整点冻结次数 //C00147000000031900A0032802004116
        			case "A2"://日冻结次数 //C00147000000031900A20203006716
        			case "A4"://月冻结次数 //C00147000000031900A401016816
        			//抄表
        			case "F5"://IMSI ASCII //C00147000000031900F50F3436303034353539333130323938310116
        			case "F6"://IMEI  ASCII //C00147000000031900F60F383636393731303330333839333337F016
        			case "F7"://ICCID ASCII //C00147000000031900F7143839383630343335313031383930303532393830ED16
        			case "F8"://信号强度 //C00147000000031900F80124F116
        			case "F9"://驻网状态
        			case "FA"://模块监控状态字
        			case "10"://有功总电能 //C0014700000003190010050000000000F916
        			case "11"://A相有功电能 //C0014700000003190010050000000000F916
        			case "12"://B相有功电能 
        			case "13"://C相有功电能 
        			case "20"://总有功功率
        			case "21"://A相有功功率
        			case "22"://B相有功功率
        			case "23"://C相有功功率
        			case "31"://A相电压 //C00147000000031900310258226116
        			case "32"://B相电压 
        			case "33"://C相电压 
        			case "41"://A相电流 //C001470000000319004103070000C316
        			case "42"://B相电流 
        			case "43"://C相电流
        			case "50"://总功率因数
        			case "60"://频率
        			case "70"://时间
        			case "71"://注册标识
        			case "72"://上报时间
        			case "73"://认证时长
        			case "74"://心跳间隔
        			case "80"://电表运行状态字1
        			case "81"://电表运行状态字2
        			case "82"://继电器控制字
        			case "83"://电表常数
        			case "90"://厂家标识
        			case "91"://硬件版本号
        			case "92"://软件版本号
        			case "93"://协议版本号
        			case "94"://表号
        			case "95"://通讯号
        			case "96"://用户号 
        				log.info("task: c0 start analysis-----------dataType="+dataType);
        				if(mINBYDEMReadService == null) {
        					mINBYDEMReadService =  SpringContextUtils.getBean(INBYDEMReadService.class);
        				}
        				YDEMNBReadPo mYDEMNBReadPo = new YDEMNBReadPo();
        				Map<String ,String> result_read = EMDataAnalysisUtil.getReadValue(dataType,valueD);
        				mYDEMNBReadPo.setDevId(imei);
        				mYDEMNBReadPo.setImei(imei);
        				mYDEMNBReadPo.setOrigValue(ori_value);
        				mYDEMNBReadPo.setSource(data_type);
        		        mYDEMNBReadPo.setTime(time);
        		        mYDEMNBReadPo.setENum(e_num);
        		        mYDEMNBReadPo.setEFac(e_fac);
        		        mYDEMNBReadPo.setDataSeq(startbyte);
        		        mYDEMNBReadPo.setReadType(result_read.get("read_type") !=null ? result_read.get("read_type"):"");
        		        mYDEMNBReadPo.setReadValue(result_read.get("read_value") !=null ? result_read.get("read_value"):"");
        		        mINBYDEMReadService.addOne(mYDEMNBReadPo);
        		        log.info("task: c0 insert into SQL end!");
        				break;
        			}
        		}else if ("90".equals(startbyte) && "16".equals(endbyte)) {
        			
        			String e_num = Utilty.convertByteToString(binaryData, 3, 8);
        			log.info("task: C0 analysis: e_num:" + e_num);
        			
        			String e_fac = orig_code.substring(16, 18);
        			log.info("task: C0 analysis: e_fac="+ e_fac);
        			
        			String dataType = orig_code.substring(18, 20);
        			log.info("task: C0 analysis: dataType="+ dataType);
        			
        			byte dataLen = binaryData[10];
        			log.info("task: C0 analysis: data Length="+ dataLen);
        			
        			//数据域
        			String valueD = orig_code.substring(22, 22+dataLen*2);
        			
        			switch(dataType) {
        			//定时上报数据 冻结数据
        			case "A1"://读取丢帧项
	        			Map<String ,String> emMap9 = new HashMap<String,String>(); 
	        			emMap9.put("imei", imei);
	        			emMap9.put("dev_id", imei);
	        			emMap9.put("ds_id", ds_id);
	        			emMap9.put("time", String.valueOf(time));
	        			emMap9.put("type", String.valueOf(type));
	        			emMap9.put("ori_value", ori_value);
	        			emMap9.put("source", data_type);
	        			emMap9.put("e_num", e_num);
	        			emMap9.put("e_fac", e_fac);
	        			emMap9.put("flag_reload", "0");
	        			SaveXBEMDataToDB(emMap9,valueD);//20190618
				        log.info("task: 90 insert into SQL end!");
        				break;
        			default:
        				break;
        			}
        		}
        	}
		}else if(type == 2 ) {//login 设备上下线消息
			//"msg":{"at":1553224749838,"login_type":10,"imei":"866971030389733","type":2,"dev_id":520156945,"status":1}
			if(nbYDEMStatusService == null) {
				nbYDEMStatusService =  SpringContextUtils.getBean(INBYDEMStatusService.class);
			}
			String dev_id = data.getLong("dev_id")+"";
			String imei = data.getString("imei");
			int status = data.getInt("status");
			int login_type = data.getInt("login_type");
			long time = data.getLong("at");
			log.info("task: : nbYDEMStatusService : dev_id="+dev_id);
			log.info("task: : nbYDEMStatusService : imei="+imei);
			log.info("task: : nbYDEMStatusService : 设备上下线 status="+status);
			log.info("task: : nbYDEMStatusService : login_type="+login_type);
			log.info("task: : nbYDEMStatusService : time="+time);
			
			YDEMeterStatusPo mYDEMeterStatusPo = new YDEMeterStatusPo();
			mYDEMeterStatusPo.setDevId(dev_id);
			mYDEMeterStatusPo.setImei(imei);
			mYDEMeterStatusPo.setType(type);
			mYDEMeterStatusPo.setLoginType(login_type);
			mYDEMeterStatusPo.setStatus(status);
			mYDEMeterStatusPo.setTime(time);
			log.info("task: : nbYDEMStatusService: in insertStatus");
	        List<YDEMeterStatusPo> list = nbYDEMStatusService.selectDevid(dev_id,imei);
	        if(list.size()>=1) {//存在，更新
	        	nbYDEMStatusService.updateEMStatus(mYDEMeterStatusPo);
	        }else {//数据库中不存在，插入
	        	nbYDEMStatusService.insertStatus(mYDEMeterStatusPo);
	        }
	        log.info("task: status insert into SQL end!");
		}
	}
	
	/**
	 * lora电表数据接收解析
	 */
	public void LoraAnalysis() {
//		LOGGER.info("============= Lora Date start analysis=============");
		try {
			com.alibaba.fastjson.JSONObject jo = JSON.parseObject(data);
			if (jo.containsKey("devEUI") && jo.containsKey("data")) {
//				LOGGER.info("lora data =====================" );
				String dev_eui = jo.getString("devEUI");
				String ori_value = jo.getString("data");
				String time_s = DateUtil.shortenTimeStr(jo.getString("time_s"));//2019-04-09 08:24:09.120570725
				long time = DateUtil.date2TimeStampLong(time_s, "yyyy-MM-dd HH:mm:ss");
				
				log.info("lora data LoraAnalysis:dev_eui=" + dev_eui );
				log.info("lora data LoraAnalysis:data=" + ori_value );
				log.info("lora data LoraAnalysis:time_s=" + time_s );
				int len = ori_value.length();
		    	String orig_data = ori_value.substring(2, len).trim().replaceAll(" ", "");
		    	
		        String regex="^[A-Fa-f0-9]+$";//是16进制数
		    	if(orig_data.matches(regex)){
		    		String dataFlag = orig_data.substring(0, 8);
		    		log.info("task: lora data receive: dataFlag:"+dataFlag);
		    		String orig_code = orig_data;
		    		if ("FEFEFEFE".equals(dataFlag)) {//lora 数据
		    			orig_code = orig_data.substring(8, orig_data.length());
//			    		System.out.println("task: data receive: orig_code:"+orig_code);
		    			byte[] binaryData = Utilty.hexStringToBytes(orig_code);
			    		len = binaryData.length;
			    		String startbyte = Integer.toHexString(binaryData[0] & 0xFF).toUpperCase() ;
			    		String endbyte = Integer.toHexString(binaryData[len - 1] & 0xFF) ;
			    		log.info("analysis: startbyte:"+ startbyte );
			    		log.info("analysis: endbyte:"+ endbyte);
			    		if ("80".equals(startbyte) && "16".equals(endbyte)) {//电表启动发出的是8X，电表响应后台的是CX
			    			//System.out.println("AnalysisData15: 80----------------");
			    			//表号
			    			String e_num = Utilty.convertByteToString(binaryData, 3, 8);
			    			log.info("task: lora analysis: e_num:" + e_num);
			    			
			    			String e_fac = orig_code.substring(16, 18);
			    			log.info("task: lora analysis: e_fac="+ e_fac);
			    			
			    			String dataType = orig_code.substring(18, 20);
			    			log.info("task: lora analysis: dataType="+ dataType);
			    			
			    			byte dataLen = binaryData[10];
			    			log.info("task: lora analysis: data Length="+ dataLen);
			    			                                 
			    			if("70".equals(dataType) && (dataLen == 0)) {
		    					String msgStr = LoraSendCmdsUtils.getTimeContent(dev_eui,e_num,e_fac);
		    					log.info(String.format("sendData data:%s", msgStr));
		    					LoraClientFactory.getInstance(null).getClient().sendMsg(msgStr);
			    			}else {
			    				//数据域
			    				String valueD = orig_code.substring(22, 22+dataLen*2);
			    				switch(dataType) {
			        			case "A1"://定时上报数据
			        			case "FF"://定时上报数据
			        			case "A3"://第几次日冻结数据
			        			case "A5"://第几次月冻结数据
			        				Map<String ,String> emMap9 = new HashMap<String,String>(); 
				        			emMap9.put("dev_id", dev_eui);
				        			emMap9.put("time", String.valueOf(time));
				        			emMap9.put("ori_value", orig_data);
				        			emMap9.put("source", data_type);
				        			emMap9.put("e_num", e_num);
				        			emMap9.put("e_fac", e_fac);
				        			emMap9.put("flag_reload", "0");
				        			emMap9.put("dataType", dataType);
				        			SaveXBEMDataToDB(emMap9,valueD);//zjq 20190705
			        				log.info("task: lora fefefefe 80 insert into SQL end!");
			        				
			        				break;
			        			case "B1"://停电事件  返回 C90148000000031900B1005316
			        			case "B3"://拉合闸事件  返回 C90147000000031900B3005216
			        			case "B5"://过流事件	CA0148000000031900B5004E16
			        			case "B7"://过压事件	
			        			case "B9"://欠压事件
			        			case "BB"://重启记录最近十次  E80D48000000031900BB22BA0281177006313315240419FA030503FFBA0281177006213515240419FA0304FFFFFF16
			        			case "BD"://编程记录最近十次	CC0148000000031900BD004416 
			        				
			        				Map<String ,String> eventMap = new HashMap<String,String>(); 
			        				eventMap.put("dev_id", dev_eui);
			        				eventMap.put("imei", dev_eui);
			        				eventMap.put("e_fac", e_fac);
			        				eventMap.put("e_num", e_num);
			        				eventMap.put("time", String.valueOf(DateUtil.date2TimeStampLong_l(time_s, "yyyy-MM-dd HH:mm:ss")));
			        				eventMap.put("ori_value", orig_data);
			        				eventMap.put("source", data_type);
			        				eventMap.put("flag_reload", "0");//上报
			        				eventMap.put("data_type", dataType);
			        				eventMap.put("deal_flag", "0");
			        				SaveEventDataToDB(eventMap,valueD);//zjq 20190705
			        				log.info("task: insert lora event into SQL end!");
			        				
			        				break;
			    				default:
			    					break;
			    				}
			    			}
			    		} else if( !"80".equals(endbyte) && "16".equals(endbyte)) {//下行返回
			    			String e_num = Utilty.convertByteToString(binaryData, 3, 8);
		        			log.info("task: C0 analysis: e_num:" + e_num);
		        			
		        			String e_fac = orig_code.substring(16, 18);
		        			log.info("task: C0 analysis: e_fac="+ e_fac);
		        			
		        			String dataType = orig_code.substring(18, 20);
		        			log.info("task: C0 analysis: dataType="+ dataType);
		        			
		        			byte dataLen = binaryData[10];
		        			log.info("task: C0 analysis: data Length="+ dataLen);
		        			
		        			//数据域
		        			String valueD = orig_code.substring(22, 22+dataLen*2);
		        			
		        			switch(dataType) {
		        			//定时上报数据 冻结数据
		        			case "A1"://读取丢帧项
		        			case "FF"://读取多项数据
		        			case "A3"://第几次日冻结数据
		        			case "A5"://第几次月冻结数据
		        				//C00D39000000031900A332A20201007006071111150419100400000000F8012120030000003102632241030700005002000080021800810200808201AA5516
		        				//C00D47000000031900A134A003340200700627421811041910050000000000F8012420030000003102762241030700005002000080020000810200808201551516
		        				//加flag
		        				
		        				Map<String ,String> emMap9 = new HashMap<String,String>(); 
			        			emMap9.put("dev_id", dev_eui);
			        			emMap9.put("time", String.valueOf(time));
			        			emMap9.put("ori_value", orig_data);
			        			emMap9.put("source", data_type);
			        			emMap9.put("e_num", e_num);
			        			emMap9.put("e_fac", e_fac);
			        			emMap9.put("flag_reload", "1");
			        			emMap9.put("dataType", dataType);
			        			SaveXBEMDataToDB(emMap9,valueD);//zjq 20190705
		        				log.info("task: lora fefefefe 80 insert into SQL end!");
		        				
		        				break;
		        			//事件数据
		        			case "B1"://停电事件
		            		case "B3"://拉合闸事件 
		            		case "B5"://过流事件
		        			case "B7"://过压事件
		        			case "B9"://欠压事件
		            		case "BB"://重启记录最近十次
		            		case "BD"://编程记录最近十次
		            			
		            			Map<String ,String> eventMap = new HashMap<String,String>(); 
		        				eventMap.put("dev_id", dev_eui);
		        				eventMap.put("imei", dev_eui);
		        				eventMap.put("e_fac", e_fac);
		        				eventMap.put("e_num", e_num);
		        				eventMap.put("time", String.valueOf(DateUtil.date2TimeStampLong_l(time_s, "yyyy-MM-dd HH:mm:ss")));
		        				eventMap.put("ori_value", orig_data);
		        				eventMap.put("source", data_type);
		        				eventMap.put("flag_reload", "1");//上报
		        				eventMap.put("data_type", dataType);
			        			eventMap.put("deal_flag", "0");
		        				SaveEventDataToDB(eventMap,valueD);//zjq 20190705
		        				log.info("task: insert lora event into SQL end!");
		        				
		        				break;
		        			//事件次数
		        			case "B0"://停电事件总次数 //C00147000000031900B00203005916
		        			case "B2"://拉合闸总次数 //C00147000000031900B20209005116
		        			case "B4"://过流总数 //C00147000000031900B40200005816
		        			case "B6"://过压事件总数 //C00147000000031900B60200005616
		        			case "B8"://欠压事件次数 //C00147000000031900B80200005416
		        			case "BA"://模块重启次数 //C00147000000031900BA0200005216
		        			case "BC"://编程记录次数
		        			case "A0"://整点冻结次数 //C00147000000031900A0032802004116
		        			case "A2"://日冻结次数 //C00147000000031900A20203006716
		        			case "A4"://月冻结次数 //C00147000000031900A401016816
		        			//抄表
		        			case "F5"://IMSI ASCII //C00147000000031900F50F3436303034353539333130323938310116
		        			case "F6"://IMEI  ASCII //C00147000000031900F60F383636393731303330333839333337F016
		        			case "F7"://ICCID ASCII //C00147000000031900F7143839383630343335313031383930303532393830ED16
		        			case "F8"://信号强度 //C00147000000031900F80124F116
		        			case "F9"://驻网状态
		        			case "FA"://模块监控状态字
		        			case "10"://有功总电能 //C0014700000003190010050000000000F916
		        			case "11"://A相有功电能 //C0014700000003190010050000000000F916
		        			case "12"://B相有功电能 
		        			case "13"://C相有功电能 
		        			case "20"://总有功功率
		        			case "21"://A相有功功率
		        			case "22"://B相有功功率
		        			case "23"://C相有功功率
		        			case "31"://A相电压 //C00147000000031900310258226116
		        			case "32"://B相电压 
		        			case "33"://C相电压 
		        			case "41"://A相电流 //C001470000000319004103070000C316
		        			case "42"://B相电流 
		        			case "43"://C相电流
		        			case "50"://总功率因数
		        			case "60"://频率
		        			case "70"://时间
		        			case "71"://注册标识
		        			case "72"://上报时间
		        			case "73"://认证时长
		        			case "74"://心跳间隔
		        			case "80"://电表运行状态字1
		        			case "81"://电表运行状态字2
		        			case "82"://继电器控制字
		        			case "83"://电表常数
		        			case "90"://厂家标识
		        			case "91"://硬件版本号
		        			case "92"://软件版本号
		        			case "93"://协议版本号
		        			case "94"://表号
		        			case "95"://通讯号
		        			case "96"://用户号 
		        				log.info("task: c0 start analysis-----------dataType="+dataType);
		        				if(mINBYDEMReadService == null) {
		        					mINBYDEMReadService =  SpringContextUtils.getBean(INBYDEMReadService.class);
		        				}
		        				YDEMNBReadPo mYDEMNBReadPo = new YDEMNBReadPo();
		        				Map<String ,String> result_read = EMDataAnalysisUtil.getReadValue(dataType,valueD);
		        				mYDEMNBReadPo.setDevId(dev_eui);
		        				mYDEMNBReadPo.setImei(dev_eui);
		        				mYDEMNBReadPo.setOrigValue(orig_code);
		        				mYDEMNBReadPo.setSource(data_type);
		        		        mYDEMNBReadPo.setTime(DateUtil.date2TimeStampLong_l(time_s, "yyyy-MM-dd HH:mm:ss"));
		        		        mYDEMNBReadPo.setENum(e_num);
		        		        mYDEMNBReadPo.setEFac(e_fac);
		        		        mYDEMNBReadPo.setDataSeq(startbyte);
		        		        mYDEMNBReadPo.setReadType(result_read.get("read_type") !=null ? result_read.get("read_type"):"");
		        		        mYDEMNBReadPo.setReadValue(result_read.get("read_value") !=null ? result_read.get("read_value"):"");
		        		        mINBYDEMReadService.addOne(mYDEMNBReadPo);
		        		        log.info("task: c0 insert into SQL end!");
		        				break;
		        			}
		        		}
			    		}
		    		}
		    	}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	/**
	 * lora水表数据接收解析
	 */
	public void LoraWMAnalysis() {
		//收 读计量数据2：
		//FE FE FE 68 10 01 13 12 19 00 00 00 
		//A1 1D 90 1F 01 2C 00 00 00 00 2C 00 00 00 00 35 00 00 00 00 41 20 14 16 12 19 20 40 21 00 02 EB 16
		try {
			com.alibaba.fastjson.JSONObject jo = JSON.parseObject(data);
			if (jo.containsKey("devEUI") && jo.containsKey("data")) {
//				LOGGER.info("lora data =====================" );
				String dev_eui = jo.getString("devEUI");
				String ori_value = jo.getString("data");
				String time_s = DateUtil.shortenTimeStr(jo.getString("time_s"));//2019-04-09 08:24:09.120570725
				long time = DateUtil.date2TimeStampLong(time_s, "yyyy-MM-dd HH:mm:ss");
				
				log.info("lora data LoraAnalysis:dev_eui=" + dev_eui );
				log.info("lora data LoraAnalysis:data=" + ori_value );
				log.info("lora data LoraAnalysis:time_s=" + time_s );
				int len = ori_value.length();
		    	String orig_data = ori_value.substring(2, len).trim().replaceAll(" ", "");//去掉\x
		    	
		        String regex="^[A-Fa-f0-9]+$";//是16进制数
		    	if(orig_data.matches(regex)){
		    		String dataFlag = orig_data.substring(0, 6);
		    		log.info("task: lora data receive: dataFlag:"+dataFlag);
		    		String orig_code = orig_data;
		    		if ("FEFEFE".equals(dataFlag)) {//lora 数据
		    			orig_code = orig_data.substring(6, orig_data.length());
//			    		System.out.println("task: data receive: orig_code:"+orig_code);
		    			byte[] binaryData = Utilty.hexStringToBytes(orig_code);
			    		len = binaryData.length;
			    		String startbyte = Integer.toHexString(binaryData[0] & 0xFF).toUpperCase() ;
			    		String endbyte = Integer.toHexString(binaryData[len - 1] & 0xFF) ;
			    		log.info("analysis: startbyte:"+ startbyte );
			    		log.info("analysis: endbyte:"+ endbyte);
			    		if ("68".equals(startbyte) && "16".equals(endbyte)) {//水表上报发出的是68
			    			//System.out.println("AnalysisData15: 80----------------");
			    			//表号
			    			String w_num = Utilty.convertByteToString(binaryData, 3, 9);
			    			System.out.println("task: lora analysis: e_num:" + w_num);
			    			//FE FE FE 68 10 01 13 12 19 00 00 00 
			    			//A1 1D 90 1F 01 2C 00 00 00 00 2C 00 00 00 00 35 00 00 00 00 41 20 14 16 12 19 20 40 21 00 02 EB 16
			    			String control_code = orig_code.substring(18, 20);
			    			System.out.println("task: lora analysis: control_code="+ control_code);
			    			//\xFEFEFE681001131219000000A11D901F012C000000002C00000000350000000056201817121920402200030716
			    			int dataLen = Integer.parseInt(orig_code.substring(20, 22), 16);
			    			System.out.println("task: lora analysis: data Length="+ dataLen);
			    			
		    				//数据域
		    				String valueD = orig_code.substring(22, 22+dataLen*2);
		    				System.out.println("task: lora fefefe 68 valueD="+valueD);
		    				switch(control_code) {
		        			case "A1"://读计量数据2
		        				if(mIWMDataService == null) {
		        					mIWMDataService =  SpringContextUtils.getBean(IWMDataService.class);
		        				}
		        				WMDataPo wmDataPo = new WMDataPo();
		        				Map<String ,String> result_read = EMDataAnalysisUtil.getWMDataValue(valueD);
		        				wmDataPo.setWmNum(w_num);
		        				wmDataPo.setTime(time);
		        				wmDataPo.setOrigValue(orig_data);
		        				wmDataPo.setDevId(dev_eui);
		        				wmDataPo.setSource(data_type);
		        				wmDataPo.setDataFlag1(result_read.get("data_flag1"));
		        				wmDataPo.setDataFlag2(result_read.get("data_flag2"));
		        				wmDataPo.setDataSeq(result_read.get("data_seq"));
		        				wmDataPo.setWmFlow(result_read.get("wm_flow"));
		        				wmDataPo.setWmFlowRate(result_read.get("wm_flow_rate"));
		        				wmDataPo.setWmReverseFlow(result_read.get("wm_reverse_flow"));
		        				wmDataPo.setWmTime(result_read.get("wm_time"));
		        				wmDataPo.setWmStatu1(result_read.get("wm_statu1"));
		        				wmDataPo.setWmStatu2(result_read.get("wm_statu2"));
		        				mIWMDataService.addOne(wmDataPo);
		        				System.out.println("task: lora fefefe 68 insert into SQL end!");
		        				
		        				break;
		        			case "81"://读计量数据1
		        				break;
		        			case "83"://读表头地址
		        				break;
		        			case "84"://强制开关阀,自由开关阀
		        				break;
		    				default:
		    					break;
		    				}
			    		}
		    		}
		    	}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	
	}
	/**
	 * 2G电表数据接收解析 mqtt方式接收
	 */
	private void Mqtt2gAnalysis() {
		log.info("============= GPRS Date start analysis=============");
		com.alibaba.fastjson.JSONObject jo = JSON.parseObject(data);
		if (jo.containsKey("imei") && jo.containsKey("data") && jo.containsKey("type")) {
			String imei = jo.getString("imei");
			String ori_value = jo.getString("data");
			String time_s = jo.getString("time");
			long time = DateUtil.date2TimeStampLong(time_s, "yyyy-MM-dd HH:mm:ss");
			String type = jo.getString("type");//emeterGprsData

			log.info("GPRS data LoraAnalysis:imei=" + imei );
			log.info("GPRS data LoraAnalysis:data=" + ori_value );
			log.info("GPRS data LoraAnalysis:time_s=" + time_s );
			
			log.info("task: data receive: ori_value:"+ori_value);
	        String regex="^[A-Fa-f0-9]+$";//是16进制数
        	if(ori_value.matches(regex)){
        		int len = ori_value.length();
        		String orig_code = ori_value.substring(0, len).trim().replaceAll(" ", "");
//	        	LOGGER.info("AnalysisData15: orig_code:"+orig_code);
        		byte[] binaryData = Utilty.hexStringToBytes(orig_code);
        		len = binaryData.length;
        		String startbyte = Integer.toHexString(binaryData[0] & 0xFF).toUpperCase() ;
        		String endbyte = Integer.toHexString(binaryData[len - 1] & 0xFF) ;
        		log.info("analysis: startbyte:"+ startbyte );
        		log.info("analysis: endbyte:"+ endbyte);
        		if ("80".equals(startbyte) && "16".equals(endbyte)) {//电表启动发出的是8X，电表响应后台的是CX
//	        		System.out.println("AnalysisData15: 80----------------");
        			String e_num = Utilty.convertByteToString(binaryData, 3, 8);//表号
        			log.info("task: 80 analysis: e_num:" + e_num);
        			String e_fac = orig_code.substring(16, 18);
        			log.info("task: 80 analysis: e_fac="+ e_fac);
        			String dataType = orig_code.substring(18, 20);
        			log.info("task: 80 analysis: dataType="+ dataType);
        			byte dataLen = binaryData[10];
        			log.info("task: 80 analysis: data Length="+ dataLen);
        			//数据域
        			String valueD = orig_code.substring(22, 22+dataLen*2);
//	        		LOGGER.info("AnalysisData15: valueD:"+ valueD);
        			switch(dataType) {
        			case "70"://验证订阅
        				JSONObject msg = new JSONObject();
        				msg.put("imei", imei);
        				msg.put("time", DateUtil.timeStamp2Date(String.valueOf(System.currentTimeMillis()/1000), null));
        				msg.put("data", "sub topic ok");
        				msg.put("type", "emeterGprsData");
        				
        				String topic = MqttConfig.TOPIC_SERVER+"/"+imei;
        				MqttSendCmdsUtil mMqttSendCmdsUtil = new MqttSendCmdsUtil();
        				JSONObject result = mMqttSendCmdsUtil.MqttSendCmds(topic, msg);
        				log.info("70 mqtt 验证订阅 result="+result);
//        				LOGGER.info(String.format("sendData,topic:%s,message:%s", topic,msg));
//        				if(MQTTClientFactory.getInstance(null).getClient().pubMessage(topic, msg)) {
//        					LOGGER.info("pub msg success");
//        				}else {
//        					LOGGER.info("pub msg fail");
//        				}
        				break;
        			case "A1"://定时上报数据
        			case "FF"://定时上报数据
        				Map<String ,String> emMap = new HashMap<String,String>(); 
    					emMap.put("dev_id", imei);
    					emMap.put("time", String.valueOf(time));
	        			emMap.put("ori_value", ori_value);
    					emMap.put("source", data_type);
    					emMap.put("e_num", e_num);
    					emMap.put("e_fac", e_fac);
    					emMap.put("dataType", dataType);
        				emMap.put("flag_reload", "0");
        				SaveXBEMDataToDB(emMap,valueD);//20190618
				        log.info("task: 80 gprs insert into SQL end!");
				        
        				break;
        			case "B1"://停电事件  返回 C90148000000031900B1005316
        			case "B3"://拉合闸事件  返回 C90147000000031900B3005216
        			case "B5"://过流事件	CA0148000000031900B5004E16
        			case "B7"://过压事件	
        			case "B9"://欠压事件
        			case "BB"://重启记录最近十次  E80D48000000031900BB22BA0281177006313315240419FA030503FFBA0281177006213515240419FA0304FFFFFF16
        			case "BD"://编程记录最近十次	CC0148000000031900BD004416 
        				Map<String ,String> eventMap = new HashMap<String,String>();
        				eventMap.put("dev_id", imei);
        				eventMap.put("imei", imei);
        				eventMap.put("e_fac", e_fac);
        				eventMap.put("e_num", e_num);
        				eventMap.put("time", String.valueOf(time));
        				eventMap.put("ori_value", ori_value);
        				eventMap.put("source", data_type);
        				eventMap.put("flag_reload", "0");//上报
        				eventMap.put("data_type", dataType);
        				eventMap.put("deal_flag", "0");
        				SaveEventDataToDB(eventMap,valueD);//zjq 20190719
        				log.info("task: insert gprs event into SQL end!");
        				
        				break;
    				default:
    					break;
        			}
        		}else if ( !"80".equals(endbyte) && "16".equals(endbyte)){//电表响应后台的是CX  "C0".equals(startbyte)
        			String e_num = Utilty.convertByteToString(binaryData, 3, 8);
        			log.info("task: C0 analysis: e_num:" + e_num);
        			String e_fac = orig_code.substring(16, 18);
        			log.info("task: C0 analysis: e_fac="+ e_fac);
        			String dataType = orig_code.substring(18, 20);
        			log.info("task: C0 analysis: dataType="+ dataType);
        			byte dataLen = binaryData[10];
        			log.info("task: C0 analysis: data Length="+ dataLen);
        			//数据域
        			String valueD = orig_code.substring(22, 22+dataLen*2);
        			
        			switch(dataType) {
        			//定时上报数据 冻结数据
        			case "A1"://读取丢帧项
        			case "FF"://读取多项数据
        			case "A3"://第几次日冻结数据
        			case "A5"://第几次月冻结数据
        				Map<String ,String> emMap = new HashMap<String,String>(); 
    					emMap.put("dev_id", imei);
    					emMap.put("time", String.valueOf(time));
	        			emMap.put("ori_value", ori_value);
    					emMap.put("source", data_type);
    					emMap.put("e_num", e_num);
    					emMap.put("e_fac", e_fac);
    					emMap.put("dataType", dataType);
        				emMap.put("flag_reload", "1");
        				SaveXBEMDataToDB(emMap,valueD);//20190618
				        log.info("task: CX gprs insert into SQL end!");
				        
        				break;
        			//事件数据
        			case "B1"://停电事件
            		case "B3"://拉合闸事件 
            		case "B5"://过流事件
        			case "B7"://过压事件
        			case "B9"://欠压事件
            		case "BB"://重启记录最近十次
            		case "BD"://编程记录最近十次
            			Map<String ,String> eventMap = new HashMap<String,String>();
            			eventMap.put("dev_id", imei);
        				eventMap.put("imei", imei);
        				eventMap.put("e_fac", e_fac);
        				eventMap.put("e_num", e_num);
        				eventMap.put("time", String.valueOf(time));
        				eventMap.put("ori_value", ori_value);
        				eventMap.put("source", data_type);
        				eventMap.put("flag_reload", "1");//下行
        				eventMap.put("data_type", dataType);
        				eventMap.put("deal_flag", "0");
        				SaveEventDataToDB(eventMap,valueD);//zjq 20190719
        				log.info("task: insert gprs event into SQL end!");
        				
        				break;
        			//抄表
        			case "10"://有功总电能 //C0014700000003190010050000000000F916
//        			case "11"://A相有功电能 //C0014700000003190010050000000000F916
//        			case "12"://B相有功电能 
//        			case "13"://C相有功电能
        			case "14"://有功余脉冲数
        			case "15"://无功总电能
        			case "19"://无功余脉冲数
        			case "1F"://电能读块
        			case "20"://总有功功率
        			case "21"://A相有功功率
        			case "22"://B相有功功率
        			case "23"://C相有功功率
        			case "24"://总无功功率
        			case "25"://A相无功功率
        			case "26"://B相无功功率
        			case "27"://C相无功功率
        			case "2F"://功率读块
        			case "31"://A相电压 //C00147000000031900310258226116
        			case "32"://B相电压 
        			case "33"://C相电压 
        			case "3F"://电压读块
        			case "41"://A相电流 //C001470000000319004103070000C316
        			case "42"://B相电流 
        			case "43"://C相电流
        			case "4F"://电流读块
        			case "50"://总功率因数
        			case "51"://A相功率因数
        			case "52"://B相功率因数
        			case "53"://C相功率因数
        			case "5F"://功率因数读块
        			case "60"://频率
        			case "70"://时间
        			case "71"://注册标识
        			case "72"://上报时间
        			case "73"://认证时长
        			case "74"://心跳间隔
        			case "80"://电表运行状态字1
        			case "81"://电表运行状态字2
        			case "82"://继电器控制字
        			case "83"://电表常数
        			case "90"://厂家标识
        			case "91"://硬件版本号
        			case "92"://软件版本号
        			case "93"://协议版本号
        			case "94"://表号
        			case "95"://通讯号
        			case "96"://用户号 
        				//事件次数
        			case "A0"://整点冻结次数 //C00147000000031900A0032802004116
        			case "A2"://日冻结次数 //C00147000000031900A20203006716
        			case "A4"://月冻结次数 //C00147000000031900A401016816
        			case "B0"://停电事件总次数 //C00147000000031900B00203005916
        			case "B2"://拉合闸总次数 //C00147000000031900B20209005116
        			case "B4"://过流总数 //C00147000000031900B40200005816
        			case "B6"://过压事件总数 //C00147000000031900B60200005616
        			case "B8"://欠压事件次数 //C00147000000031900B80200005416
        			case "BA"://模块重启次数 //C00147000000031900BA0200005216
        			case "BC"://编程记录次数
        			//抄表
        			case "F1"://读RAM的XXXXXXXX地址后的N字节
        			case "F2"://读ROM的XXXXXXXX地址后的N字节
        			case "F3"://读EEPROM的XXXXXXXX地址后的N字节
        			case "F4"://读计量芯片指定寄存器PP页，RR寄存器地址数据
        			case "F5"://IMSI ASCII //C00147000000031900F50F3436303034353539333130323938310116
        			case "F6"://IMEI  ASCII //C00147000000031900F60F383636393731303330333839333337F016
        			case "F7"://ICCID ASCII //C00147000000031900F7143839383630343335313031383930303532393830ED16
        			case "F8"://信号强度 //C00147000000031900F80124F116
        			case "F9"://驻网状态
        			case "FA"://模块监控状态字
        				log.info("task: c0 start analysis-----------dataType="+dataType);
        				if(mINBYDEMReadService == null) {
        					mINBYDEMReadService =  SpringContextUtils.getBean(INBYDEMReadService.class);
        				}
        				YDEMNBReadPo mYDEMNBReadPo = new YDEMNBReadPo();
        				Map<String ,String> result_read = EMDataAnalysisUtil.getReadValue(dataType,valueD);
        				mYDEMNBReadPo.setDevId(imei);
        				mYDEMNBReadPo.setImei(imei);
        				mYDEMNBReadPo.setOrigValue(ori_value);
        				mYDEMNBReadPo.setSource(data_type);
        		        mYDEMNBReadPo.setTime(time);
        		        mYDEMNBReadPo.setENum(e_num);
        		        mYDEMNBReadPo.setEFac(e_fac);
        		        mYDEMNBReadPo.setDataSeq(startbyte);
        		        mYDEMNBReadPo.setReadType(result_read.get("read_type") !=null ? result_read.get("read_type"):"");
        		        mYDEMNBReadPo.setReadValue(result_read.get("read_value") !=null ? result_read.get("read_value"):"");
        		        mINBYDEMReadService.addOne(mYDEMNBReadPo);
        		        log.info("task: c0 gprs insert into SQL end!");
        				break;
        			}
        		}
        	}
		}
	}
	
	/**
	 * lora 烟感数据接收解析
	 */
	private void LoraSmokeAnalysis() {
		log.info("--------------lora smoke sensor start --------------");
		try {
			com.alibaba.fastjson.JSONObject jo = JSON.parseObject(data);
			if (jo.containsKey("devEUI") && jo.containsKey("data")) {
//				LOGGER.info("lora data =====================" );
				String dev_eui = jo.getString("devEUI");
				String ori_value = jo.getString("data");
				String time_s = DateUtil.shortenTimeStr(jo.getString("time_s"));//2019-04-09 08:24:09.120570725
				long time = DateUtil.date2TimeStampLong(time_s, "yyyy-MM-dd HH:mm:ss");
				
				log.info("lora smoke sensor LoraAnalysis:dev_eui=" + dev_eui );
				log.info("lora smoke sensor LoraAnalysis:data=" + ori_value );
				log.info("lora smoke sensor LoraAnalysis:time_s=" + time_s );
				int len = ori_value.length();
		    	String orig_data = ori_value.substring(2, len).trim().replaceAll(" ", "");//去掉\x
		    	
		        String regex="^[A-Fa-f0-9]+$";//是16进制数
		    	if(orig_data.matches(regex)){
		    		//\x303230453030
		    		String dataFlag = orig_data.substring(0, 6);
		    		String dataStatus = orig_data.substring(6, 8);
		    		String dataEnd = orig_data.substring(8, 12);
		    		String dataStatus_type = "";
		    		log.info("task: lora smoke sensor receive: dataFlag:"+dataFlag);
		    		String orig_code = orig_data;
		    		if ("303230".equals(dataFlag) && "3030".equals(dataEnd)) {//水表上报发出的是68
			    		//System.out.println("AnalysisData15: 80----------------");
		    			log.info("task: lora smoke sensor dataStatus="+dataStatus);
	    				switch(dataStatus) {
	        			case "30"://正常
	        				dataStatus_type = "正常";
	        				break;
	        			case "31"://火警
	        				dataStatus_type = "火警";
	        				break;
	        			case "33"://低电
	        				dataStatus_type = "低电";
	        				break;
	        			case "34"://火警消除
	        				dataStatus_type = "火警消除";
	        				break;
	        			case "36"://低电恢复
	        				dataStatus_type = "低电恢复";
	        				break;
	        			case "44"://按键自测
	        				dataStatus_type = "按键自测";
	        				break;
	        			case "45"://探测器上电
	        				dataStatus_type = "探测器上电";
	        				break;
	    				default:
	    					break;
	    				}
	    				log.info("task: lora smoke sensor dataStatus_type="+dataStatus_type);
	    				if(mISmokeDataService == null) {
	    					mISmokeDataService =  SpringContextUtils.getBean(ISmokeDataService.class);
        				}
	    				SmokeDataPo mSmokeDataPo = new SmokeDataPo();
	    				mSmokeDataPo.setDevId(dev_eui);
	    				mSmokeDataPo.setOrigValue(ori_value);
	    				mSmokeDataPo.setSource("lora_smoke");
	    				mSmokeDataPo.setTime(time);
	    				mSmokeDataPo.setDataStatus(dataStatus_type);
	    				mISmokeDataService.insert(mSmokeDataPo);
	    				log.info("task: lora smoke insert into SQL end!");
		    		}
		    	}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	/**
	 * udp水表数据接收解析
	 */
	private void UdpWMAnalysis(String ori_value,String ip,int port) {
		try {
			int len = ori_value.length();
	    	String orig_data = ori_value;//ori_value.substring(2, len).trim().replaceAll(" ", "");//去掉\x
	    	long time = System.currentTimeMillis();
	        String regex="^[A-Fa-f0-9]+$";//是16进制数
	    	if(orig_data.matches(regex)){
	    		String dataFlag = orig_data.substring(0, 12);
	    		log.info("task: lora data receive: dataFlag:"+dataFlag);
	    		String orig_code = orig_data;
	    		if ("00006CFEFEFE".equals(dataFlag)) {//lora 数据
	    			orig_code = orig_data.substring(12, orig_data.length());
//		    		System.out.println("task: data receive: orig_code:"+orig_code);
	    			byte[] binaryData = Utilty.hexStringToBytes(orig_code);
		    		len = binaryData.length;
		    		String startbyte = Integer.toHexString(binaryData[0] & 0xFF).toUpperCase() ;
		    		String endbyte = Integer.toHexString(binaryData[len - 1] & 0xFF) ;
		    		log.info("analysis: startbyte:"+ startbyte );
		    		log.info("analysis: endbyte:"+ endbyte);
		    		//00006CFEFEFE //6810 54512020000000 81 //5C  //数据长度域 
		    		//921F01****//2A030B000100//FFFF2E690016
		    		
		    		if ("68".equals(startbyte) && "16".equals(endbyte)) {//水表上报发出的是68
		    			//表号
		    			String w_num = Utilty.convertByteToString(binaryData, 3, 9);
		    			System.out.println("task: lora analysis: e_num:" + w_num);
		    			//控制字
		    			String control_code = orig_code.substring(18, 20);
		    			System.out.println("task: lora analysis: control_code="+ control_code);
		    			//数据长度
		    			int dataLen = Integer.parseInt(orig_code.substring(20, 22), 16);
		    			System.out.println("task: lora analysis: data Length="+ dataLen);
		    			
	    				//数据域
	    				String valueD = orig_code.substring(22, 22+dataLen*2);
	    				System.out.println("task: lora fefefe 68 valueD="+valueD);
	    				switch(control_code) {
	        			case "81"://读计量数据2
	        				Map<String ,String> result_read = EMDataAnalysisUtil.getUdpWMDataValue(valueD);
	        				if(mIWMDataService == null) {
	        					mIWMDataService =  SpringContextUtils.getBean(IWMDataService.class);
	        				}
	        				WMDataPo wmDataPo = new WMDataPo();
	        				wmDataPo.setWmNum(w_num);
	        				wmDataPo.setTime(time);
	        				wmDataPo.setOrigValue(orig_data);
	        				wmDataPo.setDevId(result_read.get("imei"));
	        				wmDataPo.setSource(data_type);
	        				wmDataPo.setDataFlag1(result_read.get("data_flag1"));
	        				wmDataPo.setDataFlag2(result_read.get("data_flag2"));
	        				wmDataPo.setDataSeq(result_read.get("data_seq"));
	        				wmDataPo.setWmFlow(result_read.get("wm_flow"));
	        				wmDataPo.setWmFlowRate(result_read.get("wm_flow_rate"));
	        				wmDataPo.setWmReverseFlow("");
	        				wmDataPo.setWmTime(result_read.get("wm_time"));
	        				wmDataPo.setWmStatu1(result_read.get("wm_statu1"));
	        				wmDataPo.setWmStatu2("");
	        				wmDataPo.setSendIp(ip.substring(1,ip.length()));
	        				wmDataPo.setSendPort(port);
	        				mIWMDataService.addOne(wmDataPo);
	        				System.out.println("task: udp wm insert into SQL end!");
	        				
	        				break;
	        			case "83"://读表头地址
	        				break;
	        			case "84"://强制开关阀,自由开关阀
	        				break;
	    				default:
	    					break;
	    				}
		    		}
	    		}else if ("010000000017".equals(dataFlag) || "010000000016".equals(dataFlag)) {//读阀值状态
	    			//010000000017FEFEFE685554512020000000A407A0170140429916361600000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
		    		//010000000016FEFEFE685554512020000000A406A0170140421CA2160000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
	    			//010000000017FEFEFE685554512020000000A407A0170140225516D216
	    			orig_code = orig_data.substring(18, orig_data.length());
//		    		System.out.println("task: data receive: orig_code:"+orig_code);
	    			byte[] binaryData = Utilty.hexStringToBytes(orig_code);
		    		len = binaryData.length;
		    		String startbyte = Integer.toHexString(binaryData[0] & 0xFF).toUpperCase() ;
		    		String endbyte = Integer.toHexString(binaryData[len - 1] & 0xFF) ;
		    		log.info("analysis: startbyte:"+ startbyte );
		    		log.info("analysis: endbyte:"+ endbyte);
		    		//00006CFEFEFE //6810 54512020000000 81 //5C  //数据长度域 
		    		//921F01****//2A030B000100//FFFF2E690016
		    		
		    		if ("68".equals(startbyte) /*&& "16".equals(endbyte)*/) {//水表上报发出的是68
		    			//表号
		    			String w_num = Utilty.convertByteToString(binaryData, 3, 9);
		    			System.out.println("task: lora analysis: e_num:" + w_num);
		    			//控制字
		    			String control_code = orig_code.substring(18, 20);
		    			System.out.println("task: lora analysis: control_code="+ control_code);
		    			//数据长度
		    			int dataLen = Integer.parseInt(orig_code.substring(20, 22), 16);
		    			System.out.println("task: lora analysis: data Length="+ dataLen);
		    			
	    				//数据域
	    				String valueD = orig_code.substring(22, 22+dataLen*2);//A0170140225516
	    				System.out.println("task: lora fefefe 68 valueD="+valueD);
	    				switch(control_code) {
	        			case "A4"://读计量数据2
	        				Map<String ,String> result_read = EMDataAnalysisUtil.getUdpWMReadDataValue(valueD);
	        				if(mIWMDataService == null) {
	        					mIWMDataService =  SpringContextUtils.getBean(IWMDataService.class);
	        				}
	        				WMReadDataPo mWMReadDataPo = new WMReadDataPo();
	        				mWMReadDataPo.setOrigValue(orig_data);
	        				mWMReadDataPo.setSource(data_type);
	        				mWMReadDataPo.setTime(String.valueOf(time));
	        				mWMReadDataPo.setType(result_read.get("type"));
	        				mWMReadDataPo.setWmNum(w_num);
	        				mWMReadDataPo.setValue(result_read.get("switch_status"));
	        				mIWMDataService.insertReadData(mWMReadDataPo);
	        				System.out.println("task: udp wm read insert into SQL end!");
	        				break;
	        				default:
	        					break;
	    				}
		    		}
	    		}else if(orig_data.length() > 0){
	    			WMReadDataPo mWMReadDataPo = new WMReadDataPo();
    				mWMReadDataPo.setOrigValue(orig_data);
    				mWMReadDataPo.setSource(data_type);
    				mWMReadDataPo.setTime(String.valueOf(time));
    				mWMReadDataPo.setType("");
    				mWMReadDataPo.setWmNum("");
    				mWMReadDataPo.setValue("");
    				mIWMDataService.insertReadData(mWMReadDataPo);
	    		}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	/**
	 * 判定当前上报数据是否是下行指令后电表上报的
	 */
	private static boolean ifUpDateSeq(String ser_seq){
		boolean isUpDate = false;
		if(ser_seq.isEmpty()) {
			return false;
		}
		//服务序号
		String regex="^[A-Fa-f0-9]+$";//是16进制数
    	if(ser_seq.matches(regex)){
    		//十进制：192--255
			//十六进制：C0--FF
			int seq_dec = Integer.parseInt(ser_seq,16);
			if(seq_dec >= 192 && seq_dec <=255){
			System.out.println("上报序列号-----------ser_seq="+ser_seq);
				return true;
			}
		}
    	return isUpDate;
	}
	
	/**
	 * 保存芯北电表（nb,lora,2g）上报数据
	 * @param map
	 * @param valueD
	 */
	private void SaveXBEMDataToDB(Map<String,String> map,String valueD) {
		if(mIXBEMDataService == null) {
			mIXBEMDataService =  SpringContextUtils.getBean(IXBEMDataService.class);
		}
		if(map !=null && map.size() >0 && valueD != null) {
			Map<String ,String> d_result = EMDataAnalysisUtil.getDataValue(valueD);
			if(d_result.size() > 0) {
				XBEMDataPo dataPo = new XBEMDataPo();
				dataPo.setDevId(map.get("dev_id"));
				dataPo.setOrigValue(map.get("ori_value"));
				dataPo.setTime(Long.parseLong(map.get("time")));
				dataPo.setSource(map.get("source"));
				dataPo.setEFac(map.get("e_fac"));
				dataPo.setENum(map.get("e_num"));
				dataPo.setFlagReload(Integer.parseInt(map.get("flag_reload")));
				dataPo.setDataType(map.get("dataType"));
				
				dataPo.setSeqType(d_result.get("seq_type") !=null ? d_result.get("seq_type"):"FF");
				dataPo.setESeq(Long.parseLong(d_result.get("e_seq")!=null ? d_result.get("e_seq"):"0"));
				dataPo.setETime(d_result.get("e_time") !=null ? d_result.get("e_time"):"");
				dataPo.setESignal(d_result.get("e_signal") !=null ? d_result.get("e_signal"):"");
				
				dataPo.setEKwh1(d_result.get("e_kwh1") !=null ? d_result.get("e_kwh1"):"");//有功总电能
				dataPo.setEKwh2(d_result.get("e_kwh2") !=null ? d_result.get("e_kwh2"):"");
				dataPo.setEKw1All(d_result.get("e_kw1_all") !=null ? d_result.get("e_kw1_all"):"");//总有功功率
				dataPo.setEKw2All(d_result.get("e_kw2_all") !=null ? d_result.get("e_kw2_all"):"");
		        
				dataPo.setEVoltageA(d_result.get("e_voltage_a") !=null ? d_result.get("e_voltage_a"):"");//A相电压
				dataPo.setEVoltageB(d_result.get("e_voltage_b") !=null ? d_result.get("e_voltage_b"):"");
				dataPo.setEVoltageC(d_result.get("e_voltage_c") !=null ? d_result.get("e_voltage_c"):"");
				dataPo.setECurrentA(d_result.get("e_current_a") !=null ? d_result.get("e_current_a"):"");//A相电流
				dataPo.setECurrentB(d_result.get("e_current_b") !=null ? d_result.get("e_current_b"):"");
				dataPo.setECurrentC(d_result.get("e_current_c") !=null ? d_result.get("e_current_c"):"");
				dataPo.setEFactorAll(d_result.get("e_factor_all") !=null ? d_result.get("e_factor_all"):"");//总功率因数
				dataPo.setEStatu1(d_result.get("e_statu1") !=null ? d_result.get("e_statu1"):"");//状态字1
				dataPo.setEStatu2(d_result.get("e_statu2") !=null ? d_result.get("e_statu2"):"");//状态字2
				dataPo.setESwitch(d_result.get("e_switch") !=null ? d_result.get("e_switch"):"");//继电器
				
				mIXBEMDataService.addOne(dataPo);
//		        LOGGER.info("task: lora fefefefe 80 insert into SQL end!");
			}
		}
	}

	/**
	 * 保存上报事件，不区分lora和NB
	 * @param map
	 * @param valueD
	 */
	private void SaveEventDataToDB(Map<String,String> map,String valueD) {
		if(mINBYDEMEventService == null) {
			mINBYDEMEventService =  SpringContextUtils.getBean(INBYDEMEventService.class);
		}
		if(map !=null && map.size() >0 && valueD != null) {
			Map<String,String> result = EMDataAnalysisUtil.getEventDataValue(valueD);
			if(result.size() > 0) {
			YDEMeterEventPo mYDEMeterEventPo = new YDEMeterEventPo();
			mYDEMeterEventPo.setDevId(map.get("dev_id"));
			mYDEMeterEventPo.setImei(map.get("imei"));
			mYDEMeterEventPo.setEFac(map.get("e_fac"));
			mYDEMeterEventPo.setENum(map.get("e_num"));
			mYDEMeterEventPo.setTime(Long.parseLong(map.get("time")));//时间长类型
			mYDEMeterEventPo.setOrigValue(map.get("ori_value"));
			mYDEMeterEventPo.setFlagReload(Integer.parseInt(map.get("flag_reload")));//重传上报
			mYDEMeterEventPo.setSource(map.get("source"));
			mYDEMeterEventPo.setEventType(map.get("data_type"));
			mYDEMeterEventPo.setDealFlag(Integer.parseInt(map.get("deal_flag")));
			
			mYDEMeterEventPo.setESeq(Long.parseLong(result.containsKey("e_seq") ? result.get("e_seq"):"0"));
			mYDEMeterEventPo.setEStartTime( result.containsKey("start_time") ? result.get("start_time"):"");
			mYDEMeterEventPo.setEEndTime(result.containsKey("end_time")  ? result.get("end_time"):"");
			
			mYDEMeterEventPo.setEStartKwh(result.containsKey("start_kwh1")  ? result.get("start_kwh1"):"");
			mYDEMeterEventPo.setEStartKwh2(result.containsKey("start_kwh2")  ? result.get("start_kwh2"):"");
			mYDEMeterEventPo.setEEndKwh(result.containsKey("end_kwh1")  ? result.get("end_kwh1"):"");
			mYDEMeterEventPo.setEEndKwh2(result.containsKey("end_kwh2")  ? result.get("end_kwh2"):"");
			
			mYDEMeterEventPo.setEStartVoltageA(result.containsKey("start_voltage_a")  ? result.get("start_voltage_a"):"");
			mYDEMeterEventPo.setEStartVoltageB(result.containsKey("start_voltage_b")  ? result.get("start_voltage_b"):"");
			mYDEMeterEventPo.setEStartVoltageC(result.containsKey("start_voltage_c")  ? result.get("start_voltage_c"):"");
			mYDEMeterEventPo.setEEndVoltageA(result.containsKey("end_voltage_a")  ? result.get("end_voltage_a"):"");
			mYDEMeterEventPo.setEEndVoltageB(result.containsKey("end_voltage_b")  ? result.get("end_voltage_b"):"");
			mYDEMeterEventPo.setEEndVoltageC(result.containsKey("end_voltage_c")  ? result.get("end_voltage_c"):"");
			
			mYDEMeterEventPo.setEStartCurrentA(result.containsKey("start_current_a")  ? result.get("start_current_a"):"");
			mYDEMeterEventPo.setEStartCurrentB(result.containsKey("start_current_b")  ? result.get("start_current_b"):"");
			mYDEMeterEventPo.setEStartCurrentC(result.containsKey("start_current_c")  ? result.get("start_current_c"):"");
			mYDEMeterEventPo.setEEndCurrentA(result.containsKey("end_current_a")  ? result.get("end_current_a"):"");
			mYDEMeterEventPo.setEEndCurrentB(result.containsKey("end_current_b")  ? result.get("end_current_b"):"");
			mYDEMeterEventPo.setEEndCurrentC(result.containsKey("end_current_c")  ? result.get("end_current_c"):"");
			
			mYDEMeterEventPo.setEStartStatus1(result.containsKey("start_status1")  ? result.get("start_status1"):"");
			mYDEMeterEventPo.setEEndStatus1(result.containsKey("end_status1")  ? result.get("end_status1"):"");
			mYDEMeterEventPo.setEStartStatus2(result.containsKey("start_status2")  ? result.get("start_status2"):"");
			mYDEMeterEventPo.setEEndStatus2(result.containsKey("end_status2")  ? result.get("end_status2"):"");
			mYDEMeterEventPo.setEStartSwitch(result.containsKey("start_switch")  ? result.get("start_switch"):"");
			mYDEMeterEventPo.setEEndSwitch(result.containsKey("end_switch")  ? result.get("end_switch"):"");
			mYDEMeterEventPo.setEEndModule(result.containsKey("start_module")  ? result.get("start_module"):"");
			mYDEMeterEventPo.setEStartModule(result.containsKey("end_module") ? result.get("end_module"):"");
			mINBYDEMEventService.addOne(mYDEMeterEventPo);
//			LOGGER.info("task: insert event into SQL end!");
			
			}
		}
		
	}
	/**
	 * 定时任务，获取日用电量
	 * @param map
	 */
	private void selectEMDayValue(Map<String,Object> map) {
		if(mIXBEMService == null) {
			mIXBEMService =  SpringContextUtils.getBean(IXBEMService.class);
		}
		
		EMMeterPo mEMMeterPo = (EMMeterPo) map.get("EMMeterPo");
		String e_num = (String)map.get("e_num");
		long time = (long)map.get("time");
		
		long beginTime = time-24*60*60*1000;
		long endTime = time;
		
		String imei = mEMMeterPo.getImei();
		String companyId = mEMMeterPo.getCompanyId();
		String projectName = mEMMeterPo.getProjectName();
		String  projectNo= mEMMeterPo.getProjectNo();
		log.info("--------------selectEMDayValue: e_num ="+e_num);
//		log.info("--------------selectEMDayValue: beginTime ="+beginTime);
//		log.info("--------------selectEMDayValue: endTime ="+endTime);
//		log.info("--------------selectEMDayValue: IMEI ="+imei);
//		log.info("--------------selectEMDayValue: CompanyId ="+companyId);
//		log.info("--------------selectEMDayValue: ProjectName ="+projectName);
//		log.info("--------------selectEMDayValue: projectNo ="+projectNo);
		
		XBEMDataPo startXBEMDataPo = mIXBEMService.selcetStartValue(e_num, beginTime, endTime);
		XBEMDataPo endXBEMDataPo = mIXBEMService.selcetEndValue(e_num, beginTime, endTime);
		
		if(startXBEMDataPo != null && endXBEMDataPo != null) {
			String e_value1 = startXBEMDataPo.getEKwh1();
			long e_seq1 = startXBEMDataPo.getESeq();
//			log.info("<<<<<<<<<<<<<<<<selectEMDayValue:e_num ="+e_num + ", e_value1 ="+e_value1);
//			log.info("<<<<<<<<<<<<<<<<selectEMDayValue:e_num ="+e_num + ",  e_seq1 ="+e_seq1);
			String e_value2 = endXBEMDataPo.getEKwh1();
			long e_seq2 = endXBEMDataPo.getESeq();
//			log.info(">>>>>>>>>>>>>>>>>>>>>>selectEMDayValue:e_num ="+e_num + ",  e_value2 ="+e_value2);
//			log.info(">>>>>>>>>>>>>>>>>>>>>>selectEMDayValue:e_num ="+e_num + ",  e_seq2 ="+e_seq2);
			
			String eValue = getDiffValue(e_value1,e_value2);
			EMDayReportPo mEMDayReportPo = new EMDayReportPo(); 
			mEMDayReportPo.setMeterNo(e_num);
			mEMDayReportPo.setProjectNo(projectNo);
			mEMDayReportPo.setYear(DateUtil.getYear(beginTime));
			mEMDayReportPo.setMonth(DateUtil.getMonth(beginTime));
			mEMDayReportPo.setCompanyId(companyId);
			mEMDayReportPo.setEvalue1(e_value1);
			mEMDayReportPo.setEseq1(String.valueOf(e_seq1));
			mEMDayReportPo.setEvalue2(e_value2);
			mEMDayReportPo.setEseq2(String.valueOf(e_seq2));
			mEMDayReportPo.setEvalue(eValue);
			mEMDayReportPo.setYearmonth(Integer.parseInt(DateUtil.getYearMonthDay(beginTime)));
			mEMDayReportPo.setDay(DateUtil.getDay(beginTime));
			mEMDayReportPo.setTbMeterIdNew("");//不填
			mEMDayReportPo.setTbMeterIdOld("");//不填
			mEMDayReportPo.setNewMeterValue("");//不填
			mEMDayReportPo.setOldMeterValue("");//不填
			mIXBEMService.SaveOne(mEMDayReportPo);
		}
	}
	/**
	 * 计算两个电量的差值
	 * @param value1
	 * @param value2
	 * @return
	 */
	private String getDiffValue(String value1,String value2) {
		double v1 = Double.parseDouble(value1);
		double v2 = Double.parseDouble(value2);
		
		double v3 = v2 - v1;
		return String.valueOf(v3);
	}

	public AsyncService getAsyncService() {
		return asyncService;
	}

	public void setAsyncService(AsyncService asyncService) {
		this.asyncService = asyncService;
	}
	
	private void AKRNBEMeterAnalysis() throws JSONException {
		YDUtil.BodyObj obj = YDUtil.resolveBody(data, false);
		org.json.JSONObject data = (org.json.JSONObject) obj.getMsg();
//		LOGGER.info("task: data receive: dev_id:"+data.getLong("dev_id"));
		int type = data.getInt("type");
		if(type == 1) {//value 数据点消息
	        log.info("task: data receive: imei:"+data.getString("imei"));
	        String ori_value = data.getString("value").toLowerCase();
	        String dev_id = data.getLong("dev_id")+"";
	        String imei = data.getString("imei");
	        long time = data.getLong("at");
	        
	        int len = ori_value.length();
	        String startbyte = ori_value.substring(0, 4).toLowerCase();
			String endbyte = ori_value.substring(len-4, len).toLowerCase() ;
//			System.out.println("analysis: startbyte:"+ startbyte );
//			System.out.println("analysis: endbyte:"+ endbyte);
			
			String dataCRC = ori_value.substring(len-8, len-4);
//			System.out.println("task: akr nb analysis: dataCRC="+ dataCRC);
			
			if ("7b7b".equals(startbyte) && "7d7d".equals(endbyte)) {	
				String dataType = ori_value.substring(4, 6);
				log.info("task: akr nb analysis: dataType="+ dataType);
				//表号
				String e_num_str = ori_value.substring(6, 46);
				String e_num_str1 = Utilty.hexStringToString(e_num_str);
				String e_num = e_num_str1.substring(0,14);//12006130940002
				log.info("task: akr nb analysis: e_num="+ e_num);
				
				//7b7bab31323030383237343435303030330000000000007d7d
				String orig_code = "";
				if(("ab").equals(dataType) || ("AB").equals(dataType)) {//心跳数据不做另外处理
					log.info("task: akr nb 心跳数据，imei="+ imei);
					return;
				}else {
					orig_code = ori_value.substring(46,len-8);
				}
				
				switch(dataType) {
				case "ab":
					log.info("task: akr nb 心跳数据，imei="+ imei);
					break;
				case "91"://数据上传
					String[] origArr = orig_code.split("29295d5d");// 根据))]]分割
//			        System.out.println("length="+origArr.length  + "\n"); 
			        Map<String ,Object> d_result = new HashMap<String,Object>();
			        for (int i = 0; i < origArr.length; ++i){
			        	String dataStr = origArr[i]; 
//			        	System.out.println("str="+ dataStr + "\n");
		        		String sub = dataStr.substring(14,dataStr.length());//截取[[1-1((
//		        		System.out.println("sub1="+ sub  + "\n"); 
		        		long dataLen = Long.parseLong(sub.substring(4,6), 16);
//		        		System.out.println("dataLen="+ dataLen  + "\n"); 
		        		String dataValue = sub.substring(6,sub.length()-4);
//		        		System.out.println("dataValue="+ dataValue  + "\n");
		        		d_result = EMDataAnalysisUtil.getAKRDataValue(d_result,i,dataValue);//多段数据
			        }
			        d_result.put("imei", imei);
			        d_result.put("dev_id", imei);
			        d_result.put("time", time);
			        d_result.put("type", type);
			        d_result.put("ori_value", ori_value);
			        d_result.put("source", data_type);
			        d_result.put("e_num", e_num);
			        d_result.put("dataType", dataType);
			        d_result.put("flag_reload", "0");
					SaveAKREMDataToDB(d_result);
					log.info("task: akr nb data insert into db !");
					break;
				case "90"://读取数据
					 log.info("task: akr read------ 90");
					//0x7B7B 90 3132303036313330393430303032000000000000 010304 00000000 FA33 E365 7D7D (HEX)
					//orig_code 010304 00000000 FA33 
					 //0x7B7B90 3132333231333231333231333231000000000000 010302 0000 B844 3E10 7D7D (HEX) //拉合闸状态
					 //010302 0000 B844
					 
					String dataValue = orig_code.substring(6,orig_code.length()-4);
    		        log.info("task: akr read dataValue=" + dataValue);
    		        if(dataValue == null || dataValue.equals("")) {
    		        	return;//过滤心跳数据
    		        }
					Map<String ,Object> d_result90 = EMDataAnalysisUtil.getAKRReadValue(dataValue);//回传数据
    		        String readValue = (String) d_result90.get("value");
    		        
    		        if(mIAKREMDataService == null) {
    					mIAKREMDataService =  SpringContextUtils.getBean(IAKREMDataService.class);
    				}
					AKREMReadPo mAKREMReadPo = new AKREMReadPo();
    				mAKREMReadPo.setDevId(dev_id);
    				mAKREMReadPo.setImei(imei);
    				mAKREMReadPo.setOrigValue(ori_value);
    				mAKREMReadPo.setSource(data_type);
    		        mAKREMReadPo.setTime(System.currentTimeMillis());
    		        mAKREMReadPo.setENum(e_num);
    		        mAKREMReadPo.setDataSeq("");
//    		        mAKREMReadPo.setRead_type(data_type_akr);
    		        mAKREMReadPo.setReadValue(readValue);
    		        mIAKREMDataService.insertReadData(mAKREMReadPo);
    		        log.info("task: akr read insert into SQL end!");
    		        
					break;
				case "A1" :
				case "a1"://告警
					 //0x7B7BA1 3132303037303335373230303031000000000000 
					//5B5B312D312828 [[1-1((
					//01 03 54 800F 0A5A 006407A800640258006400320064000000000000000000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000DB86
					//29295D5D ))]]
					//894E7D7D (HEX)
					Map<String ,Object> d_result_alt = new HashMap<String,Object>();
					String[] origArr_alt = orig_code.split("29295d5d");// 根据))]]分割
//			        System.out.println("length="+origArr.length  + "\n"); 
			        for (int i = 0; i < origArr_alt.length; ++i){
			        	String dataStr = origArr_alt[i]; 
//			        	System.out.println("str="+ dataStr + "\n");
		        		String sub = dataStr.substring(14,dataStr.length());//截取[[1-1((
//		        		System.out.println("sub1="+ sub  + "\n"); 
		        		long dataLen = Long.parseLong(sub.substring(4,6), 16);
//		        		System.out.println("dataLen="+ dataLen  + "\n"); 
		        		String dataValue_alt = sub.substring(6,sub.length()-4);
//		        		System.out.println("dataValue="+ dataValue  + "\n");
		        		d_result_alt = EMDataAnalysisUtil.getAKREventDataValue(d_result_alt,i,dataValue_alt);//多段数据
			        }
			        
			        d_result_alt.put("imei", imei);
			        d_result_alt.put("dev_id", imei);
			        d_result_alt.put("time", time);
			        d_result_alt.put("type", type);
			        d_result_alt.put("ori_value", ori_value);
			        d_result_alt.put("source", data_type);
			        d_result_alt.put("e_num", e_num);
			        d_result_alt.put("dataType", dataType);
					SaveAKREMEventDataToDB(d_result_alt);
					log.info("task: akr nb event insert into db !");
					break;
				case "93"://对时
				case "84":
				case "89":
				case "A3":
				case "a3":
				case "87":
				case "88":
				case "82":
					Map<String ,Object> d_result2 = new HashMap<String,Object>();
					d_result2.put("imei", imei);
			        d_result2.put("dev_id", imei);
			        d_result2.put("time", time);
			        d_result2.put("type", type);
			        d_result2.put("ori_value", ori_value);
			        d_result2.put("source", data_type);
			        d_result2.put("e_num", e_num);
			        d_result2.put("dataType", dataType);
			        d_result2.put("flag_reload", "0");
					SaveAKREMDataToDB(d_result2);
					log.info("task: akr nb data insert into db !");
					break;	
					default:
						break;
				}
			}else {//异常，保存
				Map<String ,Object> d_result = new HashMap<String,Object>();
				d_result.put("imei", imei);
		        d_result.put("dev_id", imei);
		        d_result.put("time", time);
		        d_result.put("type", type);
		        d_result.put("ori_value", ori_value);
		        d_result.put("source", data_type);
//		        d_result.put("e_num", e_num);
//		        d_result.put("dataType", dataType);
		        d_result.put("flag_reload", "0");
				SaveAKREMDataToDB(d_result);
				log.info("task: akr nb data insert into db !");
				log.info("task: akr nb data insert into db !");
			}
		}else if (type == 2 ) {//login 设备上下线消息
			//"msg":{"at":1553224749838,"login_type":10,"imei":"866971030389733","type":2,"dev_id":520156945,"status":1}
			if(nbYDEMStatusService == null) {
				nbYDEMStatusService =  SpringContextUtils.getBean(INBYDEMStatusService.class);
			}
			String dev_id = data.getLong("dev_id")+"";
			String imei = data.getString("imei");
			int status = data.getInt("status");
			int login_type = data.getInt("login_type");
			long time = data.getLong("at");
			log.info("task: : nbYDEMStatusService : dev_id="+dev_id);
			log.info("task: : nbYDEMStatusService : imei="+imei);
			log.info("task: : nbYDEMStatusService : 设备上下线 status="+status);
			log.info("task: : nbYDEMStatusService : login_type="+login_type);
			log.info("task: : nbYDEMStatusService : time="+time);
			
			Map<String ,Object> d_result = new HashMap<String,Object>();
			d_result.put("imei", imei);
	        d_result.put("dev_id", dev_id);
	        d_result.put("time", time);
	        d_result.put("type", type);
	        d_result.put("login_type", login_type);
	        d_result.put("status", status);
	        SaveNBStatus(d_result);
	        log.info("task: status insert into SQL end!");
		}
	}
	/**
	 * 安科瑞 2G电表数据接收，解析
	 */
	private void AKR2GEMeterAnalysis() {
		 String ori_value = data;
	        long time = System.currentTimeMillis();
	        int len = ori_value.length();
	        
	        log.info("task: akr 2g data receive: ori_value:"+ori_value);
    		String startbyte = ori_value.substring(0, 4);
    		String endbyte = ori_value.substring(len-4, len) ;
//	    		LOGGER.info("analysis: startbyte:"+ startbyte );
//	    		LOGGER.info("analysis: endbyte:"+ endbyte);
//    		if ("7b7b".equals(startbyte) && "7d7d".equals(endbyte)) {
//	    			String imei = ori_value.substring(4,19);
//	    			String status = ori_value.substring(19,20);
//	    			log.info("analysis:power off alert imei:"+ imei );
//	    			log.info("analysis:power off alert status:"+ status);
    			AKREMDataPo mAKREMDataPo = new AKREMDataPo();
    			mAKREMDataPo.setDevId("");
    			mAKREMDataPo.setOrigValue(ori_value);
    			mAKREMDataPo.setSource(data_type);
    			mAKREMDataPo.setTime(time);
				if(mIAKREMDataService == null) {
					mIAKREMDataService =  SpringContextUtils.getBean(IAKREMDataService.class);
				}
				mIAKREMDataService.addOne(mAKREMDataPo);
				log.info("task: akr 2g data insert into db !");
//    		}
//	        String regex="^[A-Fa-f0-9]+$";
//	    	if(ori_value.matches(regex) ){
//	    	}
	}
	
	/**
	 * 保存安科瑞电表nb 上下线信息
	 * @param map
	 * @param valueD
	 */
	private void SaveNBStatus(Map<String,Object> d_result) {
		if(d_result !=null && d_result.size() >0 ) {
			String dev_id = (String)d_result.get("dev_id");
			String imei = (String)d_result.get("imei");
			YDEMeterStatusPo mYDEMeterStatusPo = new YDEMeterStatusPo();
			mYDEMeterStatusPo.setDevId((String)d_result.get("dev_id"));
			mYDEMeterStatusPo.setImei((String)d_result.get("imei"));
			mYDEMeterStatusPo.setType((int)d_result.get("type"));
			mYDEMeterStatusPo.setLoginType((int)d_result.get("login_type"));
			mYDEMeterStatusPo.setStatus((int)d_result.get("status"));
			mYDEMeterStatusPo.setTime((long)d_result.get("time"));
			log.info("task: : nbYDEMStatusService: in insertStatus");
	        List<YDEMeterStatusPo> list = nbYDEMStatusService.selectDevid(dev_id,imei);
	        if(list.size()>=1) {//存在，更新
	        	nbYDEMStatusService.updateEMStatus(mYDEMeterStatusPo);
	        }else {//数据库中不存在，插入
	        	nbYDEMStatusService.insertStatus(mYDEMeterStatusPo);
	        }
		}
	}
	/**
	 * 保存安科瑞电表（nb,2g）上报数据
	 * @param map
	 * @param valueD
	 */
	private void SaveAKREMDataToDB(Map<String,Object> d_result) {
		if(mIAKREMDataService == null) {
			mIAKREMDataService =  SpringContextUtils.getBean(IAKREMDataService.class);
		}
		if(d_result !=null && d_result.size() >0 ) {
			AKREMDataPo dataPo = new AKREMDataPo();
			dataPo.setDevId((String)d_result.get("dev_id"));
			dataPo.setOrigValue((String)d_result.get("ori_value"));
			dataPo.setTime((long)d_result.get("time"));
			dataPo.setSource((String)d_result.get("source"));
//			dataPo.setE_fac(d_result.get("e_fac"));
			dataPo.setENum(d_result.containsKey("e_num") ? (String)d_result.get("e_num"):"");
//			dataPo.setFlag_reload(Integer.parseInt(d_result.get("flag_reload")));
			dataPo.setDataType((String)d_result.get("dataType"));
			
//			dataPo.setSeq_type(d_result.get("seq_type") !=null ? d_result.get("seq_type"):"FF");
			dataPo.setESeq(d_result.containsKey("e_seq") ? (long) d_result.get("e_seq"):0);
//			dataPo.setE_time(d_result.get("e_time") !=null ? d_result.get("e_time"):"");
			dataPo.setESignal(d_result.containsKey("e_signal") ? (String) d_result.get("e_signal"):"");
			
			dataPo.setEKwh1(d_result.containsKey("e_kwh1") ? (String)d_result.get("e_kwh1"):"");//有功总电能
			dataPo.setEKwh2(d_result.containsKey("e_kwh2") ? (String)d_result.get("e_kwh2"):"");
			dataPo.setEKw1All(d_result.containsKey("e_kw1_all")  ? (String)d_result.get("e_kw1_all"):"");//总有功功率
			dataPo.setEKw2All(d_result.containsKey("e_kw2_all") ? (String)d_result.get("e_kw2_all"):"");
	        
			dataPo.setEVoltageA(d_result.containsKey("e_voltage_a")  ? (String)d_result.get("e_voltage_a"):"");//A相电压
			dataPo.setEVoltageB(d_result.containsKey("e_voltage_b")  ? (String)d_result.get("e_voltage_b"):"");
			dataPo.setEVoltageC(d_result.containsKey("e_voltage_c") ? (String)d_result.get("e_voltage_c"):"");
			dataPo.setECurrentA(d_result.containsKey("e_current_a")  ? (String)d_result.get("e_current_a"):"");//A相电流
			dataPo.setECurrentB(d_result.containsKey("e_current_b")  ? (String)d_result.get("e_current_b"):"");
			dataPo.setECurrentC(d_result.containsKey("e_current_c")  ? (String)d_result.get("e_current_c"):"");
			dataPo.setEFactorAll(d_result.containsKey("e_factor_all")  ? (String)d_result.get("e_factor_all"):"");//总功率因数
			dataPo.setEStatu1(d_result.containsKey("e_statu1") ?  (String)d_result.get("e_statu1"):"");//状态字1
			dataPo.setEStatu2(d_result.containsKey("e_statu2") ?  (String)d_result.get("e_statu2"):"");//状态字2
			dataPo.setESwitch(d_result.containsKey("e_switch") ?  (String)d_result.get("e_switch"):"");//继电器
			mIAKREMDataService.addOne(dataPo);
		}
	}
	
	/**
	 * 保存安科瑞电表（nb,2g）上报告警数据
	 * @param map
	 * @param valueD
	 */
	private void SaveAKREMEventDataToDB(Map<String,Object> d_result) {
		if(mIAKREMDataService == null) {
			mIAKREMDataService =  SpringContextUtils.getBean(IAKREMDataService.class);
		}
		if(d_result !=null && d_result.size() >0 ) {
			AKREMEventPo dataPo = new AKREMEventPo();
			dataPo.setDevId((String)d_result.get("dev_id"));
			dataPo.setImei((String)d_result.get("imei"));
			dataPo.setOrigValue((String)d_result.get("ori_value"));
			dataPo.setTime((long)d_result.get("time"));
			dataPo.setSource((String)d_result.get("source"));
			dataPo.setENum(d_result.get("e_num")!=null ? (String)d_result.get("e_num"):"");
			dataPo.setEventType((String)d_result.get("dataType"));
			
			//解析出的数据
//			dataPo.setEStartTime(d_result.get("time"));
			dataPo.setOverVThreshold(d_result.get("over_v") !=null ? (String)d_result.get("over_v"):"");
			dataPo.setLowVThreshold(d_result.get("low_v") !=null ? (String)d_result.get("low_v"):"");
			dataPo.setOverAThreshold(d_result.get("over_a") !=null ? (String)d_result.get("over_a"):"");
			dataPo.setLowAThreshold(d_result.get("low_a") !=null ? (String)d_result.get("low_a"):"");
			dataPo.setOverKWThreshold(d_result.get("over_kw") !=null ? (String)d_result.get("over_kw"):"");
			dataPo.setLowKWThreshold(d_result.get("low_kw") !=null ? (String)d_result.get("low_kw"):"");
			dataPo.setOverVValue(d_result.get("over_v_value") !=null ? (String)d_result.get("over_v_value"):"");
			dataPo.setLowVValue(d_result.get("low_v_value") !=null ? (String)d_result.get("low_v_value"):"");
			dataPo.setOverAValue(d_result.get("over_a_value") !=null ? (String)d_result.get("over_a_value"):"");
			dataPo.setLowAValue(d_result.get("low_a_value") !=null ? (String)d_result.get("low_a_value"):"");
			dataPo.setOverKWValue(d_result.get("over_kw_value") !=null ? (String)d_result.get("over_kw_value"):"");
			dataPo.setLowKWValue(d_result.get("low_kw_value") !=null ? (String)d_result.get("low_kw_value"):"");
			dataPo.setAlertAllow(d_result.get("alert_allow") !=null ? (String)d_result.get("alert_allow"):"");
			dataPo.setAlertStatus(d_result.get("alert_status") !=null ? (String)d_result.get("alert_status"):"");//状态字
			dataPo.setAlertType(d_result.get("alert_status_type") !=null ? (String)d_result.get("alert_status_type"):"");
			dataPo.setEVValue(d_result.get("war_v_value") !=null ? (String)d_result.get("war_v_value"):"");
			dataPo.setEAValue(d_result.get("war_a_value") !=null ? (String)d_result.get("war_a_value"):"");
//			dataPo.setE_switch(d_result.get("e_switch") !=null ? d_result.get("e_switch"):"");//继电器
			mIAKREMDataService.insertEventData(dataPo);
		}
	}
	
	/**
	 * lora 烟感集中报警演示demo 数据接收解析
	 */
	private void LoraSmokeAlertDataAnalysis() {
		log.info("--------------lora smoke sensor start --------------");
		try {
			com.alibaba.fastjson.JSONObject jo = JSON.parseObject(data);
			if (jo.containsKey("devEUI") && jo.containsKey("data")) {
//				LOGGER.info("lora data =====================" );
				String dev_eui = jo.getString("devEUI");
				
				//此处演示用，如果设备在列表中继续解析，不在的话不处理
				String team_type = "smoke_sensor";
				String ori_value = jo.getString("data");
				String time_s = DateUtil.shortenTimeStr(jo.getString("time_s"));//2019-04-09 08:24:09.120570725
				long time = DateUtil.date2TimeStampLong(time_s, "yyyy-MM-dd HH:mm:ss");
				
				log.info("lora smoke sensor LoraAnalysis:dev_eui=" + dev_eui );
				log.info("lora smoke sensor LoraAnalysis:data=" + ori_value );
				log.info("lora smoke sensor LoraAnalysis:time_s=" + time_s );
				int len = ori_value.length();
		    	String orig_data = ori_value.substring(2, len).trim().replaceAll(" ", "");//去掉\x
		    	
		        String regex="^[A-Fa-f0-9]+$";//是16进制数
		    	if(orig_data.matches(regex)){
		    		//\x303230453030
		    		String dataFlag = orig_data.substring(0, 6);
		    		String dataStatus = orig_data.substring(6, 8);
		    		String dataEnd = orig_data.substring(8, 12);
		    		String dataStatus_type = "";
		    		log.info("task: lora smoke sensor receive: dataFlag:"+dataFlag);
		    		String orig_code = orig_data;
		    		if ("303230".equals(dataFlag) && "3030".equals(dataEnd)) {//水表上报发出的是68
			    		//System.out.println("AnalysisData15: 80----------------");
		    			log.info("task: lora smoke sensor dataStatus="+dataStatus);
	    				switch(dataStatus) {
	        			case "30"://正常
	        				dataStatus_type = "正常";
	        				break;
	        			case "31"://火警
	        				dataStatus_type = "火警";
	        				//火警时发下行
	        				sendCmdToSmoke(data_type,dev_eui,1,team_type);
	        				break;
	        			case "33"://低电
	        				dataStatus_type = "低电";
	        				break;
	        			case "34"://火警消除
	        				dataStatus_type = "火警消除";
	        				//火警消除时发下行
	        				sendCmdToSmoke(data_type,dev_eui,4,team_type);
	        				break;
	        			case "36"://低电恢复
	        				dataStatus_type = "低电恢复";
	        				break;
	        			case "44"://按键自测
	        				dataStatus_type = "按键自测";
	        				break;
	        			case "45"://探测器上电
	        				dataStatus_type = "探测器上电";
	        				break;
	    				default:
	    					break;
	    				}
	    				
	    				log.info("task: lora smoke sensor dataStatus_type="+dataStatus_type);
	    				Map<String ,String> dataMap = new HashMap<String ,String>();
	    				dataMap.put("dev_id", dev_eui);
	    				dataMap.put("time", String.valueOf(time));
	    				dataMap.put("ori_value", ori_value);
	    				dataMap.put("source", "lora_smoke");
	    				dataMap.put("dataStatus_type", dataStatus_type);
	    				saveSmokeData(dataMap);
		    		}
		    	}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	private void sendCmdToSmoke(String deviceType,String dev_eui,int cmdType,String team_type) {
		String msg="";
		String request_id = "1234abcd";
		if(cmdType == 1) {//报警
			msg="303230313030";//16进制：020100
		}else if(cmdType == 4){//报警消除
			msg="303230343030";//16进制：020400
		}
		if(mISmokeDataService == null) {
			mISmokeDataService =  SpringContextUtils.getBean(ISmokeDataService.class);
		}
		//给上报设备下发,连续4次
		if(dev_eui != null) {
			for(int i =0;i<5;i++) {
				LoraSendCmds mLoraSendCmds = new LoraSendCmds();
				String result = mLoraSendCmds.sendMsg(deviceType,msg,dev_eui,request_id);
				
				SmokeDownPo mSmokeDownPo = new SmokeDownPo();
				mSmokeDownPo.setDeviceId(dev_eui);
				mSmokeDownPo.setResult(result);
				mSmokeDownPo.setSendData(msg);
				mISmokeDataService.insertAlarmDown(mSmokeDownPo);
				log.info("task: lora into send log devEUI="+dev_eui);
				
				try {
					Thread.sleep(5000);//下发大于0.1秒
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		//给小组成员下发
		/*List<SmokeDevicePo> list = mISmokeDataService.selectAlarmList(null, team_type);
		if(list !=null && list.size()>0) {
			int size = list.size();
			for(int i =0;i<size;i++) {
				SmokeDevicePo mSmokeDevicePo = list.get(i);
				String devEUI = mSmokeDevicePo.getDeviceId();
				log.info("task: lora sendCmdFireAlarmRemove devEUI="+devEUI);
				if(!dev_eui.equals(devEUI)) {
					LoraSendCmds mLoraSendCmds = new LoraSendCmds();
					String result = mLoraSendCmds.sendMsg(deviceType,msg,devEUI,request_id);
					
					SmokeDownPo mSmokeDownPo = new SmokeDownPo();
					mSmokeDownPo.setDeviceId(devEUI);
					mSmokeDownPo.setResult(result);
					mSmokeDownPo.setSendData(msg);
					mISmokeDataService.insertAlarmDown(mSmokeDownPo);
					log.info("task: lora into send log devEUI="+devEUI);
				}
				try {
					Thread.sleep(200);//下发大于0.1秒
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}*/
	}
	/**
	 * 保存烟感数据
	 * @param map
	 */
	private void saveSmokeData(Map<String ,String> map) {
		if(mISmokeDataService == null) {
			mISmokeDataService =  SpringContextUtils.getBean(ISmokeDataService.class);
		}
		SmokeDataPo mSmokeDataPo = new SmokeDataPo();
		mSmokeDataPo.setDevId(map.get("dev_id"));
		mSmokeDataPo.setOrigValue(map.get("ori_value"));
		mSmokeDataPo.setSource(map.get("source"));
		mSmokeDataPo.setTime(Long.parseLong(map.get("time")));
		mSmokeDataPo.setDataStatus(map.get("dataStatus_type"));
		mISmokeDataService.insert(mSmokeDataPo);
		log.info("task: lora smoke insert into SQL end!");
	}
	
	/**
	 * 2G电表数据接收解析 mqtt方式接收
	 */
	private void MqttSmokeAnalysis() {
		log.info("============= GPRS Date start analysis=============");
		//:{"time_s":"2020-11-06 14:29:47.545528372","devEUI":"000000000000a0a7","data":"\\x303230313030"}
		try {
			com.alibaba.fastjson.JSONObject jo = JSON.parseObject(data);
			if (jo.containsKey("devEUI") && jo.containsKey("data")) {
//				LOGGER.info("lora data =====================" );
				String dev_eui = jo.getString("devEUI");
				
				//TODO 此处演示用，如果设备在列表中继续解析，不在的话不处理
				String team_type = "smoke_sensor_mqtt";
//				if(!dev_eui.contains(MqttConfig.SMOKE_LIST)) {
//					log.error("dev_eui=" + dev_eui + "不在测试列表中！");
//					return;
//				}
				if(!MqttConfig.SMOKE_LIST.contains(dev_eui)) {
					System.out.println(dev_eui+"不在测试列表中！");
					return;
				}
				String ori_value = jo.getString("data");
				String time_s = DateUtil.shortenTimeStr(jo.getString("time_s"));//2019-04-09 08:24:09.120570725
				long time = DateUtil.date2TimeStampLong(time_s, "yyyy-MM-dd HH:mm:ss");
				
				log.info("lora smoke sensor LoraAnalysis:dev_eui=" + dev_eui );
				log.info("lora smoke sensor LoraAnalysis:data=" + ori_value );
				log.info("lora smoke sensor LoraAnalysis:time_s=" + time_s );
				int len = ori_value.length();
		    	String orig_data = ori_value.substring(2, len).trim().replaceAll(" ", "");//去掉\x
		    	
		        String regex="^[A-Fa-f0-9]+$";//是16进制数
		    	if(orig_data.matches(regex)){
		    		//\x303230453030
		    		String dataFlag = orig_data.substring(0, 6);
		    		String dataStatus = orig_data.substring(6, 8);
		    		String dataEnd = orig_data.substring(8, 12);
		    		String dataStatus_type = "";
		    		log.info("task: lora smoke sensor receive: dataFlag:"+dataFlag);
		    		String orig_code = orig_data;
		    		if ("303230".equals(dataFlag) && "3030".equals(dataEnd)) {//水表上报发出的是68
			    		//System.out.println("AnalysisData15: 80----------------");
		    			log.info("task: lora smoke sensor dataStatus="+dataStatus);
	    				switch(dataStatus) {
	        			case "30"://正常
	        				dataStatus_type = "正常";
	        				break;
	        			case "31"://火警
	        				dataStatus_type = "火警";
	        				//火警时发下行
	        				sendCmdToSmoke(data_type,dev_eui,1,team_type);
	        				break;
	        			case "33"://低电
	        				dataStatus_type = "低电";
	        				break;
	        			case "34"://火警消除
	        				dataStatus_type = "火警消除";
	        				//火警消除时发下行
	        				sendCmdToSmoke(data_type,dev_eui,4,team_type);
	        				break;
	        			case "36"://低电恢复
	        				dataStatus_type = "低电恢复";
	        				break;
	        			case "44"://按键自测
	        				dataStatus_type = "按键自测";
	        				break;
	        			case "45"://探测器上电
	        				dataStatus_type = "探测器上电";
	        				break;
	    				default:
	    					break;
	    				}
	    				
	    				log.info("task: lora smoke sensor dataStatus_type="+dataStatus_type);
	    				Map<String ,String> dataMap = new HashMap<String ,String>();
	    				dataMap.put("dev_id", dev_eui);
	    				dataMap.put("time", String.valueOf(time));
	    				dataMap.put("ori_value", ori_value);
	    				dataMap.put("source", "lora_smoke");
	    				dataMap.put("dataStatus_type", dataStatus_type);
	    				saveSmokeData(dataMap);
		    		}
		    	}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
    /**
	*宽腾-沪喻电表数据解析
     * @param orig_data
     * @param deviceId
     * @param rssi
     */
    private void SaveJbqataToDB(String orig_data, String deviceId, String rssi) {
        String subNum = orig_data.substring(2, 14);
        // 表号高低位互换
        byte[] data_byte = Utilty.hexStringToBytes(subNum);
        String eNum = Utilty.convertByteToString(data_byte, 1, data_byte.length);
        if (orig_data != null && deviceId != null) {
            // 数据解码
            Map<String, String> resultMap = EmDataNBUtil.getDataValue(orig_data);

            if (resultMap.size() > 0) {
                ktEmDataPo ktEmDataPo = new ktEmDataPo();
                ktEmDataPo.seteNum(eNum);
                ktEmDataPo.setDevId(deviceId);
                ktEmDataPo.setOrigValue(orig_data);
                // 信号
                ktEmDataPo.seteSignal(rssi);
                // 设备资源类型
                ktEmDataPo.seteSource("kt_nb_em");
                // 上报时间
                ktEmDataPo.setTime(System.currentTimeMillis());
                // 有功总电
                ktEmDataPo.seteKwAll(resultMap.get("combinationPower") != null ? resultMap.get("combinationPower") : "");
                // 正向有功总电
                ktEmDataPo.seteKw1(resultMap.get("forwardPower") != null ? resultMap.get("forwardPower") : "");
                // 反向有功总电
                ktEmDataPo.seteKw2(resultMap.get("reversePower") != null ? resultMap.get("reversePower") : "");
                // 电流
                ktEmDataPo.seteCurrent(resultMap.get("current") != null ? resultMap.get("current") : "");
                // 电压
                ktEmDataPo.seteVoltage(resultMap.get("voltage") != null ? resultMap.get("voltage") : "");
                // 有功功率
                ktEmDataPo.setEkw(resultMap.get("activePower") != null ? resultMap.get("activePower") : "");
                // 功率因数
                ktEmDataPo.seteFactorAll(resultMap.get("powerFactor") != null ? resultMap.get("powerFactor") : "");
                // 断电或通电
                ktEmDataPo.seteSwitch(resultMap.get("switch") != null ? resultMap.get("switch") : "");
                // 状态位
                ktEmDataPo.seteStatus(resultMap.get("status") != null ? resultMap.get("status") : "");
                // 序列号
                ktEmDataPo.seteSeq(Integer.parseInt(resultMap.get("seq")));
                if (ktDataService == null) {
                    ktDataService = SpringContextUtils.getBean(KTDataService.class);
                }
                ktDataService.insertKtData(ktEmDataPo);
            }
        }
    }


    private void SaveCmdData(String orig_data, String deviceId) {
        Map<String, String> resultMap = EmDataNBUtil.getDocding(orig_data);
        if (resultMap.size() > 0) {
            KTREMReadPo ktremReadPo = new KTREMReadPo();
            ktremReadPo.setDevId(deviceId);
            ktremReadPo.setTime(System.currentTimeMillis());
            ktremReadPo.seteOrigValue(orig_data);
            ktremReadPo.seteNum(resultMap.get("eNum") != null ? resultMap.get("eNum") : "");
            ktremReadPo.seteReadValue(resultMap.get("status") != null ? resultMap.get("status") : "");
            ktremReadPo.seteSource("kt_nb_em");
            // 保存数据
            if (ktDataService == null) {
                ktDataService = SpringContextUtils.getBean(KTDataService.class);
            }
            ktDataService.insertCmdReadData(ktremReadPo);
        }


    }

    private void SaveCmdStauData(String orig_data, String deviceId, String rssi) {
        Map<String, String> resultMap = EmDataNBUtil.getCmdStauDocding(orig_data);
        if (resultMap != null && resultMap.size() > 0) {
            KTREMReadPo ktremReadPo = new KTREMReadPo();
            ktremReadPo.setDevId(deviceId);
            ktremReadPo.setTime(System.currentTimeMillis());
            ktremReadPo.seteOrigValue(orig_data);
            ktremReadPo.seteNum(resultMap.get("eNum") != null ? resultMap.get("eNum") : "");
            ktremReadPo.seteReadValue(resultMap.get("status") != null ? resultMap.get("status") : "");
            ktremReadPo.seteSource("kt_nb_em");
            // 保存数据
            if (ktDataService == null) {
                ktDataService = SpringContextUtils.getBean(KTDataService.class);
            }
            ktDataService.insertCmdReadData(ktremReadPo);


        }
    }
	
	  /**
     * 安科瑞数据上报解析
     */
    private void AKRNBEMetersingl() throws JSONException {
        JSONObject jsonObject = JSON.parseObject(data);
        int type = (int) jsonObject.getJSONObject("msg").get("type");
        if (type == 1) {
            JSONObject jsonMsg = jsonObject.getJSONObject("msg");
            String valueData = jsonMsg.getString("value").toLowerCase();
            String ori_value = valueData.substring(0, valueData.length()).trim().replaceAll(" ", "");
            String dev_id = jsonMsg.getLong("dev_id").toString();
            String imei = jsonMsg.getString("imei");
            long time = jsonMsg.getLong("at");
            int len = ori_value.length();
            String startbyte = ori_value.substring(0, 4).toLowerCase();
            String endbyte = ori_value.substring(len - 4, len).toLowerCase();
            if ("7b7b".equals(startbyte) && "7d7d".equals(endbyte)) {
                String dataType = ori_value.substring(4, 6);
                // 表号
                String eNumSubData = Utilty.hexStringToString(ori_value.substring(6, 46));
                String e_num = eNumSubData.substring(0, 14);
                String orig_code = "";
                // 心跳包数据包暂不处理
                if (("ab").equals(dataType) || ("AB").equals(dataType)) {
                    return;
                } else {
                    orig_code = ori_value.substring(46, len - 8);
                }
                switch (dataType) {
                    case "ab":// 心跳包
                        break;
                    case "91":// 数据上传
                        // 根据))]]分割
                        String[] origArr = orig_code.split("29295d5d");
                        Map<String, Object> d_result = new HashMap<String, Object>();
                        for (int i = 0; i < origArr.length; ++i) {
                            String dataStr = origArr[i];
                            // 截取[[1-1((
                            String sub = dataStr.substring(20, dataStr.length());
                            // 多段数据
                            d_result = EMDataAnalysisUtil.getAKRSingleDataValue(d_result, i, sub);
                        }
                        d_result.put("imei", imei);
                        d_result.put("dev_id", dev_id);
                        d_result.put("time", time);
                        d_result.put("type", type);
                        d_result.put("ori_value", ori_value);
                        d_result.put("source", data_type);
                        d_result.put("e_num", e_num);
                        d_result.put("dataType", dataType);
                        SaveAKREMDataToSingleDB(d_result);
                        break;
                    case "90":
                        // 拉合闸状态   查询状态响应数据
                        // 7B7B9044445359313335324E423030303100000000000001030 4 00010000 ABF33A2A 7D7D
                        // 7B7B9044445359313335324E423030303100000000000001030 4 00010000 ABF33A2A 7D7D
						// 7B7B9044445359313335324E423030303100000000000001030 2 000A 3843 A 7277D7D
                        log.info("task: akr read------ 90");
                        if (ori_value.length() == 72 || ori_value.length()==68) {
                            String dataValue = orig_code.substring(6, orig_code.length() - 4);
                            Map<String, Object> resultStatus = EMDataAnalysisUtil.getAKRSingleReadValue(dataValue);
                            if (resultStatus != null && resultStatus.size() > 0) {
                                String readValue = (String) resultStatus.get("value");
                                log.info("拉合闸状态==============" + readValue);
                                if (mIAKREMDataService == null) {
                                    mIAKREMDataService = SpringContextUtils.getBean(IAKREMDataService.class);
                                }
                                AKREMReadPo mAKREMReadPo = new AKREMReadPo();
                                mAKREMReadPo.setDevId(dev_id);
                                mAKREMReadPo.setImei(imei);
                                mAKREMReadPo.setOrigValue(ori_value);
                                mAKREMReadPo.setSource(data_type);
                                mAKREMReadPo.setTime(System.currentTimeMillis());
                                mAKREMReadPo.setENum(e_num);
                                mAKREMReadPo.setDataSeq("");
                                mAKREMReadPo.setReadValue(readValue);
                                // 保存安科瑞指令下发响应数据
                                int num = mIAKREMDataService.insertReadData(mAKREMReadPo);
                                if (num > 0) {
                                    log.info("数据保存成功 " + data_type);
                                } else {
                                    log.info("数据保存失败 " + data_type);
                                }
                                log.info("task: akr read insert into SQL end!");
                            }
                        }

                        break;
                    case "A1":
                    case "a1"://告警
                        Map<String, Object> resultwarning = new HashMap<String, Object>();
                        String[] resultwarns = orig_code.split("29295d5d");// 根据))]]分割
                        for (int i = 0; i < resultwarns.length; ++i) {
                            String dataStr = resultwarns[i];
                            String sub = dataStr.substring(14, dataStr.length());//截取[[1-1((
                            long dataLen = Long.parseLong(sub.substring(4, 6), 16);
                            String dataValue_alt = sub.substring(6, sub.length());
                            resultwarning = EMDataAnalysisUtil.getAKRSingleEventDataValue(resultwarning, i, dataValue_alt);//多段数据
                        }
                        resultwarning.put("imei", imei);
                        resultwarning.put("dev_id", imei);
                        resultwarning.put("time", time);
                        resultwarning.put("type", type);
                        resultwarning.put("ori_value", ori_value);
                        resultwarning.put("source", data_type);
                        resultwarning.put("e_num", e_num);
                        resultwarning.put("dataType", dataType);
						SaveAKREMEventDataToDB(resultwarning);
						log.info("task: akr nb event insert into db !");
                        break;
                    default:
                        break;
                }
            }

        }
    }

    /**
     * 保存安科瑞数据上报
     *
     * @param d_result
     */
    private void SaveAKREMDataToSingleDB(Map<String, Object> d_result) {
        if (mIAKREMDataService == null) {
            mIAKREMDataService = SpringContextUtils.getBean(IAKREMDataService.class);
        }
        if (d_result != null && d_result.size() > 0) {
            AKREMDataPo dataPo = new AKREMDataPo();
            dataPo.setDevId((String) d_result.get("dev_id"));
            dataPo.setOrigValue((String) d_result.get("ori_value"));
            dataPo.setTime((long) d_result.get("time"));
            dataPo.setSource((String) d_result.get("source"));
            dataPo.setENum(d_result.containsKey("e_num") ? (String) d_result.get("e_num") : "");
            dataPo.setDataType((String) d_result.get("dataType"));
            dataPo.setESeq(d_result.containsKey("e_seq") ? (long) d_result.get("e_seq") : 0);
            dataPo.setESignal(d_result.containsKey("e_signal") ? (String) d_result.get("e_signal") : "");
            // 有功总电能
            dataPo.setEKwh1(d_result.containsKey("e_kwh1") ? (String) d_result.get("e_kwh1") : "");
            dataPo.setEKwh2(d_result.containsKey("e_kwh2") ? (String) d_result.get("e_kwh2") : "");
            // 总有功功率
            dataPo.setEKw1All(d_result.containsKey("e_kw1_all") ? (String) d_result.get("e_kw1_all") : "");
            // 电压
            dataPo.setEVoltageA(d_result.containsKey("e_voltage_a") ? (String) d_result.get("e_voltage_a") : "");
            // 电流
            dataPo.setECurrentA(d_result.containsKey("e_current_a") ? (String) d_result.get("e_current_a") : "");
            // 总功率因数
            dataPo.setEFactorAll(d_result.containsKey("e_factor_all") ? (String) d_result.get("e_factor_all") : "");
            // 状态字1
            dataPo.setEStatu1(d_result.containsKey("e_statu1") ? (String) d_result.get("e_statu1") : "");
            // 状态字2
            dataPo.setEStatu2(d_result.containsKey("e_statu2") ? (String) d_result.get("e_statu2") : "");
            // 继电器状态
            dataPo.setESwitch(d_result.containsKey("e_switch") ? (String) d_result.get("e_switch") : "");
            int num = mIAKREMDataService.addOne(dataPo);
            if (num > 0) {
                log.info("数据保存成功" + num);
            } else {
                log.info("数据保存失败" + num);
            }
        }
    }

    /**
     * 宽腾单相电表数据接收解析
     */
    private void KTNBEMeterAnalysis() throws JSONException {
        JSONObject jsonObject = JSON.parseObject(data);
        String payload = (String) jsonObject.getJSONObject("service").getJSONObject("data").get("payload");
        if (Constant.DEVICE_INFO_CHANGED.equals(jsonObject.getString("notifyType"))) {
            // 暂不处理
        }
        // 数据正常上报
        if (Constant.DEVICE_DATA_CHANGED.equals(jsonObject.getString("notifyType"))) {
            String rssi = jsonObject.getJSONObject("service").getJSONObject("data").get("RSSI").toString();
            String deviceId = jsonObject.getString("deviceId");
            int len = payload.length();
            String regex = "^[A-Fa-f0-9]+$";
            //是16进制
            if (payload.matches(regex)) {
                String dataFlag = payload.substring(0, 8);
                if ("FEFEFEFE".equals(dataFlag)) {
                    String orig_data = payload.substring(8, payload.length());
                    byte[] binaryData = Utilty.hexStringToBytes(orig_data);
                    len = binaryData.length;
                    // 以16开头
                    String startbyte = Integer.toHexString(binaryData[0] & 0xFF).toUpperCase();
                    // 数据正常上报92长度
                    if (payload.length() == 92) {
                        String endbyte = Integer.toHexString(binaryData[len - 2] & 0xFF);
                        if ("68".equals(startbyte) && "16".equals(endbyte)) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("deviceId", deviceId);
                            SaveJbqataToDB(orig_data, deviceId, rssi);
                        }
                    }

                    // 拉合闸响应数据 32长度
                    if (payload.length() == 32) {
                        String cmdEndbyte = Integer.toHexString(binaryData[len - 1] & 0xFF);
                        if ("68".equals(startbyte) && "16".equals(cmdEndbyte)) {
                            SaveCmdData(orig_data, deviceId);
                        }
                    }

                    // 拉合闸状态查询响应数据 44长度
                    if (payload.length() == 44) {
                        String statusEndbyte = Integer.toHexString(binaryData[len - 1] & 0xFF);
                        if ("68".equals(startbyte) && "16".equals(statusEndbyte)) {
                            SaveCmdStauData(orig_data, deviceId, rssi);
                        }
                    }
                }
            }
        }


    }
}
