package com.gd.model.po;

import java.util.Date;

import lombok.Data;

/**
 * @author ZhouHR
 */
@Data
public class OfflineReport {

    private Integer id;

    private Long collectTime;

    private String meterNo;

    private Date createTime;

    private String checkTime;

    private String installTime;

    private String lastSendTime;

    private Integer level1;

    private Integer yearmonth;

    private String projectNo;

    private String projectName;

    private String companyId;

    private String description;

    private String company;

    private String unit;

    private String estarttime;

    private String eendtime;

}
