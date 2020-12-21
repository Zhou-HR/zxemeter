package com.gdiot.ssm.cmds;

import com.alibaba.fastjson.JSONObject;
import com.gdiot.model.EMCmdsSEQPo;
import com.gdiot.service.INBYDEMCmdsService;
import com.gdiot.service.IXBEMDataService;
import com.gdiot.ssm.http.yd.BasicResponse;
import com.gdiot.ssm.http.yd.CmdsResponse;
import com.gdiot.ssm.util.CRC16;
import com.gdiot.ssm.util.SpringContextUtils;
import com.gdiot.ssm.util.Utilty;
import com.gdiot.ssm.util.YDConfig;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZhouHR
 */
@Slf4j
public class QDSendCmdsUtils {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private INBYDEMCmdsService mINBYDEMCmdsService;
    private IXBEMDataService mIXBEMDataService;

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
        logger.info("send_cmd--errno=" + errno + ",error=" + error + ",data:" + jo.get("data"));
        logger.info("result=" + jo.toString());
        return jo;
    }

    public Map<String, Object> getCmdsInfo(Map<String, String> msgMap) {

        String module_type = msgMap.get("module_type");
        String imei = msgMap.get("imei");
        String type = msgMap.get("type");
        String value = msgMap.get("value");
        String operate_type = msgMap.get("operate_type");
        String request_id = msgMap.get("request_id");
        String e_num = msgMap.get("eNum");
        String time = msgMap.get("time");
        Map<String, Object> map = new HashMap<>();
        String new_seq_hex = getNewCmdSeq(e_num, imei);
        if (new_seq_hex != "") {
            try {
                String content = getQDNBCmdMsg(type, operate_type, value, e_num, new_seq_hex);
                logger.info("--------------getCmdsInfo---content==" + content);
                map.put("content", content);
                map.put("new_seq_hex", new_seq_hex);
                map.put("new_data_seq_hex", new_seq_hex);
                return map;
            } catch (Exception e) {
                logger.error("e=" + e);
            }
        }
        return map;
    }

    public static String getQDNBCmdMsg(String type, String operate_type, String value, String e_num, String new_seq_hex) {
        //01F2 002C 5EA0 190026140023 FA071304 FA01 277000080720 0013 1C00 68277000080720681C10CCBEAC02AB8967454D 32CCCCCCCCCCCC2116 60FAFF 4C
        //起始符
        String start = "01F2002C" + new_seq_hex + "190026140023";
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
        //68 277000080720 68 1C10 02ACBECC AB896745 4D 32 CCCCCCCCCCCC 21 16
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
        //68 277000080720 68 1C10 35DFF1FF AB896745 4D 32 CCCCCCCCCCCC 21 16
        //起始符 68
        String start = "68";
        //通信地址：277000080720
        String dev_code = Utilty.convertByteToString(Utilty.hexStringToBytes(e_num), 1, Utilty.hexStringToBytes(e_num).length);

        //固定帧
        String code1 = "681C1035DFF1FFAB896745";
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

    private String getCmdsSeqByEnum(String e_num, String imei) {
        if (mINBYDEMCmdsService == null) {
            mINBYDEMCmdsService = SpringContextUtils.getBean(INBYDEMCmdsService.class);
        }
        List<EMCmdsSEQPo> list = mINBYDEMCmdsService.selectcmdseq(imei);
        if (list.size() > 0) {
            //数据库中存在,读出数据
            String cmd_seq = list.get(0).getCmd_seq();
            return cmd_seq;
        } else {
            //数据库中没有，就插入，从40开始
            EMCmdsSEQPo mNBYDEMCmdsPo = new EMCmdsSEQPo();
            mNBYDEMCmdsPo.setE_num(e_num);
            mNBYDEMCmdsPo.setImei(imei);
            mNBYDEMCmdsPo.setCmd_seq("0000");
            mNBYDEMCmdsPo.setData_seq("0000");
            mNBYDEMCmdsPo.setCreate_time(new Date(System.currentTimeMillis()));
            int intodb = mINBYDEMCmdsService.insertcmdseq(mNBYDEMCmdsPo);
            logger.info("new imei insert int em_cmds_seq-----intodb=" + intodb);
            return "0000";
        }
    }

    public String getNewCmdSeq(String e_num, String imei) {
        String seq_hex = getCmdsSeqByEnum(e_num, imei);
        if (seq_hex.isEmpty()) {
            return "";
        }
        //服务序号
        String seq_hex_new = "";

        //是16进制数
        String regex = "^[A-Fa-f0-9]+$";

        if (seq_hex.matches(regex)) {
            int seq_dec = Integer.parseInt(seq_hex, 16);
            int max = 65535;
            if (seq_dec == max) {
                seq_dec = 0;
            } else if (seq_dec >= 0 && seq_dec < max) {
                seq_dec++;
            }

            seq_hex_new = String.format("%04x", seq_dec).toUpperCase();
            logger.info("计算好的新的下行序列号-----------seq_hex_new=" + seq_hex_new);
        }
        return seq_hex_new;
    }

}