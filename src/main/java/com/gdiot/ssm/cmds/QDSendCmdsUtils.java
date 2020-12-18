package com.gdiot.ssm.cmds;

import com.alibaba.fastjson.JSONObject;
import com.gdiot.ssm.http.yd.BasicResponse;
import com.gdiot.ssm.http.yd.CmdsResponse;
import com.gdiot.ssm.util.CRC16;
import com.gdiot.ssm.util.Utilty;
import com.gdiot.ssm.util.YDConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZhouHR
 */
@Slf4j
public class QDSendCmdsUtils {

    public QDSendCmdsUtils() {

    }

    public JSONObject SendMsgNB(String imei_qd, String content, String time) {
        String url = YDConfig.YD_EXECUTE_URL + "imei=" + imei_qd + "&obj_id=" + YDConfig.QD_OBJ_ID
                + "&obj_inst_id=" + YDConfig.QD_OBJ_INST_ID + "&res_id=" + YDConfig.QD_RES_ID;
        String api_key = YDConfig.QD_API_KEY;

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
        String time = msgMap.get("time");
        Map<String, Object> map = new HashMap<>();
        try {
            String content = getQDNBCmdMsg(type, operate_type, value, e_num);
            log.info("--------------getCmdsInfo---content==" + content);
            map.put("content", content);
            return map;
        } catch (Exception e) {
            log.error("e=" + e);
        }
        return map;
    }

    public static String getQDNBCmdMsg(String type, String operate_type, String value, String e_num) {
        //01F2002C5EA0190026140023 FA071304 FA01 277000080720 0013 1C00 68277000080720681C10CCBEAC02AB8967454D32CCCCCCCCCCCC2116 60FAFF 4C
        //起始符
        String start = "01F2002C5EA0190026140023";
        //设备编码
        String data = getCmdNB(type, operate_type, value, e_num);
        //CRC
        String CRC;

        String content;
        content = start + data;
        CRC = CRC16.getCRC8(content);
        content = content + CRC;
        return content;
    }

    //NB帧
    public static String getCmdNB(String type, String operate_type, String value, String e_num) {
        //FA071304 FA01 277000080720 0013 1C00 68277000080720681C10CCBEAC02AB8967454D32CCCCCCCCCCCC2116 60FAFF
        //68 277000080720 68 1C10 CCBEAC02 AB896745 4D 32 CCCCCCCCCCCC 21 16
        //起始符 68
        String start = "FA071304FA01";
        //通信地址：277000080720
        String dev_code = Utilty.convertByteToString(Utilty.hexStringToBytes(e_num), 1, Utilty.hexStringToBytes(e_num).length);

        //固定帧
        String code1 = "00131C00";

        //透传数据 68277000080720681C10CCBEAC02AB8967454D32CCCCCCCCCCCC2116
        String data = getCmdContent(type, operate_type, value, e_num);
        //CRC8校验
        String CRC;
        //结束符 16
        String end = "FAFF";

        String content;
        content = start + dev_code + code1 + data;

        CRC = CRC16.getCRC8(content);
        content = content + CRC + end;
        return content;
    }

    //透传数据组帧
    public static String getCmdContent(String type, String operate_type, String value, String e_num) {
        //68 277000080720 68 1C10 CCBEAC02 AB896745 4D 32 CCCCCCCCCCCC 21 16
        //起始符 68
        String start = "68";
        //通信地址：277000080720
        String dev_code = Utilty.convertByteToString(Utilty.hexStringToBytes(e_num), 1, Utilty.hexStringToBytes(e_num).length);

        //固定帧
        String code1 = "681C10CCBEAC02AB896745";
        //控制命令类型 4D
        String function_code = Utilty.convertByteToString2(Utilty.hexStringToBytes(type), 1, Utilty.hexStringToBytes(type).length);
        //固定帧
        String code2 = "32CCCCCCCCCCCC";
        //CRC8校验
        String CRC;
        //结束符 16
        String end = "16";

        String content;
        content = start + dev_code + code1 + function_code + code2;

        CRC = CRC16.getCRC8(content);
        content = content + CRC + end;
        return content;
    }

}