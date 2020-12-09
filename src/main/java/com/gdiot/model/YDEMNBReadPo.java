package com.gdiot.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhouHR
 */
@Data
public class YDEMNBReadPo implements Serializable {

    private static final long serialVersionUID = 1L;
    //设备上报点抄数值
    private int id;
    private String dev_id;
    private String imei;
    private long time;
    private String orig_value;
    private String e_num;
    private String e_fac;
    private String read_type;
    private String read_value;
    private String data_seq;
    /**
     * 信息来源:01:NB;02:Lora
     */
    private String source;

}
