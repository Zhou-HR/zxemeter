package com.gdiot.ssm.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class Project implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String projectNo;
	
	private String unitcode;
	
	private Integer status;
	
	private String projectName;
	
	private String meterNo;
	
	private String longitude;
	
	private String latitude;
	
	private Integer num=1;
	
	private Integer installNum=0;
	
	private String companyName;
	
	private String companyId;
	
	private String address;
	
	private String carrierName;
	
	private String carrierPart;
	
	private String carrierPrice;
	
	private String userName;
	
	private String userCode;
	
	private String meterStatus1;
	
	private String meterNo1;
	
}
