package com.gdiot.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhouHR
 */
@Data
public class SmokeDataPo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String devId;
    private String origValue;
    private long time;
    private String source;
    private String dataStatus;
    private String createTime;

}
