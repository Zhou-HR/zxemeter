package com.gdiot.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhouHR
 */
@Data
public class SmokeDownPo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String deviceId;
    private String sendData;
    private String result;
    private String createTime;

}
