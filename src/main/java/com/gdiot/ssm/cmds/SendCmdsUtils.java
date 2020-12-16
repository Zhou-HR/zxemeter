package com.gdiot.ssm.cmds;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gdiot.model.EMCmdsSEQPo;
import com.gdiot.model.XBEMDataPo;
import com.gdiot.service.INBYDEMCmdsService;
import com.gdiot.service.IXBEMDataService;
import com.gdiot.ssm.util.CRC16;
import com.gdiot.ssm.util.SpringContextUtils;
import com.gdiot.ssm.util.Utilty;

/**
 * @author ZhouHR
 */
public class SendCmdsUtils {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private INBYDEMCmdsService mINBYDEMCmdsService;
    private IXBEMDataService mIXBEMDataService;

    public SendCmdsUtils() {

    }

    public Map<String, String> getEMInfoByImei(String module_type, String imei) {
        Map<String, String> map = new HashMap<>();

        //16 dev_eui
        String regex_dev = "^[A-Fa-f0-9]+$";

        //imei 15
        String regex_imei = "^\\d{15}$";
        if (mIXBEMDataService == null) {
            mIXBEMDataService = SpringContextUtils.getBean(IXBEMDataService.class);
        }
        if (imei.matches(regex_imei) || imei.matches(regex_dev)) {
            List<XBEMDataPo> list = mIXBEMDataService.selectOne(imei, -1, module_type);
//			logger.info("--------------SendCmdsGetSignal---list.size=="+ list.size());
            if (list.size() >= 1) {
                logger.info("--------------SendCmdsGetSignal---get e_num==" + list.get(0).getE_num());
                logger.info("--------------SendCmdsGetSignal---get e_fac==" + list.get(0).getE_fac());
                logger.info("--------------SendCmdsGetSignal---get imei==" + list.get(0).getDev_id());
                logger.info("--------------SendCmdsGetEMValue---get time==" + list.get(0).getTime());
                map.put("imei", list.get(0).getDev_id());
                map.put("e_num", list.get(0).getE_num());
                map.put("e_fac", list.get(0).getE_fac());
                map.put("time", String.valueOf(list.get(0).getTime()));
                return map;
            }
        } else {
            logger.error("imei error");
            return null;
        }
        return map;
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

//		logger.info("--------------getCmdsInfo---imei=="+imei);

        Map<String, Object> map = new HashMap<>();
        String new_seq_hex = getNewCmdSeq(module_type, e_num, imei);
//		logger.info("--------------getCmdsInfo---new_seq_hex=="+ new_seq_hex);
        if (new_seq_hex != "") {
//			logger.info("--------------getCmdContent start----------");
//			logger.info("--------------getCmdContent start----------module_type="+module_type);
//			logger.info("--------------getCmdContent start----------new_seq_hex="+new_seq_hex);
//			logger.info("--------------getCmdContent start----------e_num="+e_num);
//			logger.info("--------------getCmdContent start----------fac_id="+fac_id);
//			logger.info("--------------getCmdContent start----------type="+type);
//			logger.info("--------------getCmdContent start----------operate_type="+operate_type);
            try {
                String content = getCmdContent(module_type, new_seq_hex, e_num, fac_id, type, operate_type, value);
                logger.info("--------------getCmdsInfo---content==" + content);
                map.put("content", content);
                map.put("new_seq_hex", new_seq_hex);
                map.put("new_data_seq_hex", getDataSeq(new_seq_hex).toUpperCase());
                return map;
            } catch (Exception e) {
                logger.error("e=" + e);
            }
        }
        return map;
    }

