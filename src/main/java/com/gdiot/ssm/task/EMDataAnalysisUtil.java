package com.gdiot.ssm.task;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gdiot.ssm.entity.NBDXDataPo;
import com.gdiot.ssm.util.Utilty;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EMDataAnalysisUtil {

	private static final Logger LOGGER =  LoggerFactory.getLogger(DataSenderTask.class);
	/**
	 * 用于解析电表上报数据，芯北电表协议 单相电表
	 * @param data3 需要解析的数据域
	 * @return
	 */
	public static Map<String ,String> getDataValue(String data3) {
		Map<String ,String> map = new HashMap<String ,String>();
		for(int i =0,j=data3.length() ;i<j;) {
//			System.out.print("---------i_str="+i);
			String typeStr = data3.substring(i, i+2);
			String lenStr = data3.substring(i+2,i+4);
//			LOGGER.info("---------typeStr="+typeStr);
//			LOGGER.info("---------lenStr="+lenStr);
			int length = Integer.valueOf(lenStr,16);
			String dataStr = data3.substring(i+4,i+4+length*2);
//			LOGGER.info("---------length="+length);
//			LOGGER.info("---------dataStr="+dataStr);
			
			byte[] data_byte = Utilty.hexStringToBytes(dataStr);
			String data = Utilty.convertByteToString(data_byte, 1, data_byte.length);
//			System.out.print("---------data_byte="+data_byte);
//			LOGGER.info("---------data="+data);
			switch(typeStr) {
			case "A0"://序列号 //整点冻结次数 
			case "A2"://日冻结次数 
			case "A4"://月冻结次数 //A4020400
				map.put("seq_type", String.valueOf(typeStr));
				if(Utilty.isNumeric(data)) {
					int e_seq = Integer.parseInt(data);
					LOGGER.info("data receive: 序列号:"+e_seq);
					map.put("e_seq", String.valueOf(e_seq));
				}
				break;
			case "70"://time
				if(Utilty.isNumeric(data)) {
					LOGGER.info("data receive: 时间:"+data);
					map.put("e_time", String.valueOf(data));
				}
				break;
			case "F8"://信号值
				if(Utilty.isNumeric(data)) {
					if(length == 1) {
						BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0").format(Double.valueOf(data)));
						LOGGER.info("data receive: 信号值:"+mBigDecimal);
						map.put("e_signal", mBigDecimal.toString());
					}else if(length == 2){//F8 02 32 32
						String signal = Utilty.hexStringToString(dataStr);
						LOGGER.info("data receive: 信号值:"+ signal);
						map.put("e_signal", signal);
					}
				}
				break;
			case "10"://有功总电量
				if(Utilty.isNumeric(data)) {
					BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(data) / 100));
					LOGGER.info("data receive: 有功总电量:"+mBigDecimal);
					map.put("e_kwh1",mBigDecimal.toString());
				}
				break;
			case "20"://有功总功率
				if(Utilty.isNumeric(data)) {
					BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.0000").format(Double.valueOf(data) / 10000));
					LOGGER.info("data receive: 总有功功率:"+mBigDecimal);
					map.put("e_kw1_all", mBigDecimal.toString());
				}
				break;
			case "24"://无功总功率
				if(Utilty.isNumeric(data)) {
					BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.0000").format(Double.valueOf(data) / 10000));
					LOGGER.info("data receive: 总无功功率:"+mBigDecimal);
					map.put("e_kw2_all", mBigDecimal.toString());
				}
				break;
			case "31"://电压
				if(Utilty.isNumeric(data)) {
					BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#00.0").format(Double.valueOf(data) / 10));
					LOGGER.info("data receive: 电压:"+mBigDecimal);
					map.put("e_voltage_a", mBigDecimal.toString());
				}
				break;
			case "41"://电流
				if(Utilty.isNumeric(data)) {
					BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(data) / 1000));
					LOGGER.info("data receive: 电流:"+mBigDecimal);
					map.put("e_current_a", mBigDecimal.toString());
				}
				break;
			case "50"://总功率因数
				if(Utilty.isNumeric(data)) {
					BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("0.000").format(Double.valueOf(data) / 1000));
					LOGGER.info("data receive: 总功率因数:"+mBigDecimal);
					map.put("e_factor_all", mBigDecimal.toString());
				}
				break;
			case "80"://状态字1
				map.put("e_statu1", data);
				LOGGER.info("data receive: 状态字1:"+data);
				break;
			case "81"://状态字2
				map.put("e_statu2", data);
				LOGGER.info("data receive: 状态字2:"+data);
				break;
			case "82"://继电器
				map.put("e_switch", data);
				LOGGER.info("data receive: 继电器:"+data);
				break;

			case "1F"://电能
				if(Utilty.isNumeric(data)) {//前后顺序已变更
					String kwh2_str = data.substring(0,8);
					String kwh1_str = data.substring(8,data.length());
					BigDecimal mBigDecimal_kwh1 = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(kwh1_str) / 100));
					LOGGER.info("data receive: 有功总电量:"+mBigDecimal_kwh1);
					BigDecimal mBigDecimal_kwh2 = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(kwh2_str) / 100));
					LOGGER.info("data receive: 无功总电量:"+mBigDecimal_kwh2);
					
					map.put("e_kwh1",mBigDecimal_kwh1.toString());//有功电能
					map.put("e_kwh2",mBigDecimal_kwh2.toString());//无功电能
				}
				break;
			case "2F"://功率 弃用
				if(Utilty.isNumeric(data)) {
					String kw2_c = data.substring(0,6);
					String kw2_b = data.substring(6,12);
					String kw2_a = data.substring(12,18);
					String kw2_all = data.substring(18,24);
					
					String kw1_c = data.substring(24,30);
					String kw1_b = data.substring(30,36);
					String kw1_a = data.substring(36,42);
					String kw1_all = data.substring(42,48);
					
					BigDecimal mbd_kw1_all = new BigDecimal(new DecimalFormat("#0.0000").format(Double.valueOf(kw1_all) / 10000));
					BigDecimal mbd_kw1_a = new BigDecimal(new DecimalFormat("#0.0000").format(Double.valueOf(kw1_a) / 10000));
					BigDecimal mbd_kw1_b = new BigDecimal(new DecimalFormat("#0.0000").format(Double.valueOf(kw1_b) / 10000));
					BigDecimal mbd_kw1_c = new BigDecimal(new DecimalFormat("#0.0000").format(Double.valueOf(kw1_c) / 10000));
					BigDecimal mbd_kw2_all = new BigDecimal(new DecimalFormat("#0.0000").format(Double.valueOf(kw2_all) / 10000));
					BigDecimal mbd_kw2_a = new BigDecimal(new DecimalFormat("#0.0000").format(Double.valueOf(kw2_a) / 10000));
					BigDecimal mbd_kw2_b = new BigDecimal(new DecimalFormat("#0.0000").format(Double.valueOf(kw2_b) / 10000));
					BigDecimal mbd_kw2_c = new BigDecimal(new DecimalFormat("#0.0000").format(Double.valueOf(kw2_c) / 10000));
					LOGGER.info("data receive: 总有功功率:"+mbd_kw1_all.toString());
					LOGGER.info("data receive: 有功功率a:"+mbd_kw1_a.toString());
					LOGGER.info("data receive: 有功功率b:"+mbd_kw1_b.toString());
					LOGGER.info("data receive: 有功功率c:"+mbd_kw1_c.toString());
					LOGGER.info("data receive: 总无功功率:"+mbd_kw2_all.toString());
					LOGGER.info("data receive: 无功功率a:"+mbd_kw2_a.toString());
					LOGGER.info("data receive: 无功功率b:"+mbd_kw2_b.toString());
					LOGGER.info("data receive: 无功功率c:"+mbd_kw2_c.toString());
					map.put("e_kw1_all", mbd_kw1_all.toString());//有功功率
					map.put("e_kw1_a", mbd_kw1_a.toString());
					map.put("e_kw1_b", mbd_kw1_b.toString());
					map.put("e_kw1_c", mbd_kw1_c.toString());
					
					map.put("e_kw2_all", mbd_kw2_all.toString());//无功功率
					map.put("e_kw2_a", mbd_kw2_a.toString());
					map.put("e_kw2_b", mbd_kw2_b.toString());
					map.put("e_kw2_c", mbd_kw2_c.toString());
				}
				break;
			case "3F"://电压 //00 22 00 22 00 22
				if(Utilty.isNumeric(data)) {
					String v_c = data.substring(0,4);
					String v_b = data.substring(4,8);
					String v_a = data.substring(8,12);
					
					BigDecimal mbd_v_a = new BigDecimal(new DecimalFormat("#00.0").format(Double.valueOf(v_a) / 10));
					BigDecimal mbd_v_b = new BigDecimal(new DecimalFormat("#00.0").format(Double.valueOf(v_b) / 10));
					BigDecimal mbd_v_c = new BigDecimal(new DecimalFormat("#00.0").format(Double.valueOf(v_c) / 10));
					LOGGER.info("data receive: A电压:"+mbd_v_a.toString());
					LOGGER.info("data receive: B电压:"+mbd_v_b.toString());
					LOGGER.info("data receive: C电压:"+mbd_v_c.toString());
					
					map.put("e_voltage_a",mbd_v_a.toString());
					map.put("e_voltage_b",mbd_v_b.toString());
					map.put("e_voltage_c",mbd_v_c.toString());
				}
				break;
			case "4F"://电流 00 00 01 00 00 01 00 00 01
				if(Utilty.isNumeric(data)) {
					String c_c = data.substring(0,6);
					String c_b = data.substring(6,12);
					String c_a = data.substring(12,18);
					
					BigDecimal mbd_c_a = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(c_a) / 1000));
					BigDecimal mbd_c_b = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(c_b) / 1000));
					BigDecimal mbd_c_c = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(c_c) / 1000));
					LOGGER.info("data receive: A电流:"+mbd_c_a);
					LOGGER.info("data receive: B电流:"+mbd_c_b);
					LOGGER.info("data receive: C电流:"+mbd_c_c);
					map.put("e_current_a",mbd_c_a.toString());
					map.put("e_current_b",mbd_c_b.toString());
					map.put("e_current_c",mbd_c_c.toString());
				}
				break;
			case "5F"://功率因数  弃用
				if(Utilty.isNumeric(data)) {
					String factor_c = data.substring(0,4);
					String factor_b = data.substring(4,8);
					String factor_a = data.substring(8,12);
					String factor_all = data.substring(12,16);
					
					BigDecimal mbd_f_all = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(factor_all) / 1000));
					BigDecimal mbd_f_a = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(factor_a) / 1000));
					BigDecimal mbd_f_b = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(factor_b) / 1000));
					BigDecimal mbd_f_c = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(factor_c) / 1000));
					LOGGER.info("data receive: 总功率因数:"+ mbd_f_all);
					LOGGER.info("data receive: A功率因数:"+ mbd_f_a);
					LOGGER.info("data receive: B功率因数:"+ mbd_f_b);
					LOGGER.info("data receive: C功率因数:"+ mbd_f_c);
					map.put("e_factor_all", mbd_f_all.toString());//总功率因数
					map.put("e_factor_a", mbd_f_a.toString());
					map.put("e_factor_b", mbd_f_b.toString());
					map.put("e_factor_c", mbd_f_c.toString());
				}
				break;
				
			/*	//事件次数
			case "B0"://停电事件总次数 //C00147000000031900B00203005916
			case "B2"://拉合闸总次数 //C00147000000031900B20209005116
			case "B4"://过流总数 //C00147000000031900B40200005816
			case "B6"://过压事件总数 //C00147000000031900B60200005616
			case "B8"://欠压事件次数 //C00147000000031900B80200005416
			case "BA"://模块重启次数 //C00147000000031900BA0200005216
//			case "A0"://整点冻结次数 //C00147000000031900A0032802004116
			case "A2"://日冻结次数 //C00147000000031900A20203006716
			case "A4"://月冻结次数 //C00147000000031900A401016816
			//事件数据
			case "B1"://停电事件
			case "B3"://拉合闸事件 //C0034700000003190082008A16
			case "B5"://过流事件
			case "B7"://过压事件
			case "B9"://欠压事件
			case "BB"://重启记录最近十次
			case "A3"://第几次日冻结数据
			case "A5"://第几次月冻结数据
			//抄表
			case "F5"://IMSI ASCII //C00147000000031900F50F3436303034353539333130323938310116
			case "F6"://IMEI  ASCII //C00147000000031900F60F383636393731303330333839333337F016
			case "F7"://ICCID ASCII //C00147000000031900F7143839383630343335313031383930303532393830ED16
//			case "F8"://信号强度 //C00147000000031900F80124F116
			case "F9"://驻网状态
//			case "10"://有功总电能 //C0014700000003190010050000000000F916
//			case "20"://总有功功率
//			case "31"://A相电压 //C00147000000031900310258226116
//			case "41"://A相电流 //C001470000000319004103070000C316
//			case "50"://总功率因数
			case "60"://频率
//			case "70"://时间
			case "71"://注册标识
			case "72"://上报时间
			case "73"://认证时长
			case "74"://心跳间隔
//			case "80"://电表运行状态字1
//			case "81"://电表运行状态字2
//			case "82"://继电器控制字
			case "83"://电表常数
			case "90"://厂家标识
			case "91"://硬件版本号
			case "92"://软件版本号
			case "93"://协议版本号
			case "94"://表号
			case "95"://通讯号
			case "96"://用户号
				break;*/
			default:
				break;
			}
			i = i+4 +length*2;
