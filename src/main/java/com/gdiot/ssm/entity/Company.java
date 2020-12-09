package com.gdiot.ssm.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class Company implements Serializable{
	
	private static final long serialVersionUID =1L;

	private String companyId;
	
	private String companyName;
	
}
