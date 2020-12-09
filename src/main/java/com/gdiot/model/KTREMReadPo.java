package com.gdiot.model;

import lombok.Data;

/**
 * @author ZhouHR
 */
@Data
public class KTREMReadPo {
    private int id;
    private String devId;
    private long time;
    private String eOrigValue;
    private String eNum;
    private String eReadType;
    private String eReadValue;
    private String eDataSeq;
    private String eSource;
    private String eFac;

}