//			System.out.print("---------i_end="+i);
//			System.out.print("\n");
		}
		return map;
	}
	
	/**
	 * 用于解析电表上报事件，芯北电表协议
	 * @param data3 需要解析的数据域
	 * @return
	 */
	public static Map<String ,String> getEventDataValue(String data3) {
		Map<String ,String> map = new HashMap<String ,String>();
		int seq_count = 1;
		int time_count = 1;
		int kwh_count = 1;
		int current_count = 1;
		int voltage_count = 1;
		int status1_count = 1;
		int status2_count = 1;
		int relay_count = 1;
		int module_count = 1;
		for(int i =0,j=data3.length() ;i<j;) {
//			System.out.print("--i_str="+i);
			String typeStr = data3.substring(i, i+2);
			String lenStr = data3.substring(i+2,i+4);
//			System.out.print("---------typeStr="+typeStr);
//			System.out.print("---------lenStr="+lenStr);
			int length = Integer.valueOf(lenStr,16);
			String dataStr = data3.substring(i+4,i+4+length*2);
//			System.out.print("---------length="+length);
//			System.out.print("---------dataStr="+dataStr);
			
			byte[] data_byte = Utilty.hexStringToBytes(dataStr);
			String data = "0";
			if(data_byte != null) {
				data = Utilty.convertByteToString(data_byte, 1, data_byte.length);
			}
//			System.out.print("---------data_byte="+data_byte);
//			LOGGER.info("---------data="+data);
//			map.put("event_type", typeStr);
			switch(typeStr) {
			case "00"://继电器开合，后半段为0的值，直接返回
				//B40205007006191118250819410315350610048369000081020180000000000000000000000000000000000000000000000000000000BD16
				//B2024000700633041029051910040000000080021800810200808201AA00000000000000000000000000000000000000000000000000000000004316
//				map.put("end_time", "");
//				map.put("end_kwh", "");
//				map.put("start_voltage", "");
//				map.put("end_current", "");
//				map.put("end_status1", "");
//				map.put("end_status2", "");
//				map.put("end_switch", "");
//				map.put("end_module", "");
				return map;
			case "B0"://停电
			case "B2"://拉合闸
			case "B4"://过流
			case "B6"://过压
			case "B8"://欠压
			case "BA"://重启记录
			case "BC"://编程记录
				if(Utilty.isNumeric(data)) {
					if(seq_count ==1) {
						LOGGER.info("--data receive: start序列号:"+Integer.parseInt(data));
						seq_count++;
					}else if(seq_count ==2) {
						LOGGER.info("--data receive: end序列号:"+Integer.parseInt(data));
					}
					map.put("e_seq", String.valueOf(Integer.parseInt(data)));
				}
				break;
			case "70"://time
				if(Utilty.isNumeric(data)) {
					if(time_count ==1) {
						LOGGER.info("--data receive: start 时间:"+data);
						map.put("start_time", data);
						time_count++;
					}else if(time_count ==2) {
						LOGGER.info("--data receive: end 时间:"+data);
						map.put("end_time", data);
						time_count++;
					}
				}
				break;
			case "10"://有功总电量
				if(Utilty.isNumeric(data)) {
					if(kwh_count ==1) {
						BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(data) / 100));
						map.put("start_kwh1", mBigDecimal.toString());
						LOGGER.info("--data receive: start 有功总电量:"+mBigDecimal);
						kwh_count++;				
					}else if(kwh_count ==2) {
						BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(data) / 100));
						map.put("end_kwh1", mBigDecimal.toString());
						LOGGER.info("--data receive: end 有功总电量:"+mBigDecimal);
					}
				}
				break;
			case "31"://电压
				if(Utilty.isNumeric(data)) {
					if(voltage_count ==1) {
						BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#00.0").format(Double.valueOf(data) / 10));
						LOGGER.info("--data receive: start 电压:"+mBigDecimal);
						map.put("start_voltage_a", mBigDecimal.toString());
						voltage_count++;
					}else if(voltage_count ==2) {
						BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#00.0").format(Double.valueOf(data) / 10));
						LOGGER.info("--data receive: start 电压:"+mBigDecimal);
						map.put("end_voltage_a", mBigDecimal.toString());
					}
				}
				break;
			case "41"://电流
				if(Utilty.isNumeric(data)) {
					if(current_count ==1) {
						BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(data) / 1000));
						LOGGER.info("--data receive: start 电流:"+mBigDecimal);
						map.put("start_current_a", mBigDecimal.toString());
						current_count++;
					}else if(current_count ==2) {
						BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(data) / 1000));
						LOGGER.info("--data receive: end 电流:"+mBigDecimal);
						map.put("end_current_a", mBigDecimal.toString());
						current_count++;
					}
				}
				break;
			case "80"://电表状态字1
				if( status1_count==1) {
					LOGGER.info("--data receive: start 电表状态字1:"+data);
					map.put("start_status1", data);
					status1_count++;
				}else if(status1_count ==2) {
					LOGGER.info("--data receive: end 电表状态字1:"+data);
					map.put("end_status1", data);
					status1_count++;
				}
				break;
			case "81"://电表状态字2
				if( status2_count==1) {
					LOGGER.info("--data receive: start 电表状态字2:"+data);
					map.put("start_status2", data);
					status2_count++;
				}else if(status2_count ==2) {
					LOGGER.info("--data receive: end 电表状态字2:"+data);
					map.put("end_status2", data);
					status2_count++;
				}
				break;
			case "82"://继电器
				if(relay_count ==1) {
					LOGGER.info("--data receive: start 继电器:"+data);
					map.put("start_switch", data);
					relay_count++;
				}else if(relay_count ==2) {
					LOGGER.info("--data receive: end 继电器:"+data);
					map.put("end_switch", data);
					relay_count++;
				}
				break;
			case "FA"://模块监控状态字
				if(module_count ==1) {
					LOGGER.info("--data receive: start 模块监控状态字:"+data);
					map.put("start_module", data);
					module_count++;
				}else if(module_count ==2) {
					LOGGER.info("--data receive: end 模块监控状态字:"+data);
					map.put("end_module", data);
					module_count++;
				}
				break;
				//DA0D36000000031900BB22BA0298887006485701220719FA030503FFBA0298887006030002220719FA0304FFFF4C16
				
			case "1F"://电能
				if(Utilty.isNumeric(data)) {
					if(kwh_count ==1) {
						String kwh2_str = data.substring(0,8);
						String kwh1_str = data.substring(8,data.length());
						BigDecimal mBigDecimal_kwh1 = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(kwh1_str) / 100));
						LOGGER.info("data receive: 有功总电量:"+mBigDecimal_kwh1);
						BigDecimal mBigDecimal_kwh2 = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(kwh2_str) / 100));
						LOGGER.info("data receive: 无功总电量:"+mBigDecimal_kwh2);
						map.put("start_kwh1",mBigDecimal_kwh1.toString());//有功电能
						map.put("start_kwh2",mBigDecimal_kwh2.toString());//无功电能
						
						kwh_count++;
					}else if(kwh_count ==2) {
						String kwh2_str = data.substring(0,8);
						String kwh1_str = data.substring(8,data.length());
						BigDecimal mBigDecimal_kwh1 = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(kwh1_str) / 100));
						LOGGER.info("data receive: 有功总电量:"+mBigDecimal_kwh1);
						BigDecimal mBigDecimal_kwh2 = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(kwh2_str) / 100));
						LOGGER.info("data receive: 无功总电量:"+mBigDecimal_kwh2);
						
						map.put("end_kwh1",mBigDecimal_kwh1.toString());//有功电能
						map.put("end_kwh2",mBigDecimal_kwh2.toString());//无功电能
						kwh_count++;
					}
				}
				break;
			case "3F"://电压 //00 22 00 22 00 22
				if(Utilty.isNumeric(data)) {
					if(voltage_count ==1) {
						String v_c = data.substring(0,4);
						String v_b = data.substring(4,8);
						String v_a = data.substring(8,12);
						
						BigDecimal mbd_v_a = new BigDecimal(new DecimalFormat("#00.0").format(Double.valueOf(v_a) / 10));
						BigDecimal mbd_v_b = new BigDecimal(new DecimalFormat("#00.0").format(Double.valueOf(v_b) / 10));
						BigDecimal mbd_v_c = new BigDecimal(new DecimalFormat("#00.0").format(Double.valueOf(v_c) / 10));
						LOGGER.info("data receive: A电压:"+mbd_v_a.toString());
						LOGGER.info("data receive: B电压:"+mbd_v_b.toString());
						LOGGER.info("data receive: C电压:"+mbd_v_c.toString());
						
						map.put("start_voltage_a",mbd_v_a.toString());
						map.put("start_voltage_b",mbd_v_b.toString());
						map.put("start_voltage_c",mbd_v_c.toString());
						
						voltage_count++;
					}else if(voltage_count ==2) {
						String v_c = data.substring(0,4);
						String v_b = data.substring(4,8);
						String v_a = data.substring(8,12);
						
						BigDecimal mbd_v_a = new BigDecimal(new DecimalFormat("#00.0").format(Double.valueOf(v_a) / 10));
						BigDecimal mbd_v_b = new BigDecimal(new DecimalFormat("#00.0").format(Double.valueOf(v_b) / 10));
						BigDecimal mbd_v_c = new BigDecimal(new DecimalFormat("#00.0").format(Double.valueOf(v_c) / 10));
						LOGGER.info("data receive: A电压:"+mbd_v_a.toString());
						LOGGER.info("data receive: B电压:"+mbd_v_b.toString());
						LOGGER.info("data receive: C电压:"+mbd_v_c.toString());
						
						map.put("end_voltage_a",mbd_v_a.toString());
						map.put("end_voltage_b",mbd_v_b.toString());
						map.put("end_voltage_c",mbd_v_c.toString());
						voltage_count++;
					}
				}
				break;
			case "4F"://电流 00 00 01 00 00 01 00 00 01
				if(Utilty.isNumeric(data)) {
					if(current_count ==1) {
						String c_c = data.substring(0,6);
						String c_b = data.substring(6,12);
						String c_a = data.substring(12,18);
						
						BigDecimal mbd_c_a = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(c_a) / 1000));
						BigDecimal mbd_c_b = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(c_b) / 1000));
						BigDecimal mbd_c_c = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(c_c) / 1000));
						LOGGER.info("data receive: A电流:"+mbd_c_a);
						LOGGER.info("data receive: B电流:"+mbd_c_b);
						LOGGER.info("data receive: C电流:"+mbd_c_c);
						map.put("start_current_a",mbd_c_a.toString());
						map.put("start_current_b",mbd_c_b.toString());
						map.put("start_current_c",mbd_c_c.toString());
						
						current_count++;
					}else if(current_count ==2) {
						String c_c = data.substring(0,6);
						String c_b = data.substring(6,12);
						String c_a = data.substring(12,18);
						
						BigDecimal mbd_c_a = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(c_a) / 1000));
						BigDecimal mbd_c_b = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(c_b) / 1000));
						BigDecimal mbd_c_c = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(c_c) / 1000));
						LOGGER.info("data receive: A电流:"+mbd_c_a);
						LOGGER.info("data receive: B电流:"+mbd_c_b);
						LOGGER.info("data receive: C电流:"+mbd_c_c);
						map.put("end_current_a",mbd_c_a.toString());
						map.put("end_current_b",mbd_c_b.toString());
						map.put("end_current_c",mbd_c_c.toString());
						
						current_count++;
					}
				}
				break;
			default:
				break;
			}
			i = i+4 +length*2;
