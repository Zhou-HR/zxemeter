package com.gdiot.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: shenzhengkai
 * @className: ElectricBanlanceData
 * @description: ElectricBanlanceData
 * @date: 日期: 2020/10/26 时间: 9:13
 **/
@Data
public class ktEmDataPo implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String devId;// 设备id
    private String origValue;// 数据预报
    private long time;//时间
    private String eNum;//表号
    private Integer eSeq;//序列号
    private String eSource;  // 设备资源类型
    private String eSignal;//信号
    private String eKwAll = "";//有功总功率
    private String eKw1 = "";//正向有功功率
    private String eKw2 = "";//反向无功功率
    private String eVoltage = "";//电压
    private String eCurrent = "";//相电流
    private String eFactorAll = "";//总功率因数
    private String eStatus = "";//总功率因数
    private String ekw;//总电能
    private String eSwitch;// 拉合闸状态

}
