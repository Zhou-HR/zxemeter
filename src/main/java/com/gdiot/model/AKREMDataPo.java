package com.gdiot.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhouHR
 */
@Data
public class AKREMDataPo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String devId;
    private String origValue;
    private long time;
    /**
     * 信息来源:nb;lora;mqtt_2g
     */
    private String source;

    //解析出的数据
    private String eNum;//表号
    //	private String e_fac;//厂商
//	private String e_time;//数据的时间
    private long eSeq;//序列号
    //	private int flag_reload;
//	private String seq_type;
    private String eSignal;//信号

    private String dataType;//上报数据类型，日冻结，月冻结，整点冻结
    private String eKwh1 = "";//有功电能 同度数 e_readings
    private String eKwh2 = "";//无功电能
    private String eKw1All = "";//有功总功率
    private String eKw2All = "";//无功总功率
    private String eVoltageA = "";//电压 同e_vlotage
    private String eVoltageB = "";
    private String eVoltageC = "";
    private String eCurrentA = "";//A相电流 同e_current
    private String eCurrentB = "";//电流
    private String eCurrentC = "";//电流
    private String eFactorAll = "";//总功率因数
    private String eStatu1 = "";//电表状态字1
    private String eStatu2 = "";//电表状态字2
    private String eSwitch = "";//继电器状态

}
