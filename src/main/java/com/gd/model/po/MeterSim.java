package com.gd.model.po;

import java.util.Date;

import lombok.Data;

/**
 * @author ZhouHR
 */
@Data
public class MeterSim {

    private int id;

    private String cardNumber;

    private long insertTime;

    private long logId;

    private String usageMonth;

    private String usageYesterday;

    private String imei;

    private String imsi;

    private String iccid;

    private String meterNo;

    private Date insertDate;

}
