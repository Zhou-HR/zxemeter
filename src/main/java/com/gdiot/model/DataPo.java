package com.gdiot.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhouHR
 */
@Data
public class DataPo implements Serializable {
    private static final long serialVersionUID = 1L;
    private int RSSI;
    private Integer length;
    private String payload;

}
