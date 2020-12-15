package com.gdiot.model;

import lombok.Data;

import java.util.Date;

/**
 * @author ZhouHR
 */
@Data
public class EMMeterPo {

    private Integer id;

    private String meterNo;

    private String oldMeterNo;

    private Double newValue;

    private Double oldValue;

    private String sim;

    private String imei;

    private String imsi;

    private String power;

    private String deviceNo;

    private String meterType;

    private String meterType1 = "";

    private String platform;

    private String platform1 = "";

    private String companyId;

    private String company;

    private String unit;

    private String projectNo;

    private Integer status;

    private Date createTime;

    private String userCode;

    private String userName;

    private Date installTime;

    private String projectName;

    private Integer lahezhaStatus;

    private String strInstllTime;

    private String strStatus;

    private String address;

}
