package com.gdiot.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: shenzhengkai
 * @className: EmDataNBUtil
 * @description: 宽腾电表数据解码
 * @date: 日期: 2020/10/24 时间: 13:02
 **/

public class EmDataNBUtil {
    private final static String HEX = "hex";
    private final static String BINARY = "binary";

    public static Map<String, String> getDataValue(String orig_data) {
        Map map = new HashMap();
        // 截取数据域
        String newString = orig_data.substring(20, orig_data.length() - 4);
        // 截取数据标识
        String dataIdent = newString.substring(0, 8);
        if (dataIdent != null) {
            String hexDataIdent = getHexBinary(dataIdent, HEX);
            map.put("dataIdent", hexDataIdent);
        }
        // 截取组合有功总电
        String combinationPower = newString.substring(8, 16);
        if (combinationPower != null) {
            String hexCombinationPower = getHexBinary(combinationPower, HEX);
            BigDecimal comPowerBigDecimal = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(hexCombinationPower) / 100));
            map.put("combinationPower", comPowerBigDecimal.toString());
        }
        // 截取正向有功总电
        String forwardPower = newString.substring(16, 24);
        if (forwardPower != null) {
            String hexForwardPower = getHexBinary(forwardPower, HEX);
            BigDecimal forwardPowerBigDecimal = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(hexForwardPower) / 100));
            map.put("forwardPower", forwardPowerBigDecimal.toString());
        }
        // 截取反向有功总电
        String reversePower = newString.substring(24, 32);
        if (reversePower != null) {
            String hexReversePower = getHexBinary(reversePower, HEX);
            BigDecimal freversePowerBigDecimal = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(hexReversePower) / 100));
            map.put("reversePower", freversePowerBigDecimal.toString());
        }
        // 截取电压
        String voltage = newString.substring(32, 36);
        if (voltage != null) {
            String hexVoltage = getHexBinary(voltage, HEX);
            BigDecimal mBigDecimal = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(hexVoltage) / 10));
            map.put("voltage", mBigDecimal.toString());
        }
        // 截取电流
        String current = newString.substring(36, 42);
        if (current != null) {
            String hexCurrent = getHexBinary(current, HEX);
            BigDecimal currentBigDecimal = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(hexCurrent) / 1000));
            map.put("current", currentBigDecimal.toString());
        }
        // 截取有功功率
        String activePower = newString.substring(42, 48);
        if (activePower != null) {
            String hexActivePower = getHexBinary(activePower, HEX);
            BigDecimal activePowerBigDecimal = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(hexActivePower) / 100));
            map.put("activePower", activePowerBigDecimal.toString());
        }
        // 截取电网频率
        String elecfrequency = newString.substring(48, 52);
        // 截取功率因数
        String powerFactor = newString.substring(52, 56);
        if (powerFactor != null) {
            String hexaPowerFactor = getHexBinary(powerFactor, HEX);
            BigDecimal activePowerBigDecimal = new BigDecimal(new DecimalFormat("#0.00").format(Double.valueOf(hexaPowerFactor) / 100));
            map.put("powerFactor", activePowerBigDecimal.toString());
        }

        // 截取序列号
        String seq = orig_data.substring(82, 84);
        map.put("seq", Integer.valueOf(seq, 16).toString());
        // 截取状态位
        String status = newString.substring(56, 58);
        if (status != null) {
            // 二进制字符串
            String binaryStatus = getHexBinary(status, BINARY);
            map.put("status", status);
            // 继电器状态
            String bit4 = binaryStatus.substring(3, 4);
            // 当前运行时区
            String bit5 = binaryStatus.substring(2, 3);
            // 继电器状态
            String bit6 = binaryStatus.substring(1, 2);
            // 预跳闸报警状态
            String bit7 = binaryStatus.substring(0, 1);
            // 断电
            if ("1".equals(bit4) && "1".equals(bit6)) {
                map.put("switch", "1");
            }
            // 通电
            if ("0".equals(bit4) && "0".equals(bit6)) {
                map.put("switch", "0");
            }
        }
        return map;
    }

    private static String getHexBinary(String hexData, String flag) {
        // 判断数据长度 根据长度拼接数据的长度
        int len = hexData.length();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 1; i <= len; i++) {
            stringBuffer.append("3");
        }
        byte[] data_byte = Utilty.hexStringToBytes(hexData);
        String dataValue = Utilty.convertByteToString(data_byte, 1, data_byte.length);
        // 返回十六进制
        if ("hex".equals(flag)) {
            String hex = Integer.toHexString(Integer.valueOf(dataValue, 16) - Integer.valueOf(stringBuffer.toString(), 16));
            return hex;
        }
        // 返回二进制
        if ("binary".equals(flag)) {
            String statusBinary = Integer.toBinaryString(Integer.valueOf(dataValue, 16) - Integer.valueOf(stringBuffer.toString(), 16));
            int binaryLen = statusBinary.length();
            StringBuffer binaryString = new StringBuffer();
            binaryString.append(statusBinary);
            //最高位不够八位自动补0
            for (int i = 1; i <= 8 - binaryLen; i++) {
                binaryString.insert(0, 0);
            }
            return binaryString.toString();
        }
        return null;
    }

    public static Map<String, String> getCmdStauDocding(String orig_data) {
        Map map = new HashMap();
        // 截取表号
        String eNum = orig_data.substring(2, 14);
        String hexNum = highLowSwap(eNum);
        map.put("eNum", hexNum);
        // 截取状态
        String status = orig_data.substring(28, 32);
        if (status != null) {
            if ("3333".equals(status)) {
                map.put("status", "合闸");
            }
            if ("8333".equals(status)) {
                map.put("status", "拉闸");
            }

        }
        return map;
    }

    public static Map<String, String> getDocding(String orig_data) {
        Map map = new HashMap();
        // 截取表号
        String eNum = orig_data.substring(2, 14);
        String hexNum = highLowSwap(eNum);
        map.put("eNum", hexNum);
        // 截取状态
        String status = orig_data.substring(16, 18);
        if (status != null) {
            if ("9C".equals(status) || "94".equals(status)) {
                map.put("status", "SUCCESS");
            }
            if ("DC".equals(status) || "D4".equals(status)) {
                map.put("status", "FAIL");
            }
        }
        return map;
    }

    private static String highLowSwap(String eNum) {
        byte[] data_byte = Utilty.hexStringToBytes(eNum);
        String eNumHex = Utilty.convertByteToString(data_byte, 1, data_byte.length);
        return eNumHex;
    }
}
