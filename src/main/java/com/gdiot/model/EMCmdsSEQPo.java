package com.gdiot.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhouHR
 */
@Data
public class EMCmdsSEQPo implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String imei;
    private String e_num;
    private String cmd_seq;
    private String data_seq;
    private Date create_time;
}
