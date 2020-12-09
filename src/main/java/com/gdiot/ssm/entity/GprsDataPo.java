package com.gdiot.ssm.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhouHR
 */
@Data
public class GprsDataPo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String dev_id;
    private String orig_value;
    private long time;
    /**
     * 信息来源:01:NB;02:Lora;03:GPRS
     */
    private String source;

    //解析出的数据
    private String e_num;//表号
    private String e_fac;//厂商
    private String e_time;//数据的时间
    private long e_seq;//序列号
    private int flag_reload;
    private String seq_type;
    private String e_signal;//信号

    private String e_kwh1 = "";//有功电能 同度数 e_readings
    private String e_kwh2 = "";//无功电能
    private String e_kw1_all = "";//有功总功率
    private String e_kw2_all = "";//无功总功率
    private String e_voltage_a = "";//电压 同e_vlotage
    private String e_voltage_b = "";
    private String e_voltage_c = "";
    private String e_current_a = "";//A相电流 同e_current
    private String e_current_b = "";//电流
    private String e_current_c = "";//电流
    private String e_factor_all = "";//总功率因数
    private String e_statu1 = "";//电表状态字1
    private String e_statu2 = "";//电表状态字2
    private String e_switch = "";//继电器状态

}
