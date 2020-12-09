package com.gd.model.po;

import lombok.Data;

@Data
public class TbMeterInfo {
	
	private String projCode;
	
	private String oldMeterNumber;
	
	private String oldMeterValue;
	
	private String newMeterNumber;
	
	private String newMeterValue;
	
	private String createTime;
	
	private String companyId;
	
	private Integer ifUseNewBox=0;
	
	private Integer ifStateGrid=0;
	
	private String cityname;
	
	private String unitname;
	
}
