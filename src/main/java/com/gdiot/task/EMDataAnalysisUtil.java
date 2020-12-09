package com.gdiot.task;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gdiot.model.NBDXDataPo;
import com.gdiot.util.Utilty;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhouHR
 */
@Slf4j
public class EMDataAnalysisUtil {

//	private static final Logger log =  LoggerFactory.getLogger(DataSenderTask.class);

    /**
     * 用于解析电表上报数据，芯北电表协议 单相电表
     *
     * @param data3 需要解析的数据域
     * @return
     */
    public static Map<String, String> getDataValue(String data3) {
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0, j = data3.length(); i < j; ) {
//			System.out.print("---------i_str="+i);
            String typeStr = data3.substring(i, i + 2);
            String lenStr = data3.substring(i + 2, i + 4);
//			LOGGER.info("---------typeStr="+typeStr);
//			LOGGER.info("---------lenStr="+lenStr);
            int length = Integer.valueOf(lenStr, 16);
            String dataStr = data3.substring(i + 4, i + 4 + length * 2);
//			LOGGER.info("---------length="+length);
//			LOGGER.info("---------dataStr="+dataStr);

            byte[] data_byte = Utilty.hexStringToBytes(dataStr);
            String data = Utilty.convertByteToString(data_byte, 1, data_byte.length);
//			System.out.print("---------data_byte="+data_byte);
//			LOGGER.info("---------data="+data);
            switch (typeStr) {
                case "A0"://序列号 //整点冻结次数
                case "A2"://日冻结次数
                case "A4"://月冻结次数 //A4020400
                    map.put("seq_type", String.valueOf(typeStr));
                    if (Utilty.isNumeric(data)) {
                        int e_seq = Integer.parseInt(data);
                        log.info("data receive: 序列号:" + e_seq);
                        map.put("e_seq", String.valueOf(e_seq));
                    }
                    break;
                case "70"://time
                    if (Utilty.isNumeric(data)) {
                        log.info("data receive: 时间:" + data);
                        map.put("e_time", String.valueOf(data));
                    }
                    break;
                case "F8"://信号值
                    if (Utilty.isNumeric(data)) {
                        if (length == 1) {
                            BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0").format(Double.valueOf(data)));
                            log.info("data receive: 信号值:" + mBigDecimal);
                            map.put("e_signal", mBigDecimal.toString());
                        } else if (length == 2) {//F8 02 32 32
                            String signal = Utilty.hexStringToString(dataStr);
                            log.info("data receive: 信号值:" + signal);
                            map.put("e_signal", signal);
                        }
                    }
                    break;
                case "10"://有功总电量
                    if (Utilty.isNumeric(data)) {
                        BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(data) / 100));
                        log.info("data receive: 有功总电量:" + mBigDecimal);
                        map.put("e_kwh1", mBigDecimal.toString());
                    }
                    break;
                case "20"://有功总功率
                    if (Utilty.isNumeric(data)) {
                        BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.0000").format(Double.valueOf(data) / 10000));
                        log.info("data receive: 总有功功率:" + mBigDecimal);
                        map.put("e_kw1_all", mBigDecimal.toString());
                    }
                    break;
                case "24"://无功总功率
                    if (Utilty.isNumeric(data)) {
                        BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.0000").format(Double.valueOf(data) / 10000));
                        log.info("data receive: 总无功功率:" + mBigDecimal);
                        map.put("e_kw2_all", mBigDecimal.toString());
                    }
                    break;
                case "31"://电压
                    if (Utilty.isNumeric(data)) {
                        BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#00.0").format(Double.valueOf(data) / 10));
                        log.info("data receive: 电压:" + mBigDecimal);
                        map.put("e_voltage_a", mBigDecimal.toString());
                    }
                    break;
                case "41"://电流
                    if (Utilty.isNumeric(data)) {
                        BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(data) / 1000));
                        log.info("data receive: 电流:" + mBigDecimal);
                        map.put("e_current_a", mBigDecimal.toString());
                    }
                    break;
                case "50"://总功率因数
                    if (Utilty.isNumeric(data)) {
                        BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("0.000").format(Double.valueOf(data) / 1000));
                        log.info("data receive: 总功率因数:" + mBigDecimal);
                        map.put("e_factor_all", mBigDecimal.toString());
                    }
                    break;
                case "80"://状态字1
                    map.put("e_statu1", data);
                    log.info("data receive: 状态字1:" + data);
                    break;
                case "81"://状态字2
                    map.put("e_statu2", data);
                    log.info("data receive: 状态字2:" + data);
                    break;
                case "82"://继电器
                    map.put("e_switch", data);
                    log.info("data receive: 继电器:" + data);
                    break;

                case "1F"://电能
                    if (Utilty.isNumeric(data)) {//前后顺序已变更
                        String kwh2_str = data.substring(0, 8);
                        String kwh1_str = data.substring(8, data.length());
                        BigDecimal mBigDecimal_kwh1 = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(kwh1_str) / 100));
                        log.info("data receive: 有功总电量:" + mBigDecimal_kwh1);
                        BigDecimal mBigDecimal_kwh2 = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(kwh2_str) / 100));
                        log.info("data receive: 无功总电量:" + mBigDecimal_kwh2);

                        map.put("e_kwh1", mBigDecimal_kwh1.toString());//有功电能
                        map.put("e_kwh2", mBigDecimal_kwh2.toString());//无功电能
                    }
                    break;
                case "2F"://功率 弃用
                    if (Utilty.isNumeric(data)) {
                        String kw2_c = data.substring(0, 6);
                        String kw2_b = data.substring(6, 12);
                        String kw2_a = data.substring(12, 18);
                        String kw2_all = data.substring(18, 24);

                        String kw1_c = data.substring(24, 30);
                        String kw1_b = data.substring(30, 36);
                        String kw1_a = data.substring(36, 42);
                        String kw1_all = data.substring(42, 48);

                        BigDecimal mbd_kw1_all = new BigDecimal(new DecimalFormat("#0.0000").format(Double.valueOf(kw1_all) / 10000));
                        BigDecimal mbd_kw1_a = new BigDecimal(new DecimalFormat("#0.0000").format(Double.valueOf(kw1_a) / 10000));
                        BigDecimal mbd_kw1_b = new BigDecimal(new DecimalFormat("#0.0000").format(Double.valueOf(kw1_b) / 10000));
                        BigDecimal mbd_kw1_c = new BigDecimal(new DecimalFormat("#0.0000").format(Double.valueOf(kw1_c) / 10000));
                        BigDecimal mbd_kw2_all = new BigDecimal(new DecimalFormat("#0.0000").format(Double.valueOf(kw2_all) / 10000));
                        BigDecimal mbd_kw2_a = new BigDecimal(new DecimalFormat("#0.0000").format(Double.valueOf(kw2_a) / 10000));
                        BigDecimal mbd_kw2_b = new BigDecimal(new DecimalFormat("#0.0000").format(Double.valueOf(kw2_b) / 10000));
                        BigDecimal mbd_kw2_c = new BigDecimal(new DecimalFormat("#0.0000").format(Double.valueOf(kw2_c) / 10000));
                        log.info("data receive: 总有功功率:" + mbd_kw1_all.toString());
                        log.info("data receive: 有功功率a:" + mbd_kw1_a.toString());
                        log.info("data receive: 有功功率b:" + mbd_kw1_b.toString());
                        log.info("data receive: 有功功率c:" + mbd_kw1_c.toString());
                        log.info("data receive: 总无功功率:" + mbd_kw2_all.toString());
                        log.info("data receive: 无功功率a:" + mbd_kw2_a.toString());
                        log.info("data receive: 无功功率b:" + mbd_kw2_b.toString());
                        log.info("data receive: 无功功率c:" + mbd_kw2_c.toString());
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
                    if (Utilty.isNumeric(data)) {
                        String v_c = data.substring(0, 4);
                        String v_b = data.substring(4, 8);
                        String v_a = data.substring(8, 12);

                        BigDecimal mbd_v_a = new BigDecimal(new DecimalFormat("#00.0").format(Double.valueOf(v_a) / 10));
                        BigDecimal mbd_v_b = new BigDecimal(new DecimalFormat("#00.0").format(Double.valueOf(v_b) / 10));
                        BigDecimal mbd_v_c = new BigDecimal(new DecimalFormat("#00.0").format(Double.valueOf(v_c) / 10));
                        log.info("data receive: A电压:" + mbd_v_a.toString());
                        log.info("data receive: B电压:" + mbd_v_b.toString());
                        log.info("data receive: C电压:" + mbd_v_c.toString());

                        map.put("e_voltage_a", mbd_v_a.toString());
                        map.put("e_voltage_b", mbd_v_b.toString());
                        map.put("e_voltage_c", mbd_v_c.toString());
                    }
                    break;
                case "4F"://电流 00 00 01 00 00 01 00 00 01
                    if (Utilty.isNumeric(data)) {
                        String c_c = data.substring(0, 6);
                        String c_b = data.substring(6, 12);
                        String c_a = data.substring(12, 18);

                        BigDecimal mbd_c_a = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(c_a) / 1000));
                        BigDecimal mbd_c_b = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(c_b) / 1000));
                        BigDecimal mbd_c_c = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(c_c) / 1000));
                        log.info("data receive: A电流:" + mbd_c_a);
                        log.info("data receive: B电流:" + mbd_c_b);
                        log.info("data receive: C电流:" + mbd_c_c);
                        map.put("e_current_a", mbd_c_a.toString());
                        map.put("e_current_b", mbd_c_b.toString());
                        map.put("e_current_c", mbd_c_c.toString());
                    }
                    break;
                case "5F"://功率因数  弃用
                    if (Utilty.isNumeric(data)) {
                        String factor_c = data.substring(0, 4);
                        String factor_b = data.substring(4, 8);
                        String factor_a = data.substring(8, 12);
                        String factor_all = data.substring(12, 16);

                        BigDecimal mbd_f_all = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(factor_all) / 1000));
                        BigDecimal mbd_f_a = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(factor_a) / 1000));
                        BigDecimal mbd_f_b = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(factor_b) / 1000));
                        BigDecimal mbd_f_c = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(factor_c) / 1000));
                        log.info("data receive: 总功率因数:" + mbd_f_all);
                        log.info("data receive: A功率因数:" + mbd_f_a);
                        log.info("data receive: B功率因数:" + mbd_f_b);
                        log.info("data receive: C功率因数:" + mbd_f_c);
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
            i = i + 4 + length * 2;
