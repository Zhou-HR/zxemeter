package com.gdiot.ssm.cmds;

import com.alibaba.fastjson.JSONObject;
import com.gdiot.ssm.http.yd.BasicResponse;
import com.gdiot.ssm.http.yd.CmdsResponse;
import com.gdiot.ssm.util.CRC16;
import com.gdiot.ssm.util.Utilty;
import com.gdiot.ssm.util.YDConfig;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZhouHR
 */
@Slf4j
public class QDSendCmdsUtils {

    public QDSendCmdsUtils() {

    }

    public JSONObject SendMsgNB(String imei_akr, String content, String time) {
        String url = YDConfig.YD_EXECUTE_URL + "imei=" + imei_akr + "&obj_id=" + YDConfig.AKR_OBJ_ID
                + "&obj_inst_id=" + YDConfig.AKR_OBJ_INST_ID + "&res_id=" + YDConfig.AKR_RES_ID;
//				logger.info("--------------getCmdsInfo---url=="+ url);

        //api_key akr
        String api_key = YDConfig.AKR_API_KEY;

        NBSendCmds mSendCmds = new NBSendCmds(url, content, time, api_key);
        BasicResponse<CmdsResponse> response = mSendCmds.executeApi();

        JSONObject jo = JSONObject.parseObject(response.getJson());
        String errno = String.valueOf(jo.get("errno"));
        String error = String.valueOf(jo.get("error"));
        log.info("send_cmd--errno=" + errno + ",error=" + error + ",data:" + jo.get("data"));
        log.info("result=" + jo.toString());
        return jo;
    }


    public Map<String, Object> getCmdsInfo(Map<String, String> msgMap) {
        String module_type = msgMap.get("module_type");
        String imei = msgMap.get("imei");
        String type = msgMap.get("type");
        String value = msgMap.get("value");
        String operate_type = msgMap.get("operate_type");
        String request_id = msgMap.get("request_id");
        String e_num = msgMap.get("e_num");
        String fac_id = msgMap.get("fac_id");
        String time = msgMap.get("time");
        Map<String, Object> map = new HashMap<>();

//			logger.info("--------------getCmdContent start----------");
//			logger.info("--------------getCmdContent start----------module_type="+module_type);
//			logger.info("--------------getCmdContent start----------new_seq_hex="+new_seq_hex);
//			logger.info("--------------getCmdContent start----------e_num="+e_num);
//			logger.info("--------------getCmdContent start----------fac_id="+fac_id);
//			logger.info("--------------getCmdContent start----------type="+type);
//			logger.info("--------------getCmdContent start----------operate_type="+operate_type);
        try {
            String content = getAKRNBCmdMsg(type, operate_type, value);
            log.info("--------------getCmdsInfo---content==" + content);
            map.put("content", content);
            return map;
        } catch (Exception e) {
            log.error("e=" + e);
        }
        return map;
    }

    public static String getAKRNBCmdMsg(String data_type, String operate_type, String value) {
        //起始符
        String start = "7B7B";
        //设备编码
//		String dev_code = "AAAAAAAAAAAA";
        //功能码：90 透传
        String function_code = "90";
        //数据域：完整的modbus帧（若发送645帧，改成645帧就行）
        String modbus_data;
        //CRC
        String CRC;
        //结束符
        String end = "7D7D";

        String content = null;
        int name_type_len = data_type.length();
        //透传  寄存器查询
        if (name_type_len == 4) {
            modbus_data = getModbusCmdMsg(data_type, operate_type, value);
            CRC = CRC16.getCRC16(function_code + modbus_data);
            content = start + function_code + modbus_data + CRC + end;
            System.out.printf("----------content==" + content + "\n");
        }
        return content;
    }

