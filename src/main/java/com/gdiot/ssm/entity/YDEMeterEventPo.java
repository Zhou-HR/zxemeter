package com.gdiot.ssm.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhouHR
 */
@Data
public class YDEMeterEventPo implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String dev_id;
    private String orig_value;
    private long time;
    private String imei;

    //解析出的数据
    private String event_type;
    private String e_num;//表号
    private String e_fac;//厂商
    //解析出的数据
    private long e_seq;//序列号
    private String e_start_time;//事件开始的时间
    private String e_end_time;//事件结束的时间
    private String e_start_kwh;//事件开始电量
    private String e_start_kwh2;//事件开始电量
    private String e_end_kwh;//事件结束电量
    private String e_end_kwh2;//事件结束电量
    private String e_start_voltage_a;//事件开始电压
    private String e_start_voltage_b;//事件开始电压
    private String e_start_voltage_c;//事件开始电压
    private String e_end_voltage_a;//事件结束电压
    private String e_end_voltage_b;//事件结束电压
    private String e_end_voltage_c;//事件结束电压
    private String e_start_current_a;//事件开始电流
    private String e_start_current_b;//事件开始电流
    private String e_start_current_c;//事件开始电流
    private String e_end_current_a;//事件结束电流
    private String e_end_current_b;//事件结束电流
    private String e_end_current_c;//事件结束电流
    private String e_start_switch;//停电事件 事件开始继电器状态
    private String e_end_switch;//停电事件 事件结束继电器状态
    private String e_start_status1;// 事件开始 状态字1
    private String e_end_status1;//事件结束 状态字1
    private String e_start_status2;// 事件开始 状态字2
    private String e_end_status2;// 事件结束 状态字2
    private String e_start_module;//模块状态监控字
    private String e_end_module;//模块状态监控字
    private int flag_reload;
    /**
     * 信息来源:01:NB;02:Lora
     */
    private String source;
    private int deal_flag;

}