//			System.out.print("---------i_end="+i);
//			System.out.print("\n");
        }
        return map;
    }

    /**
     * 用于解析电表上报事件，芯北电表协议
     *
     * @param data3 需要解析的数据域
     * @return
     */
    public static Map<String, String> getEventDataValue(String data3) {
        Map<String, String> map = new HashMap<String, String>();
        int seq_count = 1;
        int time_count = 1;
        int kwh_count = 1;
        int current_count = 1;
        int voltage_count = 1;
        int status1_count = 1;
        int status2_count = 1;
        int relay_count = 1;
        int module_count = 1;
        for (int i = 0, j = data3.length(); i < j; ) {
//			System.out.print("--i_str="+i);
            String typeStr = data3.substring(i, i + 2);
            String lenStr = data3.substring(i + 2, i + 4);
//			System.out.print("---------typeStr="+typeStr);
//			System.out.print("---------lenStr="+lenStr);
            int length = Integer.valueOf(lenStr, 16);
            String dataStr = data3.substring(i + 4, i + 4 + length * 2);
//			System.out.print("---------length="+length);
//			System.out.print("---------dataStr="+dataStr);

            byte[] data_byte = Utilty.hexStringToBytes(dataStr);
            String data = "0";
            if (data_byte != null) {
                data = Utilty.convertByteToString(data_byte, 1, data_byte.length);
            }
//			System.out.print("---------data_byte="+data_byte);
//			LOGGER.info("---------data="+data);
//			map.put("event_type", typeStr);
            switch (typeStr) {
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
                    if (Utilty.isNumeric(data)) {
                        if (seq_count == 1) {
                            log.info("--data receive: start序列号:" + Integer.parseInt(data));
                            seq_count++;
                        } else if (seq_count == 2) {
                            log.info("--data receive: end序列号:" + Integer.parseInt(data));
                        }
                        map.put("e_seq", String.valueOf(Integer.parseInt(data)));
                    }
                    break;
                case "70"://time
                    if (Utilty.isNumeric(data)) {
                        if (time_count == 1) {
                            log.info("--data receive: start 时间:" + data);
                            map.put("start_time", data);
                            time_count++;
                        } else if (time_count == 2) {
                            log.info("--data receive: end 时间:" + data);
                            map.put("end_time", data);
                            time_count++;
                        }
                    }
                    break;
                case "10"://有功总电量
                    if (Utilty.isNumeric(data)) {
                        if (kwh_count == 1) {
                            BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(data) / 100));
                            map.put("start_kwh1", mBigDecimal.toString());
                            log.info("--data receive: start 有功总电量:" + mBigDecimal);
                            kwh_count++;
                        } else if (kwh_count == 2) {
                            BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(data) / 100));
                            map.put("end_kwh1", mBigDecimal.toString());
                            log.info("--data receive: end 有功总电量:" + mBigDecimal);
                        }
                    }
                    break;
                case "31"://电压
                    if (Utilty.isNumeric(data)) {
                        if (voltage_count == 1) {
                            BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#00.0").format(Double.valueOf(data) / 10));
                            log.info("--data receive: start 电压:" + mBigDecimal);
                            map.put("start_voltage_a", mBigDecimal.toString());
                            voltage_count++;
                        } else if (voltage_count == 2) {
                            BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#00.0").format(Double.valueOf(data) / 10));
                            log.info("--data receive: start 电压:" + mBigDecimal);
                            map.put("end_voltage_a", mBigDecimal.toString());
                        }
                    }
                    break;
                case "41"://电流
                    if (Utilty.isNumeric(data)) {
                        if (current_count == 1) {
                            BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(data) / 1000));
                            log.info("--data receive: start 电流:" + mBigDecimal);
                            map.put("start_current_a", mBigDecimal.toString());
                            current_count++;
                        } else if (current_count == 2) {
                            BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(data) / 1000));
                            log.info("--data receive: end 电流:" + mBigDecimal);
                            map.put("end_current_a", mBigDecimal.toString());
                            current_count++;
                        }
                    }
                    break;
                case "80"://电表状态字1
                    if (status1_count == 1) {
                        log.info("--data receive: start 电表状态字1:" + data);
                        map.put("start_status1", data);
                        status1_count++;
                    } else if (status1_count == 2) {
                        log.info("--data receive: end 电表状态字1:" + data);
                        map.put("end_status1", data);
                        status1_count++;
                    }
                    break;
                case "81"://电表状态字2
                    if (status2_count == 1) {
                        log.info("--data receive: start 电表状态字2:" + data);
                        map.put("start_status2", data);
                        status2_count++;
                    } else if (status2_count == 2) {
                        log.info("--data receive: end 电表状态字2:" + data);
                        map.put("end_status2", data);
                        status2_count++;
                    }
                    break;
                case "82"://继电器
                    if (relay_count == 1) {
                        log.info("--data receive: start 继电器:" + data);
                        map.put("start_switch", data);
                        relay_count++;
                    } else if (relay_count == 2) {
                        log.info("--data receive: end 继电器:" + data);
                        map.put("end_switch", data);
                        relay_count++;
                    }
                    break;
                case "FA"://模块监控状态字
                    if (module_count == 1) {
                        log.info("--data receive: start 模块监控状态字:" + data);
                        map.put("start_module", data);
                        module_count++;
                    } else if (module_count == 2) {
                        log.info("--data receive: end 模块监控状态字:" + data);
                        map.put("end_module", data);
                        module_count++;
                    }
                    break;
                //DA0D36000000031900BB22BA0298887006485701220719FA030503FFBA0298887006030002220719FA0304FFFF4C16

                case "1F"://电能
                    if (Utilty.isNumeric(data)) {
                        if (kwh_count == 1) {
                            String kwh2_str = data.substring(0, 8);
                            String kwh1_str = data.substring(8, data.length());
                            BigDecimal mBigDecimal_kwh1 = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(kwh1_str) / 100));
                            log.info("data receive: 有功总电量:" + mBigDecimal_kwh1);
                            BigDecimal mBigDecimal_kwh2 = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(kwh2_str) / 100));
                            log.info("data receive: 无功总电量:" + mBigDecimal_kwh2);
                            map.put("start_kwh1", mBigDecimal_kwh1.toString());//有功电能
                            map.put("start_kwh2", mBigDecimal_kwh2.toString());//无功电能

                            kwh_count++;
                        } else if (kwh_count == 2) {
                            String kwh2_str = data.substring(0, 8);
                            String kwh1_str = data.substring(8, data.length());
                            BigDecimal mBigDecimal_kwh1 = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(kwh1_str) / 100));
                            log.info("data receive: 有功总电量:" + mBigDecimal_kwh1);
                            BigDecimal mBigDecimal_kwh2 = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(kwh2_str) / 100));
                            log.info("data receive: 无功总电量:" + mBigDecimal_kwh2);

                            map.put("end_kwh1", mBigDecimal_kwh1.toString());//有功电能
                            map.put("end_kwh2", mBigDecimal_kwh2.toString());//无功电能
                            kwh_count++;
                        }
                    }
                    break;
                case "3F"://电压 //00 22 00 22 00 22
                    if (Utilty.isNumeric(data)) {
                        if (voltage_count == 1) {
                            String v_c = data.substring(0, 4);
                            String v_b = data.substring(4, 8);
                            String v_a = data.substring(8, 12);

                            BigDecimal mbd_v_a = new BigDecimal(new DecimalFormat("#00.0").format(Double.valueOf(v_a) / 10));
                            BigDecimal mbd_v_b = new BigDecimal(new DecimalFormat("#00.0").format(Double.valueOf(v_b) / 10));
                            BigDecimal mbd_v_c = new BigDecimal(new DecimalFormat("#00.0").format(Double.valueOf(v_c) / 10));
                            log.info("data receive: A电压:" + mbd_v_a.toString());
                            log.info("data receive: B电压:" + mbd_v_b.toString());
                            log.info("data receive: C电压:" + mbd_v_c.toString());

                            map.put("start_voltage_a", mbd_v_a.toString());
                            map.put("start_voltage_b", mbd_v_b.toString());
                            map.put("start_voltage_c", mbd_v_c.toString());

                            voltage_count++;
                        } else if (voltage_count == 2) {
                            String v_c = data.substring(0, 4);
                            String v_b = data.substring(4, 8);
                            String v_a = data.substring(8, 12);

                            BigDecimal mbd_v_a = new BigDecimal(new DecimalFormat("#00.0").format(Double.valueOf(v_a) / 10));
                            BigDecimal mbd_v_b = new BigDecimal(new DecimalFormat("#00.0").format(Double.valueOf(v_b) / 10));
                            BigDecimal mbd_v_c = new BigDecimal(new DecimalFormat("#00.0").format(Double.valueOf(v_c) / 10));
                            log.info("data receive: A电压:" + mbd_v_a.toString());
                            log.info("data receive: B电压:" + mbd_v_b.toString());
                            log.info("data receive: C电压:" + mbd_v_c.toString());

                            map.put("end_voltage_a", mbd_v_a.toString());
                            map.put("end_voltage_b", mbd_v_b.toString());
                            map.put("end_voltage_c", mbd_v_c.toString());
                            voltage_count++;
                        }
                    }
                    break;
                case "4F"://电流 00 00 01 00 00 01 00 00 01
                    if (Utilty.isNumeric(data)) {
                        if (current_count == 1) {
                            String c_c = data.substring(0, 6);
                            String c_b = data.substring(6, 12);
                            String c_a = data.substring(12, 18);

                            BigDecimal mbd_c_a = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(c_a) / 1000));
                            BigDecimal mbd_c_b = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(c_b) / 1000));
                            BigDecimal mbd_c_c = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(c_c) / 1000));
                            log.info("data receive: A电流:" + mbd_c_a);
                            log.info("data receive: B电流:" + mbd_c_b);
                            log.info("data receive: C电流:" + mbd_c_c);
                            map.put("start_current_a", mbd_c_a.toString());
                            map.put("start_current_b", mbd_c_b.toString());
                            map.put("start_current_c", mbd_c_c.toString());

                            current_count++;
                        } else if (current_count == 2) {
                            String c_c = data.substring(0, 6);
                            String c_b = data.substring(6, 12);
                            String c_a = data.substring(12, 18);

                            BigDecimal mbd_c_a = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(c_a) / 1000));
                            BigDecimal mbd_c_b = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(c_b) / 1000));
                            BigDecimal mbd_c_c = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(c_c) / 1000));
                            log.info("data receive: A电流:" + mbd_c_a);
                            log.info("data receive: B电流:" + mbd_c_b);
                            log.info("data receive: C电流:" + mbd_c_c);
                            map.put("end_current_a", mbd_c_a.toString());
                            map.put("end_current_b", mbd_c_b.toString());
                            map.put("end_current_c", mbd_c_c.toString());

                            current_count++;
                        }
                    }
                    break;
                default:
                    break;
            }
            i = i + 4 + length * 2;