    /**
     * 下行指令组帧
     *
     * @param module_type
     * @param ser_id
     * @param eNum
     * @param fac
     * @param data_type
     * @param operate_type
     * @param seq
     * @return
     */
    private String getCmdContent(String module_type, String ser_id, String eNum, String fac, String data_type, String operate_type, String seq) {
//		logger.info("--------------getCmdContent start-----002-----");
        String lora_start = "FEFEFEFE";

        //服务序号
        String id = ser_id;

        //启动方向    01抄表，02设置，03 拉合闸
        String prm = "01";

        //"190300000064";//表号
        String e_num2 = eNum;

        //厂商标识
        String fac_id = fac;

        //FF03103141 请求数据类型 长度 数据  类型包含
        String dih_lon = "";
        String CRC;
        String end = "16";
        if ("R".equals(operate_type)) {
            //读
            prm = "01";
        } else if ("W".equals(operate_type)) {
            //写
            prm = "02";
        } else if ("O".equals(operate_type)) {
            //执行，操作
            prm = "03";
        } else if ("D".equals(operate_type)) {
            //读冻结数据
            prm = "0D";
        } else if ("E".equals(operate_type)) {
            //写多项
            prm = "0E";
        } else if ("F".equals(operate_type)) {
            //写多项
            prm = "0F";
        }
        switch (data_type) {
            //抄表
            case "10":
                //有功总电能 //C0014700000003190010050000000000F916

            case "15":
                //无功总电能
                if ("W".equals(operate_type)) {
                    //写//seq>=360 && seq<86400
                    if ((Double.parseDouble(seq) >= 0)) {
                        dih_lon = data_type + "04" + getKWHHex(seq, 4);
                    }
                } else if ("R".equals(operate_type)) {
                    //读上报时间
                    dih_lon = data_type + "00";
                }
                break;
//		case "11"://A相有功电能 //C0014700000003190010050000000000F916
//		case "12"://B相有功电能 //C0014700000003190010050000000000F916
//		case "13"://C相有功电能 //C0014700000003190010050000000000F916
            case "14"://有功余脉冲数
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
            case "32"://B相电压 //C00147000000031900310258226116
            case "33"://C相电压 //C00147000000031900310258226116
            case "3F"://电压读块
            case "41"://A相电流 //C001470000000319004103070000C316
            case "42"://B相电流 //
            case "43"://C相电流 //
            case "4F"://电流读块
            case "50"://总功率因数
            case "51"://A相功率因数
            case "52"://B相功率因数
            case "53"://C相功率因数
            case "5F"://功率因数读块
            case "60"://频率
            case "70"://时间
            case "71"://注册标识
            case "73"://认证时长
            case "80"://电表运行状态字1
            case "81"://电表运行状态字2
            case "83"://电表常数
            case "90"://厂家标识
            case "91"://硬件版本号
            case "92"://软件版本号
            case "93"://协议版本号
            case "95"://通讯号
            case "96"://用户号
                dih_lon = data_type + "00";
                break;
            case "74"://心跳间隔
                if ("W".equals(operate_type)) {//写//seq>=25 && seq<1800
                    if ((Long.parseLong(seq) >= 25) && (Long.parseLong(seq) <= 3600)) {
                        dih_lon = data_type + "03" + getTimesHex(seq, 3);
                    }
                } else if ("R".equals(operate_type)) {//心跳间隔
                    dih_lon = data_type + "00";
                }
                break;
            case "72"://上报时间
                if ("W".equals(operate_type)) {//写//seq>=360 && seq<86400
                    if ((Long.parseLong(seq) >= 360) && (Long.parseLong(seq) < 86400)) {
                        dih_lon = data_type + "03" + getTimesHex(seq, 3);
                    }
                } else if ("R".equals(operate_type)) {//读上报时间
                    dih_lon = data_type + "00";
                }
                break;
            case "82"://继电器控制字
                if ("O".equals(operate_type)) {//执行，操作
                    if ("on".equals(seq)) {
                        dih_lon = "8201AA";//
                    } else if ("off".equals(seq)) {
                        dih_lon = "820155";//
                    }
                } else {
                    dih_lon = data_type + "00";//读继电器值
                }
                break;
            case "94"://表号
                if ("W".equals(operate_type)) {
                    //将表号高低位互换
                    byte[] seq_b = Utilty.hexStringToBytes(seq);
                    String new_dev_num = Utilty.convertByteToString(seq_b, 1, seq_b.length);
                    System.out.println("new_dev_num=" + new_dev_num);
                    dih_lon = "9406" + new_dev_num;//
                } else if ("R".equals(operate_type)) {//读上报时间
                    dih_lon = data_type + "00";//
                }
                break;
            //冻结次数
            case "A0"://整点冻结次数 //C00147000000031900A0032802004116
            case "A2"://日冻结次数 //C00147000000031900A20203006716
            case "A4"://月冻结次数 //C00147000000031900A401016816
                //事件次数
            case "B0"://停电事件总次数 //C00147000000031900B00203005916
            case "B2"://拉合闸总次数 //C00147000000031900B20209005116
            case "B4"://过流总数 //C00147000000031900B40200005816
            case "B6"://过压事件总数 //C00147000000031900B60200005616
            case "B8"://欠压事件次数 //C00147000000031900B80200005416
            case "BA"://模块重启次数 //C00147000000031900BA0200005216
            case "BC"://编程记录次数
                dih_lon = data_type + "00";
                break;
            //事件数据
            case "A1"://整点冻结数据 最多纪录96次
                if (Integer.parseInt(seq) >= 0) {
                    dih_lon = data_type + "03" + getTimesHex(seq, 3);//
                }
                break;
            case "A3"://第几次日冻结数据 最多纪录62次
            case "A5"://第几次月冻结数据  最多纪录12次
                if (Integer.parseInt(seq) >= 0) {
                    dih_lon = data_type + "02" + getTimesHex(seq, 2);//
                }
                break;
            //事件数据
            case "B1"://停电事件
            case "B3"://拉合闸事件 //C0034700000003190082008A16
            case "B5"://过流事件
            case "B7"://过压事件
            case "B9"://欠压事件
            case "BB"://重启记录最近十次
            case "BD"://编程记录十次
                if (Integer.parseInt(seq) >= 0) {
                    dih_lon = data_type + "02" + getTimesHex(seq, 2);//
                }
                break;
            case "F0"://开启工厂模式
                if ("O".equals(operate_type)) {
                    if ("on".equals(seq)) {//进工厂模式
                        dih_lon = "F001aa";
                    } else if ("off".equals(seq)) {//退工厂模式
                        dih_lon = "F00155";
                    }
                } else if ("R".equals(operate_type)) {
                    dih_lon = data_type + "00";
                    break;
                }
                break;
            case "F1"://读RAM的XXXXXXXX地址后的N字节
            case "F2"://读ROM的XXXXXXXX地址后的N字节
            case "F3"://读EEPROM的XXXXXXXX地址后的N字节
            case "F4"://读计量芯片指定寄存器PP页，RR寄存器地址数据
            case "F5"://IMSI ASCII
            case "F6"://IMEI  ASCII
            case "F7"://ICCID ASCII
            case "F8"://信号强度
            case "F9"://驻网状态
            case "FA"://模块监控状态字
                dih_lon = data_type + "00";
                break;
            default:
                break;
        }

        //将表号高低位互换
        byte[] e = Utilty.hexStringToBytes(e_num2);
//		System.out.println("length="+e.length);
        String enum16 = Utilty.convertByteToString(e, 1, e.length);
        System.out.println("enum16=" + enum16);
        System.out.println("d_str=" + id + prm + e_num2 + fac_id + dih_lon);
        CRC = CRC16.getCRC(id + prm + enum16 + fac_id + dih_lon).toUpperCase();

        System.out.println("CRC=" + CRC);
//		logger.info("--------------getCmdContent start-----003-----");
        String contents = id + prm + enum16 + fac_id + dih_lon + CRC + end;
        if ("lora".equals(module_type) || "lora0".equals(module_type)) {
//			logger.info("--------------getCmdContent start-----004-----");
            contents = lora_start + id + prm + enum16 + fac_id + dih_lon + CRC + end;
        } else {
//			logger.info("--------------getCmdContent start-----005-----");
            contents = id + prm + enum16 + fac_id + dih_lon + CRC + end;
        }
//		System.out.println("contents="+contents);
        return contents;
    }

