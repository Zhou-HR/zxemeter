package com.gdiot.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhouHR
 */
@Data
public class DeviceServiceDataPo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String serviceId;
    private String serviceType;
    private String eventTime;
    // private SmartSocketPo data;
    private DataPo data;

}