//			System.out.print("---------i_end="+i);
//			System.out.print("\n");
        }
        return map;
    }

    /**
     * 用于解析下行指令后电表返回的数据，芯北电表协议
     *
     * @param data3   需要解析的数据域
     * @param typeStr 数据类型
     * @return
     */
    public static Map<String, String> getReadValue(String typeStr, String data3) {//读取单条数据
        String dataStr = data3;
        byte[] data_byte = Utilty.hexStringToBytes(dataStr);
        String data = Utilty.convertByteToString(data_byte, 1, data_byte.length);

        Map<String, String> map = new HashMap<String, String>();
        map.put("read_type", typeStr);
        switch (typeStr) {
            case "10"://有功总电量
//		case "11":
//		case "12":
//		case "13":
            case "15":
                if (Utilty.isNumeric(data)) {
                    BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(data) / 100));
                    log.info("data receive: 有功总电量:" + mBigDecimal);
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
                if (Utilty.isNumeric(data)) {
                    BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.0000").format(Double.valueOf(data) / 10000));
                    log.info("data receive: 有功总功率:" + mBigDecimal);
//					map.put("e_rate", mBigDecimal.toString());
                    map.put("read_value", mBigDecimal.toString());
                }
                break;
            case "31"://电压
            case "32":
            case "33":
                if (Utilty.isNumeric(data)) {
                    BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#00.0").format(Double.valueOf(data) / 10));
                    log.info("data receive: 电压:" + mBigDecimal);
//					map.put("e_voltage", mBigDecimal.toString());
                    map.put("read_value", mBigDecimal.toString());
                }
                break;
            case "41"://电流
            case "42":
            case "43":
                if (Utilty.isNumeric(data)) {
                    BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(data) / 1000));
                    log.info("data receive: 电流:" + mBigDecimal);
