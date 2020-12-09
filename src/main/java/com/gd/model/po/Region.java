package com.gd.model.po;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Region implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;

	private String name;//
	
	private Integer parentId;//
    
	private Integer level;//
	

}
