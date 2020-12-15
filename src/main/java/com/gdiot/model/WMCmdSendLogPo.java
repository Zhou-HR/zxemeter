package com.gdiot.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhouHR
 */
@Data
public class WMCmdSendLogPo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String wmNum;//表号
    private String cmdData;//数据的时间
    private String downIp;//
    private String downPort;//
    private String type;//

}