//					map.put("e_current", mBigDecimal.toString());
                    map.put("read_value", mBigDecimal.toString());
                }
                break;
            case "50"://总功率因数
            case "51":
            case "52":
            case "53":
                if (Utilty.isNumeric(data)) {
                    BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.000").format(Double.valueOf(data) / 1000));
                    log.info("data receive: 功率因数:" + mBigDecimal);
                    map.put("read_value", mBigDecimal.toString());
                }
                break;
            case "60"://频率
                if (Utilty.isNumeric(data)) {
                    BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(data) / 100));
                    log.info("data receive: 频率:" + mBigDecimal);
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
                if (Utilty.isNumeric(data)) {
                    log.info("data receive: 时间:" + data);
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
                if (Utilty.isNumeric(data)) {
                    long time = Long.parseLong(data);
                    log.info("data receive: 上报时间/认证时长/心跳间隔:" + time);
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
                if (Utilty.isNumeric(data)) {
                    log.info("data receive: 事件总次数:" + data);
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
                if (Utilty.isNumeric(data)) {
                    String i_ascii = Utilty.hexStringToString(dataStr);
                    log.info("data receive: ASCII value:" + i_ascii);
                    map.put("read_value", i_ascii);
                }
                break;
            case "F8"://信号值 //C00147000000031900F80124F116
                if (Utilty.isNumeric(data)) {
                    int length = data.length();
                    if (length == 2) {
                        BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0").format(Double.valueOf(data)));
                        log.info("data receive: 信号值:" + mBigDecimal);
//						map.put("e_signal", mBigDecimal.toString());
                        map.put("read_value", mBigDecimal.toString());
                    } else if (length == 4) {//F8 02 32 32
                        String signal = Utilty.hexStringToString(dataStr);
                        log.info("data receive: 信号值:" + signal);
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
     *
     * @param orig_data 需要解析的数据
     * @return
     */
    public static NBDXDataPo getDXNBData(String orig_data) {
        NBDXDataPo mNBDXDataPo = null;
        byte[] binaryData = Utilty.hexStringToBytes(orig_data);
        int len = binaryData.length;
        String startbyte = Integer.toHexString(binaryData[0] & 0xFF);
        String endbyte = Integer.toHexString(binaryData[len - 1] & 0xFF);
        log.info("GPRS: startbyte:" + startbyte);
        log.info("GPRS: endbyte:" + endbyte);
        //lora \x0001000000000000ff7240972500000000902000000000000097490000000000000000000000000000000000000001030201181516
        //nb_dx  0000350001350100000007FF724047084600000052220000179500000250000095462000879405006331210060090000003214010118A316
        if (/*"80".equals(startbyte) && */"16".equals(endbyte)) {//判定 0x16 == binaryData[len - 1]
            //表号
            String e_num = Utilty.convertByteToString(binaryData, 6, 11);
            System.out.println("nb dx  data : 表号4-9:" + e_num);

            String e_fac = Utilty.convertByteToString(binaryData, 12, 12);//data.substring(16, 18);
            System.out.println("nb dx  data : 厂商=" + e_fac);

            String dataType = Utilty.convertByteToString(binaryData, 13, 13);
            System.out.println("nb dx  data : 数据类型=" + dataType);

            mNBDXDataPo = new NBDXDataPo();
            mNBDXDataPo.setENum(e_num);
            mNBDXDataPo.setEFac(e_fac);

            byte dataLen = binaryData[15];
            System.out.println("nb dx  data : data Length=" + dataLen);
            if (dataLen > 0) {
                // 有功总电量
                BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.00")
                        .format(Double.valueOf(Utilty.convertByteToString(binaryData, 15, 20)) / 100));
                System.out.println("nb dx data 电量:" + mBigDecimal);

                BigDecimal voltage = new BigDecimal(new DecimalFormat("#0.00")
                        .format(Double.valueOf(Utilty.convertByteToString(binaryData, 21, 24)) / 10));
                System.out.println("nb dx data:电压：" + voltage);

                BigDecimal electricity = new BigDecimal(new DecimalFormat("#0.00")
                        .format(Double.valueOf(Utilty.convertByteToString(binaryData, 25, 28)) / 1000));
                System.out.println("nb dx data 电流:" + electricity);

                String time = Utilty.convertByteToString(binaryData, 49, 54);
                System.out.println("nb dx data time:" + time);

                mNBDXDataPo.setEReadings(mBigDecimal.toString());
                mNBDXDataPo.setEVoltage(voltage.toString());
                mNBDXDataPo.setECurrent(electricity.toString());
                mNBDXDataPo.setESignal("");
                mNBDXDataPo.setETime(time);

            } else {
                mNBDXDataPo.setEReadings("");
                mNBDXDataPo.setEVoltage("");
                mNBDXDataPo.setECurrent("");
                mNBDXDataPo.setESignal("");
                mNBDXDataPo.setETime("");
            }
        }

        return mNBDXDataPo;
    }

    /**
     * 用于解析水表上报数据，芯北电表协议 单相电表
     *
     * @param data3 需要解析的数据域
     * @return
     */
    public static Map<String, String> getWMDataValue(String data) {
        Map<String, String> map = new HashMap<String, String>();
        //901F012C000000002C0000000035000000004120141612192040210002
        String data_flag1 = data.substring(0, 2);
        String data_flag2 = data.substring(2, 4);
        String data_seq = data.substring(4, 6);
        String wm_flow = data.substring(6, 16);
        String wm_reverse_flow = data.substring(16, 26);
        String wm_flow_rate = data.substring(26, 36);
        String wm_time = data.substring(36, 50);
        String wm_statu1 = data.substring(50, 54);
        String wm_statu2 = data.substring(54, 58);

        byte[] wm_flow_binary = Utilty.hexStringToBytes(wm_flow);
        BigDecimal wm_flow_b = new BigDecimal(new DecimalFormat("#0.000")
                .format(Double.valueOf(Utilty.convertByteToString(wm_flow_binary, 2, wm_flow_binary.length)) / 1000));
        log.info("累计流量：" + wm_flow_b + "M3");

        byte[] wm_reverse_flow_binary = Utilty.hexStringToBytes(wm_reverse_flow);
        BigDecimal wm_reverse_flow_b = new BigDecimal(new DecimalFormat("#0.000")
                .format(Double.valueOf(Utilty.convertByteToString(wm_reverse_flow_binary, 2, wm_reverse_flow_binary.length)) / 1000));
        log.info("反向累计流量：" + wm_reverse_flow_b + "M3");

        byte[] wm_flow_rate_binary = Utilty.hexStringToBytes(wm_flow_rate);
        BigDecimal wm_flow_rate_b = new BigDecimal(new DecimalFormat("#0.0000")
                .format(Double.valueOf(Utilty.convertByteToString(wm_flow_rate_binary, 2, wm_flow_rate_binary.length)) / 10000));
        log.info("流速量：" + wm_flow_rate_b + "M3/h");

        byte[] wm_time_binary = Utilty.hexStringToBytes(wm_time);
        String wm_time_b = Utilty.convertByteToString(wm_time_binary, 1, wm_time_binary.length);
        log.info("数据时间：" + wm_time_b);

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
     *
     * @param data
     * @return
     */
    public static Map<String, String> getUdpWMDataValue(String data) {
        Map<String, String> map = new HashMap<String, String>();
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
        String data_flag1 = data.substring(0, 2);
        String data_flag2 = data.substring(2, 4);
        String data_seq = data.substring(4, 6);
        String wm_flow = data.substring(6, 16);
        String wm_flow_rate = data.substring(16, 26);
        String wm_pressure = data.substring(60, 64);
        String wm_time = data.substring(70, 84);
        String wm_statu1 = data.substring(84, 88);
//		String wm_statu2=data.substring(54, 58);
        String iccid = data.substring(124, 144);
        String imei = data.substring(144, 164);

        byte[] wm_flow_binary = Utilty.hexStringToBytes(wm_flow);
        BigDecimal wm_flow_b = new BigDecimal(new DecimalFormat("#0.000")
                .format(Double.valueOf(Utilty.convertByteToString(wm_flow_binary, 2, wm_flow_binary.length)) / 1000));
        log.info("累计流量：" + wm_flow_b + "M3");

        byte[] wm_flow_rate_binary = Utilty.hexStringToBytes(wm_flow_rate);
        BigDecimal wm_flow_rate_b = new BigDecimal(new DecimalFormat("#0.0000")
                .format(Double.valueOf(Utilty.convertByteToString(wm_flow_rate_binary, 2, wm_flow_rate_binary.length)) / 10000));
        log.info("瞬时流量：" + wm_flow_rate_b + "M3/h");

        byte[] wm_pressure_binary = Utilty.hexStringToBytes(wm_pressure);
        String wm_pressure_b = Utilty.convertByteToString(wm_pressure_binary, 1, wm_pressure_binary.length);
        System.out.println("水压：" + Integer.parseInt(wm_pressure_b, 16));

        byte[] wm_time_binary = Utilty.hexStringToBytes(wm_time);
        String wm_time_b = Utilty.convertByteToString(wm_time_binary, 1, wm_time_binary.length);
        log.info("数据时间：" + wm_time_b);

        byte[] statu1_binary = Utilty.hexStringToBytes(wm_statu1);
        String statu1_b = Utilty.convertByteToString(statu1_binary, 1, statu1_binary.length);
        System.out.println("imei：" + statu1_b);


        byte[] iccid_binary = Utilty.hexStringToBytes(iccid);
        String iccid_b = Utilty.convertByteToString(iccid_binary, 1, iccid_binary.length);
        System.out.println("ICCID：" + iccid_b);

        byte[] imei_binary = Utilty.hexStringToBytes(imei);
        String imei_b = Utilty.convertByteToString(imei_binary, 1, imei_binary.length);
        String imei_b_n = imei_b.substring(5, imei_b.length());
        System.out.println("imei：" + imei_b_n);

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
     *
     * @param data
     * @return
     */
    public static Map<String, String> getUdpWMReadDataValue(String data) {
        Map<String, String> map = new HashMap<String, String>();
        //A0170140225516
        //A0170140421C
        String DI0 = data.substring(0, 2);
        String DI1 = data.substring(2, 4);
        String seq = data.substring(4, 6);
        String sys_status = data.substring(6, 10);
        if (("17").equals(DI1)) {
            map.put("type", "阀门控制");
        }
        if (data.length() == 12) {
            map.put("switch_status", "写阀值成功");
        } else if (data.length() == 14) {
            String switch_status = data.substring(10, 12);
            String switch_status_str = "";

            switch (switch_status) {
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

    /**
     * 安科瑞电表上报数据解析
     *
     * @param map
     * @param dataNum
     * @param data
     * @return
     */
    public static Map<String, Object> getAKRDataValue(Map<String, Object> map, int dataNum, String data) {
        if (dataNum == 0) {//电压电流功率
            String dataStr_v = data.substring(0, 12);
            String dataStr_c = data.substring(24, 36);
            String dataStr_kw = data.substring(40, 104);
            String dataStr_factor = data.substring(136, 152);

            //电压
            String v_a = dataStr_v.substring(0, 4);
            String v_b = dataStr_v.substring(4, 8);
            String v_c = dataStr_v.substring(8, 12);

            BigDecimal mbd_v_a = new BigDecimal(new DecimalFormat("#00.0").format((double) (Long.parseLong(v_a, 16)) / 10));
            BigDecimal mbd_v_b = new BigDecimal(new DecimalFormat("#00.0").format((double) (Long.parseLong(v_b, 16)) / 10));
            BigDecimal mbd_v_c = new BigDecimal(new DecimalFormat("#00.0").format((double) (Long.parseLong(v_c, 16)) / 10));
            log.info("data receive: A电压:" + mbd_v_a.toString());
            log.info("data receive: B电压:" + mbd_v_b.toString());
            log.info("data receive: C电压:" + mbd_v_c.toString() + "\n");

            map.put("e_voltage_a", mbd_v_a.toString());
            map.put("e_voltage_b", mbd_v_b.toString());
            map.put("e_voltage_c", mbd_v_c.toString());

            //电流 //002d 0000 0000
            String c_a = dataStr_c.substring(0, 4);
            String c_b = dataStr_c.substring(4, 8);
            String c_c = dataStr_c.substring(8, 12);

            BigDecimal mbd_c_a = new BigDecimal(new DecimalFormat("#0.00").format((double) (Long.parseLong(c_a, 16)) / 100));
            BigDecimal mbd_c_b = new BigDecimal(new DecimalFormat("#0.00").format((double) (Long.parseLong(c_b, 16)) / 100));
            BigDecimal mbd_c_c = new BigDecimal(new DecimalFormat("#0.00").format((double) (Long.parseLong(c_c, 16)) / 100));
            log.info("data receive: A电流:" + mbd_c_a);
            log.info("data receive: B电流:" + mbd_c_b);
            log.info("data receive: C电流:" + mbd_c_c + "\n");
            map.put("e_current_a", mbd_c_a.toString());
            map.put("e_current_b", mbd_c_b.toString());
            map.put("e_current_c", mbd_c_c.toString());

            //功率
            String kw1_a = dataStr_kw.substring(0, 8);
            String kw1_b = dataStr_kw.substring(8, 16);
            String kw1_c = dataStr_kw.substring(16, 24);
            String kw1_all = dataStr_kw.substring(24, 32);

            String kw2_a = dataStr_kw.substring(32, 40);
            String kw2_b = dataStr_kw.substring(40, 48);
            String kw2_c = dataStr_kw.substring(48, 56);
            String kw2_all = dataStr_kw.substring(56, 64);

            BigDecimal mbd_kw1_all = new BigDecimal(new DecimalFormat("#0.000").format((double) (Long.parseLong(kw1_all, 16)) / 1000));
            BigDecimal mbd_kw1_a = new BigDecimal(new DecimalFormat("#0.000").format((double) (Long.parseLong(kw1_a, 16)) / 1000));
            BigDecimal mbd_kw1_b = new BigDecimal(new DecimalFormat("#0.000").format((double) (Long.parseLong(kw1_b, 16)) / 1000));
            BigDecimal mbd_kw1_c = new BigDecimal(new DecimalFormat("#0.000").format((double) (Long.parseLong(kw1_c, 16)) / 1000));
            BigDecimal mbd_kw2_all = new BigDecimal(new DecimalFormat("#0.000").format((double) (Long.parseLong(kw2_all, 16)) / 1000));
            BigDecimal mbd_kw2_a = new BigDecimal(new DecimalFormat("#0.000").format((double) (Long.parseLong(kw2_a, 16)) / 1000));
            BigDecimal mbd_kw2_b = new BigDecimal(new DecimalFormat("#0.000").format((double) (Long.parseLong(kw2_b, 16)) / 1000));
            BigDecimal mbd_kw2_c = new BigDecimal(new DecimalFormat("#0.000").format((double) (Long.parseLong(kw2_c, 16)) / 1000));
            log.info("data receive: 总有功功率:" + mbd_kw1_all.toString());
//			System.out.print("data receive: 有功功率a:"+mbd_kw1_a.toString());
//			System.out.print("data receive: 有功功率b:"+mbd_kw1_b.toString());
//			System.out.print("data receive: 有功功率c:"+mbd_kw1_c.toString());
            log.info("data receive: 总无功功率:" + mbd_kw2_all.toString());
//			System.out.print("data receive: 无功功率a:"+mbd_kw2_a.toString());
//			System.out.print("data receive: 无功功率b:"+mbd_kw2_b.toString());
//			System.out.print("data receive: 无功功率c:"+mbd_kw2_c.toString() +"\n");
            map.put("e_kw1_all", mbd_kw1_all.toString());//有功功率
            map.put("e_kw1_a", mbd_kw1_a.toString());
            map.put("e_kw1_b", mbd_kw1_b.toString());
            map.put("e_kw1_c", mbd_kw1_c.toString());

            map.put("e_kw2_all", mbd_kw2_all.toString());//无功功率
            map.put("e_kw2_a", mbd_kw2_a.toString());
            map.put("e_kw2_b", mbd_kw2_b.toString());
            map.put("e_kw2_c", mbd_kw2_c.toString());

            //功率因数
            String factor_a = dataStr_factor.substring(0, 4);
            String factor_b = dataStr_factor.substring(4, 8);
            String factor_c = dataStr_factor.substring(8, 12);
            String factor_all = dataStr_factor.substring(12, 16);

            BigDecimal mbd_f_all = new BigDecimal(new DecimalFormat("#0.000").format((double) (Long.parseLong(factor_all, 16)) / 1000));
            BigDecimal mbd_f_a = new BigDecimal(new DecimalFormat("#0.000").format((double) (Long.parseLong(factor_a, 16)) / 1000));
            BigDecimal mbd_f_b = new BigDecimal(new DecimalFormat("#0.000").format((double) (Long.parseLong(factor_b, 16)) / 1000));
            BigDecimal mbd_f_c = new BigDecimal(new DecimalFormat("#0.000").format((double) (Long.parseLong(factor_c, 16)) / 1000));
            log.info("data receive: 总功率因数:" + mbd_f_all);
//			System.out.print("data receive: A功率因数:"+ mbd_f_a);
//			System.out.print("data receive: B功率因数:"+ mbd_f_b);
//			System.out.print("data receive: C功率因数:"+ mbd_f_c+"\n");
            map.put("e_factor_all", mbd_f_all.toString());//总功率因数
            map.put("e_factor_a", mbd_f_a.toString());
            map.put("e_factor_b", mbd_f_b.toString());
            map.put("e_factor_c", mbd_f_c.toString());
        } else if (dataNum == 1) {//电能
            String dataStr_kwh = data.substring(0, 8);//总电能  0.00
            String dataStr_kwh1 = data.substring(8, 16);//正向有功总电能 0.00
            String dataStr_kwh2 = data.substring(16, 24);//反向有功总电能 0.00

            BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.00").format((double) (Long.parseLong(dataStr_kwh, 16)) / 100));
            BigDecimal mBigDecimal1 = new BigDecimal(new DecimalFormat("#0.00").format((double) (Long.parseLong(dataStr_kwh1, 16)) / 100));
            BigDecimal mBigDecimal2 = new BigDecimal(new DecimalFormat("#0.00").format((double) (Long.parseLong(dataStr_kwh2, 16)) / 100));
            log.info("data receive: 有功总电量:" + mBigDecimal);
            log.info("data receive: 正向有功总电量:" + mBigDecimal1);
            log.info("data receive: 反向有功总电量:" + mBigDecimal2);
            map.put("e_kwh1", mBigDecimal.toString());
            map.put("e_kwh2", mBigDecimal2.toString());

        } else if (dataNum == 2) {//信号值
            log.info("data receive: 信号值:" + data);
            String e_signal = String.valueOf(Long.parseLong(data, 16));
            map.put("e_signal", e_signal);
        } else if (dataNum == 3) {//序列号
            log.info("data receive: 序列号:" + data);
            long e_seq = Long.parseLong(data, 16);
            map.put("e_seq", e_seq);
        } else if (dataNum == 4) {//拉合闸状态
            log.info("data receive: 拉合闸状态:" + data);
            String do1 = data.substring(0, 4);
            String do2 = data.substring(4, 8);
            map.put("e_statu1", do1);//e_statu1----do1
            map.put("e_statu2", do2);//e_statu2----do2
            if ("0000".equals(do1)) {//
                map.put("e_switch", "分闸");
            } else if ("0001".equals(do1)) {
                map.put("e_switch", "合闸");
            }
        }
        return map;
    }

    public static Map<String, Object> getAKRReadValue(String dataValue) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (dataValue.length() == 8) {
            String dataStr_v = dataValue.substring(0, 8);
            BigDecimal mbd_v_c = new BigDecimal(new DecimalFormat("#0.00").format((double) (Long.parseLong(dataStr_v, 16)) / 100));
            log.info("data receive: 电能值or功率值:" + mbd_v_c.toString() + "\n");
            map.put("value", mbd_v_c.toString());
        } else if (dataValue.length() == 4) {//（1：闭合  0：断开）
            String dataStr_v = dataValue.substring(0, 4);
            if ("0000".equals(dataStr_v)) {//
                map.put("value", "分闸");
            } else if ("0001".equals(dataStr_v)) {
                map.put("value", "合闸");
            } else {
                //高低位互换得出上报周期
                byte[] e = Utilty.hexStringToBytes(dataStr_v);
                String new_hex_time = Utilty.convertByteToString(e, 1, e.length);
                map.put("value", Long.parseLong(new_hex_time, 16));//上报周期
                return map;
            }
        }
        return map;
    }

    public static Map<String, Object> getAKREventDataValue(Map<String, Object> map, int dataNum, String dataValue) {
        //800F
        //0A5A 0064
        //07A8 0064
        //0258 0064
        //0032 0064
        //0000 0000
        //00000000
        //00000000
        //00000000
        //00000000
        //00000000
        //0000 0000 0000
        //0000 0000 0000
        //0002
        //00000000000000000000000000000000000000000000000000000000
        //DB86 //CRC

        if (dataNum == 0) {//告警
            String dataStr_alert_allow = dataValue.substring(0, 4);//总电能  0.00
            String dataStr_over_v = dataValue.substring(4, 8);//电压过高 阀值及报警时间
            String dataStr_over_v_timeout = dataValue.substring(8, 12);//电压过高 阀值及报警时间
            String dataStr_low_v = dataValue.substring(12, 16);//欠压
            String dataStr_low_v_timeout = dataValue.substring(16, 20);//欠压
            String dataStr_over_a = dataValue.substring(20, 24);//过流
            String dataStr_over_a_timeout = dataValue.substring(24, 28);//过流
            String dataStr_low_a = dataValue.substring(28, 32);//欠流
            String dataStr_low_a_timeout = dataValue.substring(32, 36);//欠流
            String dataStr_over_kw = dataValue.substring(36, 40);//过功率
            String dataStr_over_kw_timeout = dataValue.substring(40, 44);//过功率
            String dataStr_low_kw = dataValue.substring(44, 48);//欠功率
            String dataStr_low_kw_timeout = dataValue.substring(48, 52);//欠功率
            String dataStr_DI1 = dataValue.substring(52, 60);
            String dataStr_DI2 = dataValue.substring(60, 68);
            String dataStr_DI3 = dataValue.substring(68, 76);
            String dataStr_DI4 = dataValue.substring(76, 84);
            String dataStr_DO1 = dataValue.substring(84, 96);
            String dataStr_DO2 = dataValue.substring(96, 108);
            String dataStr_alert_status = dataValue.substring(108, 112);//报警状态字
            String dataStr_over_v_value = dataValue.substring(112, 116);//过压
            String dataStr_low_v_value = dataValue.substring(116, 120);//欠压
            String dataStr_over_a_value = dataValue.substring(120, 124);//过流
            String dataStr_low_a_value = dataValue.substring(124, 128);//欠流
            String dataStr_over_kw_value = dataValue.substring(128, 132);//过功率
            String dataStr_low_kw_value = dataValue.substring(132, 136);//欠功率

            BigDecimal mbd_over_v = new BigDecimal(new DecimalFormat("#0.0").format((double) (Long.parseLong(dataStr_over_v, 16)) / 10));
            BigDecimal mbd_low_v = new BigDecimal(new DecimalFormat("#0.0").format((double) (Long.parseLong(dataStr_low_v, 16)) / 10));
            BigDecimal mbd_over_a = new BigDecimal(new DecimalFormat("#0.00").format((double) (Long.parseLong(dataStr_over_a, 16)) / 10));
            BigDecimal mbd_low_a = new BigDecimal(new DecimalFormat("#0.00").format((double) (Long.parseLong(dataStr_low_a, 16)) / 10));
            BigDecimal mbd_over_kw = new BigDecimal(new DecimalFormat("#0.000").format((double) (Long.parseLong(dataStr_over_kw, 16)) / 1000));
            BigDecimal mbd_low_kw = new BigDecimal(new DecimalFormat("#0.000").format((double) (Long.parseLong(dataStr_low_kw, 16)) / 1000));
            log.info("data receive: 过压值:" + mbd_over_v.toString() + "\n");
            log.info("data receive: 欠压值:" + mbd_low_v.toString() + "\n");
            log.info("data receive: 过流值:" + mbd_over_a.toString() + "\n");
            log.info("data receive: 欠流值:" + mbd_low_a.toString() + "\n");
            log.info("data receive: 过功率值:" + mbd_over_kw.toString() + "\n");
            log.info("data receive: 欠功率值:" + mbd_low_kw.toString() + "\n");
            map.put("over_v", mbd_over_v.toString());
            map.put("low_v", mbd_low_v.toString());
            map.put("over_a", mbd_over_a.toString());
            map.put("low_a", mbd_low_a.toString());
            map.put("over_kw", mbd_over_kw.toString());
            map.put("low_kw", mbd_low_kw.toString());

            BigDecimal mbd_over_v_value = new BigDecimal(new DecimalFormat("#0.0").format((double) (Long.parseLong(dataStr_over_v_value, 16)) / 10));
            BigDecimal mbd_low_v_value = new BigDecimal(new DecimalFormat("#0.0").format((double) (Long.parseLong(dataStr_low_v_value, 16)) / 10));
            BigDecimal mbd_over_a_value = new BigDecimal(new DecimalFormat("#0.00").format((double) (Long.parseLong(dataStr_low_v_value, 16)) / 10));
            BigDecimal mbd_low_a_value = new BigDecimal(new DecimalFormat("#0.00").format((double) (Long.parseLong(dataStr_low_v_value, 16)) / 10));
            BigDecimal mbd_over_kw_value = new BigDecimal(new DecimalFormat("#0.000").format((double) (Long.parseLong(dataStr_low_v_value, 16)) / 1000));
            BigDecimal mbd_low_kw_value = new BigDecimal(new DecimalFormat("#0.000").format((double) (Long.parseLong(dataStr_low_v_value, 16)) / 1000));
            log.info("data receive: 过压值:" + mbd_over_v_value.toString() + "\n");
            log.info("data receive: 欠压值:" + mbd_low_v_value.toString() + "\n");
            log.info("data receive: 过流值:" + mbd_over_a_value.toString() + "\n");
            log.info("data receive: 欠流值:" + mbd_low_a_value.toString() + "\n");
            log.info("data receive: 过功率值:" + mbd_over_kw_value.toString() + "\n");
            log.info("data receive: 欠功率值:" + mbd_low_kw_value.toString() + "\n");
            map.put("over_v_value", mbd_over_v_value.toString());
            map.put("low_v_value", mbd_low_v_value.toString());
            map.put("over_a_value", mbd_over_a_value.toString());
            map.put("low_a_value", mbd_low_a_value.toString());
            map.put("over_kw_value", mbd_over_kw_value.toString());
            map.put("low_kw_value", mbd_low_kw_value.toString());

            log.info("data receive: 报警允许位:" + dataStr_alert_allow + "\n");
            log.info("data receive: 报警状态位:" + dataStr_alert_status + "\n");
            map.put("alert_allow", dataStr_alert_allow);
            map.put("alert_status", dataStr_alert_status);
            String alert_type = "";
            if (dataStr_alert_status != null) {
                if (("0000").equals(dataStr_alert_status)) {
                    log.info("正常");
                    alert_type = alert_type + "正常";
                }
                long data_hex = Long.parseLong(dataStr_alert_status, 16);
                if ((data_hex & 0x8000) > 0) {// 断电
                    log.info("断电");
                    alert_type = "断电";
                }
                if ((data_hex & 0x4000) > 0) {// 相序错误
                    log.info("相序错误");
                    alert_type = "相序错误";
                }

                if ((data_hex & 0x0010) > 0) {
                    log.info("过功率");
                    alert_type = alert_type + "过功率";
                }
                if ((data_hex & 0x0020) > 0) {
                    log.info("欠功率");
                    alert_type = alert_type + "欠功率";
                }
                if ((data_hex & 0x0040) > 0) {
                    log.info("DO1");
                    alert_type = alert_type + "DO1";
                }
                if ((data_hex & 0x0080) > 0) {
                    log.info("DO2");
                    alert_type = alert_type + "DO2";
                }

                if ((data_hex & 0x0008) > 0) {
                    log.info("欠流");
                    alert_type = alert_type + "欠流";
                }
                if ((data_hex & 0x0004) > 0) {
                    log.info("过流");
                    alert_type = alert_type + "过流";
                }
                if ((data_hex & 0x0002) > 0) {
                    log.info("欠压");
                    alert_type = alert_type + "欠压";
                }
                if ((data_hex & 0x0001) > 0) {
                    log.info("过压");
                    alert_type = alert_type + "过压";
                }
            }
            map.put("alert_status_type", alert_type);
        }
        return map;
    }

    public static Map<String, Object> getAKRSingleReadValue(String dataValue) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (dataValue.length() == 4) {
            Integer cycle = Integer.valueOf(dataValue, 16);
            map.put("value", cycle.toString());

        }
        if (dataValue.length() == 8) {
            String dataStr = dataValue.substring(0, 8);
            // 强控打开
            String dataOne = dataStr.substring(0, 4);
            // 拉合闸状态
            String dataTwo = dataStr.substring(4, 8);
            if ("0000".equals(dataOne)) {
                log.info("数据段无效");
            }
            if ("0001".equals(dataOne) && "0000".equals(dataTwo)) {
                map.put("value", "合闸");
            }
            if ("0001".equals(dataOne) && "0001".equals(dataTwo)) {
                map.put("value", "拉闸");
            }
        }
        return map;
    }

    public static Map<String, Object> getAKRSingleEventDataValue(Map<String, Object> map, int num, String dataValue_alt) {
        if (num == 0) {//告警
            // 0003  为报警允许位，电流过高，电压过高
            // 0003  为电压阈值，单位0.1V
            // 0001  为过压延时时间，0.01S
            // 0001  为电流阈值，单位0.01A
            // 0001  为过流延时时间，0.01S
            // 0003  为报警，电流过高，电压过高
            // 08AB  为报警时电压值，单位0.1V
            // 000F  为报警时电流值，单位0.01A
            // 01    1为强控开关，0为关，1为开
            // 00    为强控分合闸状态，0为强控跳闸，1为强控合闸
            // 0000000000000000000000000000 93BB 01 2929 5D5DBBE67D7D
            String bit = dataValue_alt.substring(0, 4); //允许位
            String dataStr_vlo_value = dataValue_alt.substring(4, 8);  //为电压阈值
            String dataStr_vlo_time = dataValue_alt.substring(8, 12); // 为过压延时时间
            String dataStr_cur_value = dataValue_alt.substring(12, 16);// 电流阈值
            String dataStr_cur_time = dataValue_alt.substring(16, 20);// 为过流延时时间
            String dataString = dataValue_alt.substring(20, 24);//  为报警，电流过高，电压过高
            String waring_vlo_value = dataValue_alt.substring(24, 28); //// 报警电压值
            String waring_cur_value = dataValue_alt.substring(28, 32);// 报警电流值
            // String waring_cur_value=dataValue_alt.substring(32,34); // 强控开关
            // String dataString10=dataValue_alt.substring(34,36); // 分合闸状态
            String reserve = dataValue_alt.substring(36, 64); // 预留位
            String checkBit = dataValue_alt.substring(64, 68);// 状态位
            String statusWord = dataValue_alt.substring(68, dataValue_alt.length());

            // 过压阈值时间
            BigDecimal bigdec_over_v_threshold = new BigDecimal(new DecimalFormat("#0.00").format((double) (Long.parseLong(dataStr_vlo_time, 16)) / 100));
            // 过压阈值
            BigDecimal over_v_over_v_value = new BigDecimal(new DecimalFormat("#0.00").format((double) (Long.parseLong(dataStr_vlo_value, 16)) / 10));
            // 过流阈值时间
            BigDecimal bigdec_cur_a_threshold = new BigDecimal(new DecimalFormat("#0.00").format((double) (Long.parseLong(dataStr_cur_time, 16)) / 100));
            // 过流阈值
            BigDecimal bigdec_cur_v_value = new BigDecimal(new DecimalFormat("#0.00").format((double) (Long.parseLong(dataStr_cur_value, 16)) / 100));

            // 报警电压值
            BigDecimal bigdec_war_v_value = new BigDecimal(new DecimalFormat("#0.00").format((double) (Long.parseLong(waring_vlo_value, 16)) / 10));
            BigDecimal bigdec_war_a_value = new BigDecimal(new DecimalFormat("#0.00").format((double) (Long.parseLong(waring_cur_value, 16)) / 100));
            map.put("over_v", bigdec_over_v_threshold.toString());
            map.put("over_a", bigdec_cur_a_threshold.toString());
            map.put("over_v_value", over_v_over_v_value.toString());
            map.put("over_a_value", bigdec_cur_v_value.toString());
            // 报警电压值
            map.put("war_v_value", bigdec_war_v_value.toString());
            // 报警电流值
            map.put("war_a_value", bigdec_war_a_value.toString());

            String elStatus = Integer.toBinaryString(Integer.valueOf(dataString, 16));
            StringBuffer binaryString = new StringBuffer();
            if (elStatus.length() < 4) {

                binaryString.append(elStatus);
                for (int i = 1; i <= 4 - elStatus.length(); i++) {
                    binaryString.insert(0, 0);
                }
            }

            String overCurrent = binaryString.substring(2, 3);
            String overVoltage = binaryString.substring(3, 4);
            if ("1".equals(overCurrent)) {
                map.put("alert_status_type", "过流");
            }
            if ("1".equals(overVoltage)) {
                map.put("alert_status_type", "过压");
            }


        }
        return map;
    }

    /**
     * 数据解析
     *
     * @param map
     * @param num
     * @param dataValue
     * @return
     */
    public static Map<String, Object> getAKRSingleDataValue(Map<String, Object> map, int num, String dataValue) {
        if (num == 0) {
            // 电压 电流 有功功率 功率因数
            // 电压
            String e_voltage = dataValue.substring(0, 4);
            // 电流
            String e_current = dataValue.substring(4, 8);
            // 有功功率
            String mbd_kw1_al = dataValue.substring(8, 12);
            // 视图功率
            String e_power1 = dataValue.substring(12, 16);
            // 功率因数
            String factor_all = dataValue.substring(20, 24);
            // 08dc 0015 002b fff0 002d 03bb 083e
            // 08d3 0010 0022 ffef 0026 037e 138a 140b 57bf
            BigDecimal mbd_v_a = new BigDecimal(new DecimalFormat("#00.0").format((double) (Long.parseLong(e_voltage, 16)) / 10));

            BigDecimal mbd_c_a = new BigDecimal(new DecimalFormat("#0.00").format((double) (Long.parseLong(e_current, 16)) / 100));

            BigDecimal mbd_kw1_all = new BigDecimal(new DecimalFormat("#0.000").format((double) (Long.parseLong(mbd_kw1_al, 16)) / 1000));

            BigDecimal mbd_fac_all = new BigDecimal(new DecimalFormat("#0.000").format((double) (Long.parseLong(factor_all, 16)) / 1000));
            map.put("e_voltage_a", mbd_v_a.toString());
            map.put("e_current_a", mbd_c_a.toString());
            map.put("e_kw1_all", mbd_kw1_all.toString());
            map.put("e_factor_all", mbd_fac_all.toString());

        }
        if (num == 1) {
            String signal = dataValue.substring(0, 4); //信号值
            String seq = dataValue.substring(4, 8);// 数据包序列号
            long e_seq = Long.parseLong(seq, 16);
            Integer e_signal = Integer.valueOf(signal, 16);
            map.put("e_seq", e_seq);
            map.put("e_signal", e_signal.toString());
        }
        if (num == 2) {
            String compulsoryCon = dataValue.substring(0, 4);
            String openCloseCon = dataValue.substring(4, 8);
            map.put("e_statu2", compulsoryCon);
            if ("0001".equals(compulsoryCon) && "0000".equals(openCloseCon)) {
                map.put("e_statu1", openCloseCon);
                // 通电
            }
            if ("0001".equals(compulsoryCon) && "0001".equals(openCloseCon)) {
                map.put("e_statu1", openCloseCon);
                // 断电
            }
            if ("0000".equals(openCloseCon)) {
                map.put("e_switch", "0");
            } else if ("0001".equals(openCloseCon)) {
                map.put("e_switch", "1");
            }
        }
        if (num == 3) {
            // 正向功率 反向功率
            // 00000007 00000000 2017
            // 0000000d 00000000 b816
            String dataStr_kwh1 = dataValue.substring(0, 8);
            String dataStr_kwh2 = dataValue.substring(8, 16);
            BigDecimal BigDecimalDataStrkwh1 = new BigDecimal(new DecimalFormat("#0.00").format((double) (Long.parseLong(dataStr_kwh1, 16)) / 100));
            BigDecimal BigDecimalDataStrkwh2 = new BigDecimal(new DecimalFormat("#0.00").format((double) (Long.parseLong(dataStr_kwh2, 16)) / 100));
            // map.put("e_kwh1", BigDecimalDataStrkwh1.toString());
            map.put("e_kwh2", BigDecimalDataStrkwh2.toString());
        }
        if (num == 4) {
            String dataStr_kwh = dataValue.substring(0, 8);
            BigDecimal mBigDecimalKw = new BigDecimal(new DecimalFormat("#0.00").format((double) (Long.parseLong(dataStr_kwh, 16)) / 100));
            map.put("e_kwh1", mBigDecimalKw.toString());
        }
        return map;
    }
}
