package com.gdiot.model;

import lombok.Data;

/**
 * @author ZhouHR
 */
@Data
public class QDEMReadPo {

    //设备上报点抄数值
    private int id;
    private String devId;
    private String imei;
    private long time;
    private String origValue;
    private String eNum;
    private String readType;
    private String readValue;
    private String dataSeq;
    /**
     * 信息来源:01:NB;02:Lora
     */
    private String source;
}
