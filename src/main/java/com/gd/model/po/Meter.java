package com.gd.model.po;

import java.util.Date;

import lombok.Data;

@Data
public class Meter {
	
	private Integer id;

	private String meterNo;
	
	private String oldMeterNo="";
	
	private String newValue;
	
	private String oldValue="";
	
	private String sim;
	
	private String imei="";
	
	private String imsi;
	
	private String power;
	
	private String deviceNo;
	
	private String meterType="";
	
	private String meterType1="";
	
	private String platform="";
	
	private String platform1="";
	
	private String companyId;
	
	private String company;
	
	private String unit;
	
	private String projectNo;
	
	private Integer status;
	
	private Date createTime;
	
	private String userCode;
	
	private String userName;
	
	private Date installTime;
	
	private String projectName;
	
	private Integer lahezhaStatus;
	
	private String strInstllTime;
	
	private String strStatus;
	
	private String address="";
	
	private int userId;
	
	private String price;

}
