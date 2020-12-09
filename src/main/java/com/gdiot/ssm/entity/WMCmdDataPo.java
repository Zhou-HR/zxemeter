package com.gdiot.ssm.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter
public class WMCmdDataPo implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String wmNum;//表号
	private String cmdData;//数据的时间
	private String downStatus;//
	private String type;//

}