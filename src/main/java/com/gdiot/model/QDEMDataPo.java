package com.gdiot.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * em_data_qd
 * @author ZhouHR
 */
@Data
public class QDEMDataPo implements Serializable {
    private Integer id;

    /**
     * 设备ID
     */
    private String devId;

    /**
     * 具体数据
     */
    private String origValue;

    /**
     * 平台时间
     */
    private Long time;

    private Date createTime;

    /**
     * 信息来源
     */
    private String source;

    /**
     * 表号
     */
    private String eNum;

    /**
     * 电能
     */
    private String eKwh;

    /**
     * 总功率
     */
    private String eKwAll;

    /**
     * A相电压
     */
    private String eVoltageA;

    /**
     * B相电压
     */
    private String eVoltageB;

    /**
     * C相电压
     */
    private String eVoltageC;

    /**
     * A相电流
     */
    private String eCurrentA;

    /**
     * B相电流
     */
    private String eCurrentB;

    /**
     * C相电流
     */
    private String eCurrentC;

    /**
     * 继电器命令状态
     */
    private String eSwitch1;

    /**
     * 继电器实际状态
     */
    private String eSwitch2;

    /**
     * 数据的时间
     */
    private String eTime;

    /**
     * 温度
     */
    private String eTemperature;

    private static final long serialVersionUID = 1L;
}