    public static String getModbusCmdMsg(String name_type, String operate_type, String value) {
        //起始符
        String start = "01";
        //功能码：写多个寄存器
        String function_code;
        //寄存器起始地址
        String register_addr;
        //要操作的寄存器个数
        String register_count;
        //  数据字节数 （占1字节）
        String data_byte_count;
        //数据内容(A822寄存器)
        String data = "";
        //CRC16校验
        String CRC;

        String content = null;
        register_addr = name_type;
        switch (name_type) {
//		case "":

            //	组合有功总电能	RO	4字节 2位小数   读电量：01 03 00 3E 00 02 A5 C7
            case "003C":

                //正向有功电能	RO	4字节  2位小数
            case "003E":
                register_count = "0002";
                if ("R".equals(operate_type)) {
                    function_code = "03";
                    content = start + function_code + register_addr + register_count;
                }
                break;
            //解析时暂时无法区分，此处不单独下行查询
//		case "0014"://A相电压	  2 1位小数  读电压电流：01 03 00 3E 00 02 A5 C7
//		case "0015"://B相电压	  2 1位小数
//		case "0016"://C相电压	  2 1位小数
//		case "001A"://A相电流	  2 2位小数
//		case "001B"://B相电流	  2 2位小数
//		case "001C"://C相电流	  2 2位小数
//			register_count = "0001";
//			if("R".equals(operate_type)) {
//				function_code = "03";
//				content = start + function_code + register_addr + register_count ;
//			}
//			break;
            //A相有功功率
            case "001E":
                //B相有功功率
            case "0020":
                //C相有功功率
            case "0022":
                //总有功功率  	RO	4字节  2位小数
            case "0024":
                register_count = "0002";
                if ("R".equals(operate_type)) {
                    function_code = "03";
                    content = start + function_code + register_addr + register_count;
                }
                break;
            case "01C2"://操作拉合闸控制	DO1 2字节   //01 10 01 C2 00 01 02 00 01 67 B2
//		case "01C3"://操作拉合闸控制	DO2 2字节
                if ("O".equals(operate_type)) {
                    function_code = "10";
                    register_count = "0001";
                    //  数据字节数 （占1字节）
                    data_byte_count = "02";

                    if (value != "" && "on".equals(value)) {
                        //合闸
                        data = "0001";
                    } else if ("off".equals(value)) {
                        //开闸
                        data = "0000";
                    }
                    content = start + function_code + register_addr + register_count + data_byte_count + data;
                } else if ("R".equals(operate_type)) {
                    function_code = "03";
                    register_count = "0001";
                    content = start + function_code + register_addr + register_count;
                }
                break;
            //写上传间隔：01 10 1002 0001 02 0003 B6 83
            case "1001":
                if ("W".equals(operate_type)) {
                    function_code = "10";
                    register_count = "0001";
                    //  数据字节数 （占1字节）
                    data_byte_count = "02";
//				data = "0003";//3分钟
                    if (value != "" && value != null) {
                        int times = Integer.valueOf(value);
                        if (times >= 1 && times <= 1440) {
                            data = getHexTime(times);
                        }
                    } else {
                        //10分钟
                        data = "0A00";
                    }
                    content = start + function_code + register_addr + register_count + data_byte_count + data;
                } else if ("R".equals(operate_type)) {
                    function_code = "03";
                    register_count = "0001";
                    content = start + function_code + register_addr + register_count;
                }
                break;
            default:
                break;
        }
        CRC = CRC16.getCRC16(content);
        content = content + CRC;
        return content;
    }

    public static String getHexDataLength(String data) {
        int len = data.length() / 2;
        String.valueOf(data.length() / 2);

        return "";
    }

    /**
     * //十进制   ----》 十六进制-----》4位 ，高位补0--------》time
     *
     * @return
     */
    public static String getHexTime(int time) {
        String hexTime = Utilty.DecToHexString(time);
        int hexLen = hexTime.length();
        if (hexLen == 1) {
            hexTime = "000" + hexTime;
        } else if (hexLen == 2) {
            hexTime = "00" + hexTime;
        } else if (hexLen == 3) {
            hexTime = "0" + hexTime;
        }
//		return hexTime;//高低位不互换
        //高低位互换
        byte[] e = Utilty.hexStringToBytes(hexTime);
        String new_hex_time = Utilty.convertByteToString(e, 1, e.length);
        return new_hex_time;
    }

    private String getTimesHex(String times, int len) {
//	if((Long.parseLong(seq) >= 360) && (Long.parseLong(seq) < 86400)) {
//		dih_lon = "7203"+getTimesHex(seq,3);
//	}		

        //序列号高位补0
        DecimalFormat df = null;
        if (len == 2) {
            df = new DecimalFormat("0000");
        } else if (len == 3) {
            df = new DecimalFormat("000000");
        }
        String seq_s = df.format(Integer.parseInt(times));
//		System.out.println(seq_s);
        //将序列号高低位互换
        byte[] seq_b = Utilty.hexStringToBytes(seq_s);
        String seq16 = Utilty.convertByteToString(seq_b, 1, seq_b.length);
        System.out.println("seq16=" + seq16);
        return seq16;
    }
}
