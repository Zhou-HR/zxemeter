package com.gdiot.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhouHR
 */
@Data
public class NBDXDataPo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String devId;
    private String origValue;
    private long time;
    /**
     * 信息来源:01:NB;02:Lora;03:2G
     */
    private String source;

    //解析出的数据
    private String eReadings;//度数
    private String eVoltage;//电压
    private String eCurrent;//电流
    private String eSignal;//信号
    private String eNum;//表号
    private String eFac;//厂商
    private String eTime;//数据的时间

}
