package com.gdiot.model;

import lombok.Data;

/**
 * @author ZhouHR
 */
@Data
public class AKREMEventPo {

    private int id;
    private String devId;
    private String imei;
    private String origValue;
    private long time;

    //解析出的数据
    private String eventType;
    private String eNum;//表号
    //解析出的数据
    private String eStartTime;//事件开始的时间
    private String OverVThreshold;
    private String LowVThreshold;
    private String OverAThreshold;
    private String LowAThreshold;
    private String OverKWThreshold;
    private String LowKWThreshold;
    private String OverVValue;
    private String LowVValue;
    private String OverAValue;
    private String LowAValue;
    private String OverKWValue;
    private String LowKWValue;
    private String AlertAllow;
    private String AlertStatus;
    private String AlertType;
    private String eVValue;
    private String eAValue;

//	private String eEndTime;//事件结束的时间
//	private String eStartKwh;//事件开始电量
//	private String eStartKwh2;//事件开始电量
//	private String eEndKwh;//事件结束电量
//	private String eEndKwh2;//事件结束电量
//	private String eStartVoltageA;//事件开始电压
//	private String eStartVoltageB;//事件开始电压
//	private String eStartVoltageC;//事件开始电压
//	private String eEndVoltageA;//事件结束电压
//	private String eEndVoltageB;//事件结束电压
//	private String eEndVoltageC;//事件结束电压
//	private String eStartCurrentA;//事件开始电流
//	private String eStartCurrentB;//事件开始电流
//	private String eStartCurrentC;//事件开始电流
//	private String eEndCurrentA;//事件结束电流
//	private String eEndCurrentB;//事件结束电流
//	private String eEndCurrentC;//事件结束电流
//	private String eStartSwitch;//停电事件 事件开始继电器状态
//	private String eEndSwitch;//停电事件 事件结束继电器状态
//	private String eStartStatus1;// 事件开始 状态字1
//	private String eEndStatus1;//事件结束 状态字1
//	private String eStartStatus2;// 事件开始 状态字2
//	private String eEndStatus2;// 事件结束 状态字2
//	private String eStartModule;//模块状态监控字
//	private String eEndModule;//模块状态监控字
//	private int flagReload;
    /**
     * 信息来源:01:NB;02:Lora
     */
    private String source;
}
