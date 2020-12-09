package com.gdiot.ssm.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhouHR
 */
@Data
public class NBDXDataPo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String dev_id;
    private String orig_value;
    private long time;
    /**
     * 信息来源:01:NB;02:Lora;03:2G
     */
    private String source;

    //解析出的数据
    private String e_readings;//度数
    private String e_voltage;//电压
    private String e_current;//电流
    private String e_signal;//信号
    private String e_num;//表号
    private String e_fac;//厂商
    private String e_time;//数据的时间

}
