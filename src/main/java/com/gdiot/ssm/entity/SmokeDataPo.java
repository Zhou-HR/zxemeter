package com.gdiot.ssm.entity;

import java.io.Serializable;

public class SmokeDataPo implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String dev_id;
	private String orig_value;
	private long time;
	private String source;
	private String data_status;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDev_id() {
		return dev_id;
	}
	public void setDev_id(String dev_id) {
		this.dev_id = dev_id;
	}
	public String getOrig_value() {
		return orig_value;
	}
	public void setOrig_value(String orig_value) {
		this.orig_value = orig_value;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getData_status() {
		return data_status;
	}
	public void setData_status(String data_status) {
		this.data_status = data_status;
	}
	
}
