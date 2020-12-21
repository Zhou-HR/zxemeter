package com.gdiot.ssm.cmds;

import com.gdiot.model.EMCmdsSEQPo;
import com.gdiot.service.INBYDEMCmdsService;
import com.gdiot.service.IXBEMDataService;
import com.gdiot.ssm.util.CRC16;
import com.gdiot.ssm.util.SpringContextUtils;
import com.gdiot.ssm.util.Utilty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoraSendCmdsUtils {
	private final static Logger logger = LoggerFactory.getLogger(LoraSendCmdsUtils.class);
	private INBYDEMCmdsService mINBYDEMCmdsService; 
	private IXBEMDataService mIXBEMDataService;
	
	public static String getTimeContent(String dev_eui,String e_num,String e_fac) {
		String id = "FEFEFEFE00";//服务序号
		String prm = "01";//启动方向    01抄表，02设置，03 拉合闸
		String e_num2 = e_num;//"190300000064";//表号
		String fac_id = e_fac;//厂商标识
		String dih_lon = "7006";//FF03103141 请求数据类型 长度 数据  类型包含 
		String CRC = "";
		String end = "16";
		
		//time
		long datel = System.currentTimeMillis();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss");
        Date date = new Date(datel);
        String time = simpleDateFormat.format(date);
//        System.out.println("time="+ time);
        byte[] e_time = Utilty.hexStringToBytes(time);
        String time16 = Utilty.convertByteToString(e_time,1,e_time.length);
//        logger.info("time16="+time16);
        dih_lon = dih_lon + time16;
        
		//将表号高低位互换
		byte[] e = Utilty.hexStringToBytes(e_num2);
//				System.out.println("length="+e.length);
		String enum16 = Utilty.convertByteToString(e,1,e.length);
//				System.out.println("enum16="+enum16);
//				System.out.println("d_str="+ id+ prm +e_num2 + fac_id +dih_lon);
		CRC = CRC16.getCRC(id+ prm +enum16 + fac_id +dih_lon).toUpperCase();
//				System.out.println("CRC="+CRC);
		String contents= id+ prm + enum16 + fac_id +dih_lon+CRC +end;
//				System.out.println("contents="+contents);
		return contents;
	}
	public static String getGroupTimeContent(String groupid) {
		String id = "FEFEFEFE00";//服务序号
		String prm = "01";//启动方向    01抄表，02设置，03 拉合闸
		String e_num2 = "000000000000";//"190300000064";//表号
		String fac_id = "00";//厂商标识
		String dih_lon = "7006";//FF03103141 请求数据类型 长度 数据  类型包含 
		String CRC = "";
		String end = "16";
		
		//time
		long datel = System.currentTimeMillis();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss");
        Date date = new Date(datel);
        String time = simpleDateFormat.format(date);
//        System.out.println("time="+ time);
        byte[] e_time = Utilty.hexStringToBytes(time);
        String time16 = Utilty.convertByteToString(e_time,1,e_time.length);
        System.out.println("time16="+time16);
        dih_lon = dih_lon + time16;
        
		//将表号高低位互换
		byte[] e = Utilty.hexStringToBytes(e_num2);
//				System.out.println("length="+e.length);
		String enum16 = Utilty.convertByteToString(e,1,e.length);
//				System.out.println("enum16="+enum16);
//				System.out.println("d_str="+ id+ prm +e_num2 + fac_id +dih_lon);
		CRC = CRC16.getCRC(id+ prm +enum16 + fac_id +dih_lon).toUpperCase();
//				System.out.println("CRC="+CRC);
		String contents= id+ prm + enum16 + fac_id +dih_lon+CRC +end;
//				System.out.println("contents="+contents);
		return contents;
	}
    
	public Map<String ,Object> getCmdsInfo(Map<String,String> msgMap){
		String module_type = msgMap.get("module_type");
		String dev_eui = msgMap.get("dev_eui");
		String type = msgMap.get("type");
		String value = msgMap.get("value");
		String operate_type = msgMap.get("operate_type");
		String e_num = msgMap.get("e_num");
		String fac_id = msgMap.get("e_fac") != null ? msgMap.get("e_fac") : "01";
		String new_seq_hex = msgMap.get("new_seq_hex");
		
		logger.info("----------lora----getCmdsInfo---dev_eui=="+dev_eui);
		Map<String, Object> map = new HashMap<String, Object>();
		String regex ="[a-f0-9A-F]{16}";// 16位DEV_EUI:"[a-f0-9A-F]{15}";   15位数字IMEI:regex ="^\\d{15}$""
		if(!dev_eui.matches(regex)) {
			logger.error("dev_eui error");
			return null;
		}
		if(e_num != null){//emInfo != null
	        //cmd data
			logger.info("-----------lora---getCmdsInfo---new_seq_hex=="+ new_seq_hex);
			if(new_seq_hex != "") {
//				logger.info("--------------getCmdsInfo---new_seq_hex=="+ new_seq_hex);
				String content = getCmdContent(new_seq_hex,e_num,fac_id,type,operate_type,value);
				logger.info("------------lora--getCmdsInfo---content=="+ content);
				
		        map.put("content", content);
//		        map.put("time", emInfo.get("time"));
		        map.put("seq_hex", new_seq_hex);
		        
		        EMCmdsSEQPo mNBYDEMCmdsPo = new EMCmdsSEQPo();
		        mNBYDEMCmdsPo.setE_num(e_num);
		        mNBYDEMCmdsPo.setImei(dev_eui);
		        mNBYDEMCmdsPo.setCmd_seq(new_seq_hex);
		        mNBYDEMCmdsPo.setData_seq(getDataSeq(new_seq_hex).toUpperCase());
		        mNBYDEMCmdsPo.setCreate_time(new Date(System.currentTimeMillis()));
		        updateDBCmdsSeq(mNBYDEMCmdsPo); 
		        return map;
			}
		}
		return map;
	}
	
	private String getCmdContent(String ser_id,String eNum,String fac,String data_type,String operate_type, String seq) {
		String lora_start = "FEFEFEFE";
		String id = ser_id;//服务序号
		String prm = "01";//启动方向    01抄表，02设置，03 拉合闸
		String e_num2 = eNum;//"190300000064";//表号
		String fac_id = fac;//厂商标识
		String dih_lon = "";//FF03103141 请求数据类型 长度 数据  类型包含 
		String CRC = "";
		String end = "16";
		if("R".equals(operate_type)) {//读
			prm = "01";
		}else if("W".equals(operate_type)) {//写
			prm = "02";
		}else if("O".equals(operate_type)) {//执行，操作
			prm = "03";
		}else if("D".equals(operate_type)) {//读冻结数据
			prm = "0D";
		}else if("E".equals(operate_type)) {//写多项
			prm = "0E";
		}else if("F".equals(operate_type)) {//写多项
			prm = "0F";
		}
		switch(data_type) {
		//事件次数
		case "B0"://停电事件总次数 //C00147000000031900B00203005916
		case "power_cut_times":
			dih_lon = "B000";//
			break;
		case "B2"://拉合闸总次数 //C00147000000031900B20209005116
		case "switch_times":
			dih_lon = "B200";//
			break;
		case "B4"://过流总数 //C00147000000031900B40200005816
		case "over_current_times":
			dih_lon = "B400";
			break;
		case "B6"://过压事件总数 //C00147000000031900B60200005616
		case "over_voltage_times":
			dih_lon = "B600";
			break;
		case "B8"://欠压事件次数 //C00147000000031900B80200005416
		case "under_voltage_times":
			dih_lon = "B800";
			break;
		case "BA"://模块重启次数 //C00147000000031900BA0200005216
		case "reboot_times":
			dih_lon = "BA00";
			break;
		case "BC"://编程记录次数
			dih_lon = "BC00";
			break;
		case "A0"://整点冻结次数 //C00147000000031900A0032802004116
		case "data_times":
			dih_lon = "A000";
			break;
		case "A2"://日冻结次数 //C00147000000031900A20203006716
		case "day_data_times":
			dih_lon = "A200";
			break;
		case "A4" :
		case "month_data_times"://月冻结次数 //C00147000000031900A401016816
			dih_lon = "A400";
			break;
		//抄表
		case "F5"://IMSI ASCII //C00147000000031900F50F3436303034353539333130323938310116
		case "imsi":
			dih_lon = "F500";//
			break;
		case "F6"://IMEI  ASCII //C00147000000031900F60F383636393731303330333839333337F016
		case "imei":
			dih_lon = "F600";//
			break;
		case "F7"://ICCID ASCII //C00147000000031900F7143839383630343335313031383930303532393830ED16
		case "iccid":
			dih_lon = "F700";//
			break;
		case "F8"://信号强度 //C00147000000031900F80124F116
		case "e_signal":
			dih_lon = "F800";//
			break;
		case "F9"://驻网状态
			dih_lon = "F900";
			break;
		case "FA"://模块监控状态字
			dih_lon = "FA00";
			break;
		case "10"://有功总电能 //C0014700000003190010050000000000F916
		case "e_power":
			dih_lon = "1000";
			break;
		case "11"://A相有功电能 //C0014700000003190010050000000000F916
			dih_lon = "1100";
			break;
		case "12"://B相有功电能 //C0014700000003190010050000000000F916
			dih_lon = "1200";
			break;
		case "13"://C相有功电能 //C0014700000003190010050000000000F916
			dih_lon = "1300";
			break;
		case "20"://总有功功率
			dih_lon = "2000";//
			break;
		case "21"://A相有功功率
			dih_lon = "2100";//
			break;
		case "22"://B相有功功率
			dih_lon = "2200";//
			break;
		case "23"://C相有功功率
			dih_lon = "2300";//
			break;
		case "31"://A相电压 //C00147000000031900310258226116
		case "e_voltage":
			dih_lon = "3100";//
			break;
		case "32"://B相电压 //C00147000000031900310258226116
			dih_lon = "3200";//
			break;
		case "33"://C相电压 //C00147000000031900310258226116
			dih_lon = "3300";//
			break;
		case "41"://A相电流 //C001470000000319004103070000C316
		case "e_current":
			dih_lon = "4100";//
			break;
		case "42"://B相电流 //
			dih_lon = "4200";//
			break;
		case "43"://C相电流 //
			dih_lon = "4300";//
			break;
		case "50"://总功率因数
			dih_lon = "5000";//
			break;
		case "60"://频率
			dih_lon = "6000";//
			break;
		case "70"://时间
			dih_lon = "7000";//
			break;
		case "71"://注册标识
			dih_lon = "7100";//
			break;
		case "72"://上报时间
		case "set_time":
			if("W".equals(operate_type)) {//写//seq>=360 && seq<86400
				if((Long.parseLong(seq) >= 360) && (Long.parseLong(seq) < 86400)) {
					dih_lon = "7203"+getTimesHex(seq,3);
				}
				/*if("360".equals(seq)) {//6分钟  360 
					dih_lon = "7203600300";//
				}else if("1800".equals(seq)) {//30分钟 1800 
					dih_lon = "7203001800";//
				}else if("3600".equals(seq)) {//1小时 3600
					dih_lon = "7203003600";//
				}else if("7200".equals(seq)) {//2小时 7200
					dih_lon = "7203003600";//
				}else if("43200".equals(seq)) {//12小时 43200
					dih_lon = "7203003204";//
				}*/
			}else if("R".equals(operate_type)){//读上报时间
				dih_lon = "7200";//
			}
			break;
		case "73"://认证时长
			dih_lon = "7300";//
			break;
		case "74"://心跳间隔
			dih_lon = "7400";//
			break;
		case "80"://电表运行状态字1
			dih_lon = "8000";//
			break;
		case "81"://电表运行状态字2
			dih_lon = "8100";//
			break;
		case "82"://继电器控制字
			if("O".equals(operate_type)) {//执行，操作
				if("on".equals(seq)) {
					dih_lon = "8201AA";//
				}else if("off".equals(seq)) {
					dih_lon = "820155";//
				}
			}else {
				dih_lon = "8200";//读继电器值
			}
			break;
		case "83"://电表常数
			dih_lon = "8300";//
			break;
		case "90"://厂家标识
			dih_lon = "9000";//
			break;
		case "91"://硬件版本号
			dih_lon = "9100";//
			break;
		case "92"://软件版本号
			dih_lon = "9200";//
			break;
		case "93"://协议版本号
			dih_lon = "9300";//
			break;
		case "94"://表号
			dih_lon = "9400";//
			break;
		case "95"://通讯号
			dih_lon = "9500";//
			break;
		case "96"://用户号
			dih_lon = "9600";//
			break;
		case "FF"://预留，表头没有做
			dih_lon = "FF06112131415170";
			break;
		case "version"://预留，表头没有做
			dih_lon = "FF03919293";
			break;
		//事件数据
		case "A1"://整点冻结数据
		case "lost_data":
			if(Integer.parseInt(seq)>=0) {
				dih_lon = "A103"+ getTimesHex(seq,3);//
			}
			break;
		case "A3"://第几次日冻结数据
			if(Integer.parseInt(seq)>=0) {
				dih_lon = "A302" + getTimesHex(seq,2);//
			}
			break;
		case "A5"://第几次月冻结数据
			if(Integer.parseInt(seq)>=0) {
				dih_lon = "A502" + getTimesHex(seq,2);//
			}
			break;
		case "B1"://停电事件
			if(Integer.parseInt(seq)>=0) {
				dih_lon = "B102" + getTimesHex(seq,2);//
			}
			break;
		case "B3"://拉合闸事件 //C0034700000003190082008A16
			if(Integer.parseInt(seq)>=0) {
				dih_lon = "B302" + getTimesHex(seq,2);//
			}
			break;
		case "B5"://过流事件
			if(Integer.parseInt(seq)>=0) {
				dih_lon = "B502" + getTimesHex(seq,2);//
			}
			break;
		case "B7"://过压事件
			if(Integer.parseInt(seq)>=0) {
				dih_lon = "B702" + getTimesHex(seq,2);//
			}
			break;
		case "B9"://欠压事件
			if(!"".equals(seq)) {
				dih_lon = "B902" + getTimesHex(seq,2);//
			}
			break;
		case "BB"://重启记录最近十次
			if(Integer.parseInt(seq)>=0) {
				dih_lon = "BB02" + getTimesHex(seq,2);//
			}
			break;
		case "BD"://编程记录十次
			if(Integer.parseInt(seq)>=0) {
				dih_lon = "BD02" + getTimesHex(seq,2);//
			}
			break;
		case "F0":
			if("O".equals(operate_type)) {
				if("on".equals(seq)) {//进工厂模式
					dih_lon = "F001aa";
				}else if("off".equals(seq)) {//退工厂模式
					dih_lon = "F00155";
				}
			}
			break;
		default:
			return "";
		}
		
		//将表号高低位互换
		byte[] e = Utilty.hexStringToBytes(e_num2);
//		System.out.println("length="+e.length);
		String enum16 = Utilty.convertByteToString(e,1,e.length);
//		System.out.println("enum16="+enum16);
//		System.out.println("d_str="+ id+ prm +e_num2 + fac_id +dih_lon);
		CRC = CRC16.getCRC(id+ prm +enum16 + fac_id +dih_lon).toUpperCase();
//		System.out.println("CRC="+CRC);
		String contents= lora_start + id + prm + enum16 + fac_id +dih_lon+CRC +end;
//		System.out.println("contents="+contents);
		return contents;
	}
	
	private String getTimesHex(String times,int len) {
		//序列号高位补0
		DecimalFormat df = null;
		if(len == 2) {
			df=new DecimalFormat("0000");
		}else if(len == 3) {
			df=new DecimalFormat("000000");
		}
		String seq_s=df.format(Integer.parseInt(times));
//		System.out.println(seq_s);
		//将序列号高低位互换
		byte[] seq_b = Utilty.hexStringToBytes(seq_s);
		String seq16 = Utilty.convertByteToString(seq_b,1,seq_b.length);
		System.out.println("seq16="+seq16);
		return seq16;
	}
	
	public String getNewCmdSeq(String e_num,String imei) {
//		logger.info("从数据库读序列号-------------------");
		String seq_hex = getCmdsSeqByEnum(e_num,imei);
		if(seq_hex.isEmpty()) {
			return "";
		}
		String seq_hex_new = "";
		//服务序号
		String regex="^[A-Fa-f0-9]+$";//是16进制数
    	if(seq_hex.matches(regex)){
    		//十进制：64--127
			//十六进制：40--7F
			int seq_dec = Integer.parseInt(seq_hex,16);
			if(seq_dec==127) {
				seq_dec = 64;//新一轮循环开始
			}else if(seq_dec>=64 && seq_dec<127) {
				seq_dec++;
			}
			seq_hex_new = Integer.toHexString(seq_dec).toUpperCase();
			logger.info("计算好的新的下行序列号-----------seq_hex_new="+seq_hex_new);
		}
    	return seq_hex_new;
	}
	private String getDataSeq(String cmd_seq) {
		if(cmd_seq.isEmpty()) {
			return "";
		}
		String data_seq_hex = "";
		//服务序号
		String regex="^[A-Fa-f0-9]+$";//是16进制数
    	if(cmd_seq.matches(regex)){
    		//十进制：64--127
			//十六进制：40--7F
			int seq_dec = Integer.parseInt(cmd_seq,16);
			seq_dec = seq_dec + 128;
			data_seq_hex = Integer.toHexString(seq_dec);
//			System.out.println("计算好的新的上报序列号-----------data_seq_hex="+data_seq_hex);
		}
    	return data_seq_hex;
	}
	
	private int updateDBCmdsSeq(EMCmdsSEQPo mNBYDEMCmdsPo) {
//		logger.info("从更新数据库序列号-------------------");
		if(mINBYDEMCmdsService == null) {
			mINBYDEMCmdsService =  SpringContextUtils.getBean(INBYDEMCmdsService.class);
		}
		int result = mINBYDEMCmdsService.updatecmdseq(mNBYDEMCmdsPo);
		logger.info("update dev_eui cmds em_cmds_seq-----result=" + result);
		return result;
	}
	private String getCmdsSeqByEnum(String e_num,String dev_eui) {
//		logger.info("根据表号查询序列号-------------------");
		if(mINBYDEMCmdsService == null) {
			mINBYDEMCmdsService =  SpringContextUtils.getBean(INBYDEMCmdsService.class);
		}
		List<EMCmdsSEQPo> list = mINBYDEMCmdsService.selectcmdseq(dev_eui);
		if(list.size() > 0) {//数据库中存在,读出数据
//			logger.info("根据表号查询序列号------------------list.size()="+list.size());
//			logger.info("根据表号查询序列号------------------enum="+list.get(0).getE_num());
//			logger.info("根据表号查询序列号------------------imei="+list.get(0).getImei());
//			logger.info("根据表号查询序列号------------------cmd_seq="+list.get(0).getCmd_seq());
			String cmd_seq = list.get(0).getCmd_seq();
			return cmd_seq;
		}else {//数据库中没有，就插入，从40开始
			EMCmdsSEQPo mNBYDEMCmdsPo = new EMCmdsSEQPo();
			mNBYDEMCmdsPo.setE_num(e_num);
			mNBYDEMCmdsPo.setImei(dev_eui);
			mNBYDEMCmdsPo.setCmd_seq("40");
	        mNBYDEMCmdsPo.setData_seq(getDataSeq("40").toUpperCase());
	        mNBYDEMCmdsPo.setCreate_time(new Date(System.currentTimeMillis()));
			int intodb = mINBYDEMCmdsService.insertcmdseq(mNBYDEMCmdsPo);
			logger.info("new dev_eui insert int em_cmds_seq-----intodb=" + intodb);
			return "40";
		}
	}
}
