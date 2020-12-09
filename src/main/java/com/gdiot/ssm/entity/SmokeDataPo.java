package com.gdiot.ssm.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhouHR
 */
@Data
public class SmokeDataPo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String dev_id;
    private String orig_value;
    private long time;
    private String source;
    private String data_status;

}
