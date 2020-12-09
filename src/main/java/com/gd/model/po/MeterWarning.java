package com.gd.model.po;

import java.util.Date;

import lombok.Data;

@Data
public class MeterWarning {
	
    private Integer id;

	private Long collectTime;
	
	private String meterNo;
	
	private Date createTime;
	
	private String lastSendTime="";
	
	private Integer level1;
	
	private Integer yearmonth;
	
	private String projectNo;
	
	private String projectName;
	
	private String companyId;
	
	private String description;
	
	private String company;
	
	private String unit;
	
	private String estarttime="";
	
	private String eendtime="";
	
	private String eseq="";
	
	String eswitch;
	
}