//			System.out.print("---------i_end="+i);
//			System.out.print("\n");
		}
		return map;
	}
	
	/**
	 * 用于解析下行指令后电表返回的数据，芯北电表协议
	 * @param data3 需要解析的数据域
	 * @param typeStr 数据类型
	 * @return
	 */
	public static Map<String ,String> getReadValue(String typeStr, String data3) {//读取单条数据
		String dataStr = data3;
		byte[] data_byte = Utilty.hexStringToBytes(dataStr);
		String data = Utilty.convertByteToString(data_byte, 1, data_byte.length);
		
		Map<String ,String> map = new HashMap<String ,String>();
		map.put("read_type", typeStr);
		switch(typeStr) {
		case "10"://有功总电量
//		case "11":
//		case "12":
//		case "13":
		case "15":
			if(Utilty.isNumeric(data)) {
				BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(data) / 100));
				LOGGER.info("data receive: 有功总电量:"+mBigDecimal);
//					map.put("e_readings",mBigDecimal.toString());
				map.put("read_value", mBigDecimal.toString());
			}
			break;
		case "14":
		case "19":
			map.put("read_value", data);
			break;
		case "20"://有功总功率
		case "21":
		case "22":
		case "23":
		case "24":
		case "25":
		case "26":
		case "27":
			if(Utilty.isNumeric(data)) {
				BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.0000").format(Double.valueOf(data) / 10000));
				LOGGER.info("data receive: 有功总功率:"+mBigDecimal);
//					map.put("e_rate", mBigDecimal.toString());
				map.put("read_value", mBigDecimal.toString());
			}
			break;
		case "31"://电压
		case "32":
		case "33":
			if(Utilty.isNumeric(data)) {
				BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#00.0").format(Double.valueOf(data) / 10));
				LOGGER.info("data receive: 电压:"+mBigDecimal);
//					map.put("e_voltage", mBigDecimal.toString());
				map.put("read_value", mBigDecimal.toString());
			}
			break;
		case "41"://电流
		case "42":
		case "43":
			if(Utilty.isNumeric(data)) {
				BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(data) / 1000));
				LOGGER.info("data receive: 电流:"+mBigDecimal);
