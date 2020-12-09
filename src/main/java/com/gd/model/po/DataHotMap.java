package com.gd.model.po;

import java.io.Serializable;

import lombok.Data;

@Data
public class DataHotMap implements Serializable{
	
	private static final long serialVersionUID =1L;

	private String name;
	
	private Integer value;
	
}
