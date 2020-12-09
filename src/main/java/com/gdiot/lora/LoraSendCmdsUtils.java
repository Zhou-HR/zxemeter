package com.gdiot.lora;

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

import com.gdiot.model.WMDataPo;
import com.gdiot.service.IWMDataService;
import com.gdiot.util.CRC16;
import com.gdiot.util.LoraConfig;
import com.gdiot.util.SpringContextUtils;
import com.gdiot.util.Utilty;

/**
 * @author ZhouHR
 */
public class LoraSendCmdsUtils {
    private final static Logger logger = LoggerFactory.getLogger(LoraSendCmdsUtils.class);
    private IWMDataService mIWMDataService;

    public LoraSendCmdsUtils() {

    }

    public static String getTimeContent(String dev_eui, String e_num, String e_fac) throws JSONException {
        String loraStart = "FEFEFEFE";
        String id = "00";//服务序号
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
        String time16 = Utilty.convertByteToString(e_time, 1, e_time.length);
        System.out.println("time16=" + time16);
        dih_lon = dih_lon + time16;

        //将表号高低位互换
        byte[] e = Utilty.hexStringToBytes(e_num2);
//				System.out.println("length="+e.length);
        String enum16 = Utilty.convertByteToString(e, 1, e.length);
//				System.out.println("enum16="+enum16);
//				System.out.println("d_str="+ id+ prm +e_num2 + fac_id +dih_lon);
        CRC = CRC16.getCRC(id + prm + enum16 + fac_id + dih_lon).toUpperCase();
//				System.out.println("CRC="+CRC);
        String contents = loraStart + id + prm + enum16 + fac_id + dih_lon + CRC + end;
//				System.out.println("contents="+contents);

        JSONObject msg = new JSONObject();
        JSONObject param = new JSONObject();
        msg.put("devEUI", dev_eui);
        msg.put("data", "\\x" + contents);
        msg.put("userSec", LoraConfig.userSec);
        msg.put("type", LoraConfig.lora_send_type);
//		msg.put("expire_time", "");//(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(datel))
        msg.put("request_id", LoraConfig.requestid);

        param.put("params", msg);
        logger.info("LoraSendCmdsUtils param=" + param);
        return param.toString();
    }

    public static String getGroupTimeContent(String groupid) throws JSONException {
        String loraStart = "FEFEFEFE";
        String id = "00";//服务序号
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
        String time16 = Utilty.convertByteToString(e_time, 1, e_time.length);
        System.out.println("time16=" + time16);
        dih_lon = dih_lon + time16;

        //将表号高低位互换
        byte[] e = Utilty.hexStringToBytes(e_num2);
//				System.out.println("length="+e.length);
        String enum16 = Utilty.convertByteToString(e, 1, e.length);
//				System.out.println("enum16="+enum16);
//				System.out.println("d_str="+ id+ prm +e_num2 + fac_id +dih_lon);
        CRC = CRC16.getCRC(id + prm + enum16 + fac_id + dih_lon).toUpperCase();
//				System.out.println("CRC="+CRC);
        String contents = loraStart + id + prm + enum16 + fac_id + dih_lon + CRC + end;
//				System.out.println("contents="+contents);

        JSONObject msg = new JSONObject();
        JSONObject param = new JSONObject();
        msg.put("groupId", groupid);
        msg.put("data", "\\x" + contents);

        param.put("params", msg);
        logger.info("LoraSendCmdsUtils param=" + param);
        return param.toString();
    }

    public Map<String, String> getEMInfoByDeveui(String dev_eui) {
        Map<String, String> map = new HashMap<String, String>();
        if (mIWMDataService == null) {
            mIWMDataService = SpringContextUtils.getBean(IWMDataService.class);
        }
        List<WMDataPo> list = mIWMDataService.selectbyDevId(dev_eui, 1, 20);
//		logger.info("--------------SendCmdsGetSignal---list.size=="+ list.size());
        if (list.size() >= 1) {
//			logger.info("--------------SendCmdsGetSignal---get e_num=="+ list.get(0).getE_num());
//			logger.info("--------------SendCmdsGetSignal---get e_fac=="+ list.get(0).getE_fac());
//			logger.info("--------------SendCmdsGetSignal---get imei=="+ list.get(0).getImei());
//			logger.info("--------------SendCmdsGetSignal---get ds_id=="+ list.get(0).getDs_id());
//			logger.info("--------------SendCmdsGetEMValue---get time=="+ list.get(0).getTime());
            map.put("dev_id", list.get(0).getDevId());
            map.put("wm_num", list.get(0).getWmNum());
            map.put("time", String.valueOf(list.get(0).getTime()));
            return map;
        } else {
            return null;
        }
    }

    public String getCmdContent(String meter_no, String data_type, String operate_type, String value) {
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
        switch (data_type) {
            case "wm_switch"://强制开关阀
                if ("O".equals(operate_type)) {//执行，操作
                    if ("on".equals(value)) {
                        dih_lon = "0404A0170155";//
                    } else if ("off".equals(value)) {
                        dih_lon = "0404A0170199";//
                    }
                } else if ("R".equals(operate_type)) {
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
        String enum16 = Utilty.convertByteToString(e, 1, e.length);
//		System.out.println("enum16="+enum16);
//		System.out.println("d_str="+ id+ prm +e_num2 + fac_id +dih_lon);
        CRC = CRC16.getCRC8(id + prm + enum16 + dih_lon).toUpperCase();
//		System.out.println("CRC="+CRC);
        String contents = lora_start + id + prm + enum16 + dih_lon + CRC + end;
//		System.out.println("contents="+contents);
        return contents;
    }
}
