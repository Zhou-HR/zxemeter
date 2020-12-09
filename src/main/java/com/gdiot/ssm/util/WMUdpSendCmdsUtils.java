package com.gdiot.ssm.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gdiot.ssm.entity.WMDataPo;
import com.gdiot.ssm.service.IWMDataService;
import com.gdiot.ssm.util.CRC16;
import com.gdiot.ssm.util.LoraConfig;
import com.gdiot.ssm.util.SpringContextUtils;
import com.gdiot.ssm.util.Utilty;

public class WMUdpSendCmdsUtils {
	private final static Logger logger = LoggerFactory.getLogger(WMUdpSendCmdsUtils.class);
	private IWMDataService mIWMDataService; 
	
	public WMUdpSendCmdsUtils() {
		
	}
	/**
	 * 水表。udp
	 * @param meter_no
	 * @param data_type
	 * @param operate_type
	 * @param value
	 * @return
	 */
	public static String getCmdContent(String meter_no,String data_type,String operate_type, String value) {
		//自由开阀FE FE FE 68 10 01 13 12 19 00 00 00 04 04 A0 17 01 66 DD 16
		//自由关阀FE FE FE 68 10 01 13 12 19 00 00 00 04 04 A0 17 01 77 EE 16
		//读阀控状态:FEFEFE6810011312190000000404A01701007716
		//读表头地址:FEFEFE6810AAAAAAAAAAAAAA0303810A01B016
		//强制开阀：  FEFEFE6810011312190000000404A0170155CC16
		//强制关阀：  FEFEFE6810011312190000000404A01701991016
		//xFEFEFE6810011312190000000404A01701991016
		String lora_start = "FEFEFE";
		String id = "68";//起始帧
		String prm = "55";//仪表类型
		String e_num2 = meter_no;//表号
		String dih_lon = "";//A017015500 控制码（请求数据类型）+长度+ 数据（类型包含：数据标识，数据标识，序列号，操作 00 ）
		String CRC = "";
		String end = "16";
		switch(data_type) {
		case "wm_switch"://强制开关阀
			if("O".equals(operate_type)) {//执行，操作
				if("on".equals(value)) {
					dih_lon = "2405A017015500";//强制开阀
				}else if("off".equals(value)) {
					dih_lon = "2405A017019900";//强制关阀
				}else if("back".equals(value)) {
					dih_lon = "2405A01701FF00";//强制撤消
				}else if("free_on".equals(value)) {
					dih_lon = "2405A017016600";//自由开阀
				}else if("free_off".equals(value)) {
					dih_lon = "2405A017017700";//自由关阀
				}
			}else if("R".equals(operate_type)) {
				dih_lon = "2405A017010000";//读阀控状态
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
		CRC = CRC16.getCRC8(id+prm +enum16 +dih_lon).toUpperCase();
//		System.out.println("CRC="+CRC);
		String contents= lora_start  + id + prm + enum16 + dih_lon + CRC +end;
//		System.out.println("contents="+contents);
		return contents;
	}
	
	/**
	 * 水表。lora
	 * @param meter_no
	 * @param data_type
	 * @param operate_type
	 * @param value
	 * @return
	 */
	public String getCmdContent0(String meter_no,String data_type,String operate_type, String value) {
		//自由开阀FE FE FE 68 10 01 13 12 19 00 00 00 04 04 A0 17 01 66 DD 16
		//自由关阀FE FE FE 68 10 01 13 12 19 00 00 00 04 04 A0 17 01 77 EE 16
		//读阀控状态:FEFEFE6810011312190000000404A01701007716
		//读表头地址:FEFEFE6810AAAAAAAAAAAAAA0303810A01B016
		//强制开阀：  FEFEFE6810011312190000000404A0170155CC16
		//强制关阀：  FEFEFE6810011312190000000404A01701991016
		//xFEFEFE6810011312190000000404A01701991016
		String lora_start = "FEFEFE";
		String id = "68";//起始帧
		String prm = "10";//仪表类型
		String e_num2 = meter_no;//表号
		String dih_lon = "";//FF03103141 请求数据类型 长度 数据  类型包含 
		String CRC = "";
		String end = "16";
		switch(data_type) {
		case "wm_switch"://强制开关阀
			if("O".equals(operate_type)) {//执行，操作
				if("on".equals(value)) {
					dih_lon = "0404A0170155";//
				}else if("off".equals(value)) {
					dih_lon = "0404A0170199";//
				}
			}else if("R".equals(operate_type)) {
				dih_lon = "0404A0170100";//读阀控状态
			}
			break;
		case "wm_num"://表号
			e_num2 = "AAAAAAAAAAAAAA";
			dih_lon = "0303810A01";
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
		CRC = CRC16.getCRC8(id+prm +enum16 +dih_lon).toUpperCase();
//		System.out.println("CRC="+CRC);
		String contents= lora_start  + id + prm + enum16 + dih_lon + CRC +end;
//		System.out.println("contents="+contents);
		return contents;
	}
}
