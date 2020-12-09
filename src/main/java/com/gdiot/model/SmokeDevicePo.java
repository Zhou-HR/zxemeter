package com.gdiot.model;

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ZhouHR
 */
@Data
public class SmokeDevicePo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String deviceId;
    private String deviceType;
    private String createTime;

}
