package com.gdiot.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhouHR
 */
@Data
public class DeviceDataChangedPo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String notifyType;
    private String requestId;
    private String deviceId;
    private String gatewayId;
    private DeviceServiceDataPo service;


}
