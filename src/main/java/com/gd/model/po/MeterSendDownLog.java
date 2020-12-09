package com.gd.model.po;

import java.util.Date;

import lombok.Data;

@Data
public class MeterSendDownLog {
	
	private Integer id;

	private String meterNo="";
	
	private String name;
	
	private String imei="";
	
	private String param;
	
	private String result="超时";
	
	private Date createDate;
	
	private String user1;
	
	private String userName;
	

}
