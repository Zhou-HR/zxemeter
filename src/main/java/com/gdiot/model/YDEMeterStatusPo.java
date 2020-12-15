package com.gdiot.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhouHR
 */
@Data
public class YDEMeterStatusPo implements Serializable {

    private static final long serialVersionUID = 1L;
    //设备上线下线状态
    private int id;
    private String devId;
    private String imei;
    private int status;
    private int type;
    private int loginType;
    private long time;

}
