package com.gd.model.po;

import lombok.Data;

/**
 * @author ZhouHR
 */
@Data
public class MeterMaintain {

    private Integer id;

    private String meterNo;

    private String projectNo;

    private String sendDate;

    private String dealDate;

    private String sender;

    private String senderName;

    private String receiver;

    private String receiverName;

    private String reason;

    private String dealResult;

    private String dealResultShort;

    private String companyId;

    private String company;

    private String unit;

    private String dealer;

    private String dealerName;

    private String imgs;

}
