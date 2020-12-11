package com.gdiot.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhouHR
 * @date 2020/12/11
 */
@Data
public class QDEMDataPo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String devId;
    private String origValue;
    private long time;
    /**
     * 信息来源
     */
    private String source;

    //解析出的数据
    /**
     * 表号
     */
    private String eNum;
    /**
     * 电能
     */
    private String eKwh = "";
    /**
     * 总功率
     */
    private String eKwAll = "";
    /**
     * A相电压
     */
    private String eVoltageA = "";
    /**
     * B相电压
     */
    private String eVoltageB = "";
    /**
     * C相电压
     */
    private String eVoltageC = "";
    /**
     * A相电流
     */
    private String eCurrentA = "";
    /**
     * B相电流
     */
    private String eCurrentB = "";
    /**
     * C相电流
     */
    private String eCurrentC = "";
    /**
     * 继电器命令状态
     */
    private String eSwitch1 = "";
    /**
     * 继电器实际状态
     */
    private String eSwitch2 = "";
    /**
     * 数据的时间
     */
    private String eTime;
}
