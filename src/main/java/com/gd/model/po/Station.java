package com.gd.model.po;

import java.util.Date;

import lombok.Data;

@Data
public class Station {
	
	private String projectNo;
	
	private String projectName;
	
	private String longitude;
	
	private String latitude;
	
	private String address;
	
	private String cityname;
	
	private String unitname;
	
	private String projectDate;
	
	private Integer meterStatus1;
	
	private String carrierName;
	
	private String carrierPart;
	
	private String carrierPrice;
	
	private String meterNo1="";
	
	private String gdmapJd;
	
	private String gdmapWd;
	
	
}
