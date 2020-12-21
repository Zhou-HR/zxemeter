package com.gdiot.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * em_read_qd
 *
 * @author ZhouHR
 */
@Data
public class QDEMReadPo implements Serializable {
    private Integer id;

    private String devId;

    private String imei;

    private String origValue;

    private Long time;

    private Date createTime;

    private String eNum;

    private String readType;

    private String readValue;

    private String dataSeq;

    private String source;

    private static final long serialVersionUID = 1L;
}