//					map.put("e_current", mBigDecimal.toString());
				map.put("read_value", mBigDecimal.toString());
			}
			break;
		case "50"://总功率因数
		case "51":
		case "52":
		case "53":
			if(Utilty.isNumeric(data)) {
				BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(data) / 1000));
				LOGGER.info("data receive: 功率因数:"+mBigDecimal);
				map.put("read_value", mBigDecimal.toString());
			}
			break;
		case "60"://频率
			if(Utilty.isNumeric(data)) {
				BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(data) / 100));
				LOGGER.info("data receive: 频率:"+mBigDecimal);
//					map.put("e_readings",mBigDecimal.toString());
				map.put("read_value", mBigDecimal.toString());
			}
			break;
		case "1F":
		case "2F":
		case "3F":
		case "4F":
		case "5F":
			map.put("read_value", data);
			break;
		case "70"://time
			if(Utilty.isNumeric(data)) {
				LOGGER.info("data receive: 时间:"+data);
//					map.put("e_time", String.valueOf(data));
				map.put("read_value", String.valueOf(data));
			}
			break;
		case "71"://注册标识
			map.put("read_value", data);
			break;
		case "72"://上报时间
		case "73"://认证时长
		case "74"://心跳间隔
			if(Utilty.isNumeric(data)) {
				long time = Long.parseLong(data);
				LOGGER.info("data receive: 上报时间/认证时长/心跳间隔:"+time);
				map.put("read_value", String.valueOf(time));
			}
			break;
		case "91"://硬件版本号 01031920
		case "92"://软件版本号 07051920
		case "93"://协议版本号  18010319
			map.put("read_value", dataStr);
			break;
		case "80"://状态字1
		case "81"://状态字2
		case "82"://继电器
		case "83"://电表常数
		case "90"://厂家标识
		case "94"://表号
		case "95"://通讯号
		case "96"://用户号
			map.put("read_value", data);
			break;
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
			if(Utilty.isNumeric(data)) {
				LOGGER.info("data receive: 事件总次数:"+data);
				int times = Integer.parseInt(data);
				map.put("read_value", String.valueOf(times));
			}
			break;
		//事件数据
		case "A3"://第几次日冻结数据
		case "A5"://第几次月冻结数据
		case "B1"://停电事件
		case "B3"://拉合闸事件 //C0034700000003190082008A16
		case "B5"://过流事件
		case "B7"://过压事件
		case "B9"://欠压事件
		case "BB"://重启记录最近十次
		case "BD"://编程记录最近十次
			map.put("read_value", data);
			break;
		//抄表
		case "F1"://读RAM的XXXXXXXX地址后的N字节
		case "F2"://读ROM的XXXXXXXX地址后的N字节
		case "F3"://读EEPROM的XXXXXXXX地址后的N字节
		case "F4"://读计量芯片指定寄存器PP页，RR寄存器地址数据
			map.put("read_value", data);
			break;
		case "F5"://IMSI ASCII //C00147000000031900F50F3436303034353539333130323938310116
		case "F6"://IMEI  ASCII //C00147000000031900F60F383636393731303330333839333337F016
		case "F7"://ICCID ASCII //C00147000000031900F7143839383630343335313031383930303532393830ED16
			if(Utilty.isNumeric(data)) {
				String i_ascii = Utilty.hexStringToString(dataStr);
				LOGGER.info("data receive: ASCII value:"+ i_ascii);
				map.put("read_value", i_ascii);
			}
			break;
		case "F8"://信号值 //C00147000000031900F80124F116
			if(Utilty.isNumeric(data)) {
				int length = data.length();
				if(length == 2) {
					BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0").format(Double.valueOf(data)));
					LOGGER.info("data receive: 信号值:"+mBigDecimal);
//						map.put("e_signal", mBigDecimal.toString());
					map.put("read_value", mBigDecimal.toString());
				}else if(length == 4){//F8 02 32 32
					String signal = Utilty.hexStringToString(dataStr);
					LOGGER.info("data receive: 信号值:"+ signal);
//						map.put("e_signal", signal);
					map.put("read_value", signal);
				}
			}
			break;
		case "F9"://驻网状态
		case "FA"://模块监控状态字
			map.put("read_value", data);
			break;
		default:
			break;
		}
		return map;
	}
	
	/**
	 * 电信平台传输来的数据解析
	 * @param orig_data 需要解析的数据
	 * @return
	 */
	public static NBDXDataPo getDXNBData(String orig_data) {
		NBDXDataPo mNBDXDataPo = null;
		byte[] binaryData = Utilty.hexStringToBytes(orig_data);
		int len = binaryData.length;
		String startbyte = Integer.toHexString(binaryData[0] & 0xFF) ;
		String endbyte = Integer.toHexString(binaryData[len - 1] & 0xFF) ;
		LOGGER.info("GPRS: startbyte:"+ startbyte );
		LOGGER.info("GPRS: endbyte:"+ endbyte);
		//lora \x0001000000000000ff7240972500000000902000000000000097490000000000000000000000000000000000000001030201181516
		//nb_dx  0000350001350100000007FF724047084600000052220000179500000250000095462000879405006331210060090000003214010118A316
		if (/*"80".equals(startbyte) && */"16".equals(endbyte)) {//判定 0x16 == binaryData[len - 1]
			//表号
			String e_num = Utilty.convertByteToString(binaryData, 6, 11);
			System.out.println("nb dx  data : 表号4-9:" + e_num);
			
			String e_fac = Utilty.convertByteToString(binaryData, 12, 12);//data.substring(16, 18);
			System.out.println("nb dx  data : 厂商="+ e_fac);
			
			String dataType = Utilty.convertByteToString(binaryData, 13, 13);
			System.out.println("nb dx  data : 数据类型="+ dataType);
			
			mNBDXDataPo = new NBDXDataPo();
			mNBDXDataPo.setE_num(e_num);
			mNBDXDataPo.setE_fac(e_fac);
			
			byte dataLen = binaryData[15];
			System.out.println("nb dx  data : data Length="+ dataLen);
			if(dataLen > 0) {
				// 有功总电量
				BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.00")
						.format(Double.valueOf(Utilty.convertByteToString(binaryData, 15, 20)) / 100));
				System.out.println("nb dx data 电量:"+ mBigDecimal);
				
				BigDecimal voltage = new BigDecimal(new DecimalFormat("#0.00")
						.format(Double.valueOf(Utilty.convertByteToString(binaryData, 21, 24)) / 10));
				System.out.println("nb dx data:电压："+ voltage);
				
				BigDecimal electricity = new BigDecimal(new DecimalFormat("#0.00")
						.format(Double.valueOf(Utilty.convertByteToString(binaryData, 25, 28)) / 1000));
				System.out.println("nb dx data 电流:"+ electricity);
				
				String time = Utilty.convertByteToString(binaryData, 49, 54);
				System.out.println("nb dx data time:"+ time);
				
				mNBDXDataPo.setE_readings(mBigDecimal.toString());
				mNBDXDataPo.setE_voltage(voltage.toString());
				mNBDXDataPo.setE_current(electricity.toString());
				mNBDXDataPo.setE_signal("");
				mNBDXDataPo.setE_time(time);
				
			}else {
				mNBDXDataPo.setE_readings("");
				mNBDXDataPo.setE_voltage("");
				mNBDXDataPo.setE_current("");
				mNBDXDataPo.setE_signal("");
				mNBDXDataPo.setE_time("");
			}
		}
		
		return mNBDXDataPo;
	}
	/**
	 * 用于解析水表上报数据，芯北电表协议 单相电表
	 * @param data3 需要解析的数据域
	 * @return
	 */
	public static Map<String ,String> getWMDataValue(String data) {
		Map<String ,String> map = new HashMap<String ,String>();
		//901F012C000000002C0000000035000000004120141612192040210002
		String data_flag1=data.substring(0, 2);
		String data_flag2=data.substring(2, 4);
		String data_seq=data.substring(4, 6);
		String wm_flow=data.substring(6, 16);
		String wm_reverse_flow=data.substring(16, 26);
		String wm_flow_rate=data.substring(26, 36);
		String wm_time=data.substring(36, 50);
		String wm_statu1=data.substring(50, 54);
		String wm_statu2=data.substring(54, 58);
		
		byte[] wm_flow_binary = Utilty.hexStringToBytes(wm_flow);
		BigDecimal wm_flow_b = new BigDecimal(new DecimalFormat("#0.000")
				.format(Double.valueOf(Utilty.convertByteToString(wm_flow_binary, 2, wm_flow_binary.length)) / 1000));
		LOGGER.info("累计流量："+ wm_flow_b +"M3");
		
		byte[] wm_reverse_flow_binary = Utilty.hexStringToBytes(wm_reverse_flow);
		BigDecimal wm_reverse_flow_b = new BigDecimal(new DecimalFormat("#0.000")
				.format(Double.valueOf(Utilty.convertByteToString(wm_reverse_flow_binary, 2, wm_reverse_flow_binary.length)) / 1000));
		LOGGER.info("反向累计流量："+ wm_reverse_flow_b +"M3");
		
		byte[] wm_flow_rate_binary = Utilty.hexStringToBytes(wm_flow_rate);
		BigDecimal wm_flow_rate_b = new BigDecimal(new DecimalFormat("#0.0000")
				.format(Double.valueOf(Utilty.convertByteToString(wm_flow_rate_binary, 2, wm_flow_rate_binary.length)) / 10000));
		LOGGER.info("流速量："+ wm_flow_rate_b +"M3/h");
		
		byte[] wm_time_binary = Utilty.hexStringToBytes(wm_time);
		String wm_time_b = Utilty.convertByteToString(wm_time_binary, 1, wm_time_binary.length);
		LOGGER.info("数据时间："+ wm_time_b);
		
		map.put("data_flag1", data_flag1);
		map.put("data_flag2", data_flag2);
		map.put("data_seq", data_seq);
		map.put("wm_flow", wm_flow_b.toString());
		map.put("wm_reverse_flow", wm_reverse_flow_b.toString());
		map.put("wm_flow_rate", wm_flow_rate_b.toString());
		map.put("wm_time", wm_time_b);
		map.put("wm_statu1", wm_statu1);
		map.put("wm_statu2", wm_statu2);
		
//		System.out.println("data_flag1:"+data_flag1);
//		System.out.println("data_flag2:"+data_flag2);
//		System.out.println("data_seq:"+data_seq);
//		System.out.println("wm_flow:"+wm_flow);
//		System.out.println("wm_reverse_flow:"+wm_reverse_flow);
//		System.out.println("wm_flow_rate:"+wm_flow_rate);
//		System.out.println("wm_time:"+wm_time);
//		System.out.println("wm_statu1:"+wm_statu1);
//		System.out.println("wm_statu2:"+wm_statu2);
		
		return map;
	}
	
	/**
	 * Udp水表数据解析  ZP超声波阀控水表NB-iot传协议
	 * @param data
	 * @return
	 */
	public static Map<String ,String> getUdpWMDataValue(String data) {
		Map<String ,String> map = new HashMap<String ,String>();
		//901F012C000000002C0000000035000000004120141612192040210002
		//921F012C560100003500000000A0052C560100002C560100002C56010000FFFF00000028411519052020
		//4021A005A0 053CA00517 061606F405 F205010B00 27976490191069048689 34535647108866080000 2A030B000100FFFF2E18
		//92
		//1F
		//01
		//2C56010000  //为累计流量 0.025 M3，2C 为累计流量单位代号表示M3（表1）
		//3500000000  //为瞬时流量 0.0170 M3/h，35 为瞬时流量单位代号表示M3/h（表1）
		//A005  //表示上报时间间隔单位分钟 
		//2C56010000
		//2C56010000
		//2C56010000
		//FFFF  //表示水压 282kpa，单位kpa(小表预留，字节内容为 0xFFFF)
		//000000 //预留字
		//33281619052020 实时时间
		//4021  状态字
		//A005 //为上报时间间隔单位分钟，1440分钟(1天)
		//A005
		//3C  //为上报延时秒(16进制数值)60秒,如果>=60,则NB模块按照自身的IMEI号和上报间隔自行分流 U8
		//A005
		//6A07
		//6907 //上传成功次数（16进制数值）101次  U16
		//4507
		//4307
		//01  数据是否有效
		//0E  信号强度RSSI 
		//00  运营商
		//27976490191069048689   ICCID 
		//34535647108866080000   IMEI
		//2A030B000100
		//FFFF2E690016
		String data_flag1=data.substring(0, 2);
		String data_flag2=data.substring(2, 4);
		String data_seq=data.substring(4, 6);
		String wm_flow=data.substring(6, 16);
		String wm_flow_rate=data.substring(16, 26);
		String wm_pressure = data.substring(60, 64);
		String wm_time=data.substring(70, 84);
		String wm_statu1=data.substring(84, 88);
//		String wm_statu2=data.substring(54, 58);
		String iccid = data.substring(124, 144);
		String imei = data.substring(144, 164);
		
		byte[] wm_flow_binary = Utilty.hexStringToBytes(wm_flow);
		BigDecimal wm_flow_b = new BigDecimal(new DecimalFormat("#0.000")
				.format(Double.valueOf(Utilty.convertByteToString(wm_flow_binary, 2, wm_flow_binary.length)) / 1000));
		LOGGER.info("累计流量："+ wm_flow_b +"M3");
		
		byte[] wm_flow_rate_binary = Utilty.hexStringToBytes(wm_flow_rate);
		BigDecimal wm_flow_rate_b = new BigDecimal(new DecimalFormat("#0.0000")
				.format(Double.valueOf(Utilty.convertByteToString(wm_flow_rate_binary, 2, wm_flow_rate_binary.length)) / 10000));
		LOGGER.info("瞬时流量："+ wm_flow_rate_b +"M3/h");
		
		byte[] wm_pressure_binary = Utilty.hexStringToBytes(wm_pressure);
		String wm_pressure_b = Utilty.convertByteToString(wm_pressure_binary,1,wm_pressure_binary.length);
		System.out.println("水压："+ Integer.parseInt(wm_pressure_b, 16));
		
		byte[] wm_time_binary = Utilty.hexStringToBytes(wm_time);
		String wm_time_b =Utilty.convertByteToString(wm_time_binary, 1, wm_time_binary.length);
		LOGGER.info("数据时间："+ wm_time_b);
		
		byte[] statu1_binary = Utilty.hexStringToBytes(wm_statu1);
		String statu1_b = Utilty.convertByteToString(statu1_binary, 1, statu1_binary.length);
		System.out.println("imei："+ statu1_b);
		
		
		byte[] iccid_binary = Utilty.hexStringToBytes(iccid);
		String iccid_b = Utilty.convertByteToString(iccid_binary, 1, iccid_binary.length);
		System.out.println("ICCID："+ iccid_b);
		
		byte[] imei_binary = Utilty.hexStringToBytes(imei);
		String imei_b = Utilty.convertByteToString(imei_binary, 1, imei_binary.length);
		String imei_b_n = imei_b.substring(5, imei_b.length());
		System.out.println("imei："+ imei_b_n);
		
		map.put("data_flag1", data_flag1);
		map.put("data_flag2", data_flag2);
		map.put("data_seq", data_seq);
		map.put("wm_flow", wm_flow_b.toString());
		map.put("wm_flow_rate", wm_flow_rate_b.toString());
		map.put("wm_time", wm_time_b);
		map.put("wm_statu1", statu1_b);
		map.put("iccid", iccid_b);
		map.put("imei", imei_b_n);
		
		return map;
	}
	
	/**
	 * 读阀值状态返回值
	 * @param data
	 * @return
	 */
	public static Map<String ,String>  getUdpWMReadDataValue(String data) {
		Map<String ,String> map = new HashMap<String ,String>();
		//A0170140225516
		//A0170140421C
		String DI0 = data.substring(0, 2);
		String DI1 = data.substring(2, 4);
		String seq = data.substring(4, 6);
		String sys_status = data.substring(6, 10);
		if(("17").equals(DI1)) {
			map.put("type", "阀门控制");
		}
		if(data.length() == 12) {
			map.put("switch_status", "写阀值成功");
		}else if(data.length() == 14) {
			String switch_status = data.substring(10, 12);
			String switch_status_str = "";
			
			switch(switch_status) {
			//55 -> 强制开阀, 99 -> 强制关阀, 
	        //66 -> 自由开, 77 -> 自由关, FF -> 自由控制
			case "55"://强制开阀
				switch_status_str = "强制开阀";
				break;
			case "99"://强制关阀
				switch_status_str = "强制关阀";
				break;
			case "66"://自由开阀
				switch_status_str = "自由开阀";
				break;
			case "77"://自由关阀
				switch_status_str = "自由关阀";
				break;
			case "FF"://自由控制
				switch_status_str = "自由控制";
				break;
				default:
					break;
			}
			map.put("switch_status", switch_status_str);
		}
		
		return map;
	}
}
