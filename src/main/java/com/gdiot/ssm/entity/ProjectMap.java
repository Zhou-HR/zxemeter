package com.gdiot.ssm.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class ProjectMap implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String longitude="0";
	
	private String latitude="0";
	
	private Integer num=0;
	
	private Integer installNum=0;
	
	private String companyName;
	
	private String companyId;
	
}
