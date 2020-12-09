package com.gd.model.po;

import java.util.Date;

import lombok.Data;

@Data
public class MeterLostData {
	
	private Integer id;

	private String meterNo;
	
	private Integer seq;
	
	private String imei;
	
	private String result;
	
	private Integer deal;
	
	private Date createTime;
	
	private Date updateTime;
	
	private Integer dealtime;
	
	private Integer success;
	
	private String companyId;
	
	private String company;
	
	private String unit;
	
}