    /**
     * double----*100-----高位补零------高低位互换
     *
     * @param kwh
     * @param len
     * @return
     */
    private static String getKWHHex(String kwh, int len) {
        //序列号高位补0
        DecimalFormat df = null;
        if (len == 4) {
            df = new DecimalFormat("00000000");
        }
        String seq_s = df.format(Double.parseDouble(kwh) * 100);
        //将序列号高低位互换
        byte[] seq_b = Utilty.hexStringToBytes(seq_s);
        String seq16 = Utilty.convertByteToString(seq_b, 1, seq_b.length);
        System.out.println("kwh seq16=" + seq16);
        return seq16;
    }

    private String getTimesHex(String times, int len) {
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


    private String getDataSeq(String cmd_seq) {
        if (cmd_seq.isEmpty()) {
            return "";
        }
        String data_seq_hex = "";
        //服务序号
        String regex = "^[A-Fa-f0-9]+$";//是16进制数
        if (cmd_seq.matches(regex)) {
            //十进制：64--127
            //十六进制：40--7F
            int seq_dec = Integer.parseInt(cmd_seq, 16);
            seq_dec = seq_dec + 128;
            data_seq_hex = Integer.toHexString(seq_dec);
//			System.out.println("计算好的新的上报序列号-----------data_seq_hex="+data_seq_hex);
        }
        return data_seq_hex;
    }

    //	private int updateDBCmdsSeq(EMCmdsSEQPo mNBYDEMCmdsPo) {
////		logger.info("从更新数据库序列号-------------------");
//		if(mINBYDEMCmdsService == null) {
//			mINBYDEMCmdsService =  SpringContextUtils.getBean(INBYDEMCmdsService.class);
//		}
//		int result = mINBYDEMCmdsService.updatecmdseq(mNBYDEMCmdsPo);
//		logger.info("update cmds em_cmds_seq-----result=" + result);
//		return result;
//	}
    private String getCmdsSeqByEnum(String module_type, String e_num, String imei) {
//		logger.info("根据表号查询序列号-------------------");
        if (mINBYDEMCmdsService == null) {
            mINBYDEMCmdsService = SpringContextUtils.getBean(INBYDEMCmdsService.class);
        }
        List<EMCmdsSEQPo> list = mINBYDEMCmdsService.selectcmdseq(imei);
        if (list.size() > 0) {//数据库中存在,读出数据
//			logger.info("根据表号查询序列号------------------list.size()="+list.size());
//			logger.info("根据表号查询序列号------------------enum="+list.get(0).getE_num());
//			logger.info("根据表号查询序列号------------------imei="+list.get(0).getImei());
//			logger.info("根据表号查询序列号------------------cmd_seq="+list.get(0).getCmd_seq());
            String cmd_seq = list.get(0).getCmd_seq();
            return cmd_seq;
        } else {//数据库中没有，就插入，从40开始
            EMCmdsSEQPo mNBYDEMCmdsPo = new EMCmdsSEQPo();
            mNBYDEMCmdsPo.setE_num(e_num);
            mNBYDEMCmdsPo.setImei(imei);
            mNBYDEMCmdsPo.setCmd_seq("40");
            mNBYDEMCmdsPo.setData_seq(getDataSeq("40").toUpperCase());
            mNBYDEMCmdsPo.setCreate_time(new Date(System.currentTimeMillis()));
            int intodb = mINBYDEMCmdsService.insertcmdseq(mNBYDEMCmdsPo);
            logger.info("new imei insert int em_cmds_seq-----intodb=" + intodb);
            return "40";
        }
    }

    public String getNewCmdSeq(String module_type, String e_num, String imei) {
//		logger.info("从数据库读序列号-------------------");
        String seq_hex = getCmdsSeqByEnum(module_type, e_num, imei);
        if (seq_hex.isEmpty()) {
            return "";
        }
        String seq_hex_new = "";
        //服务序号
        String regex = "^[A-Fa-f0-9]+$";//是16进制数
        if (seq_hex.matches(regex)) {
            //十进制：64--127
            //十六进制：40--7F
            int seq_dec = Integer.parseInt(seq_hex, 16);
            if (seq_dec == 127) {
                seq_dec = 64;//新一轮循环开始
            } else if (seq_dec >= 64 && seq_dec < 127) {
                seq_dec++;
            }
            seq_hex_new = Integer.toHexString(seq_dec).toUpperCase();
            logger.info("计算好的新的下行序列号-----------seq_hex_new=" + seq_hex_new);
        }
        return seq_hex_new;
    }
}
