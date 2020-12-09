package com.gdiot.ssm.entity;

import java.io.Serializable;

public class YDEMNBReadPo implements Serializable {

	private static final long serialVersionUID = 1L;
	//设备上报点抄数值
	private int id;
	private String dev_id ;
	private String imei ;
	private long time;
	private String orig_value ;
	private String e_num;
	private String e_fac;
	private String read_type;
	private String read_value;
	private String data_seq;
	/**
	 * 信息来源:01:NB;02:Lora
	 */
	private String source;
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
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
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getOrig_value() {
		return orig_value;
	}
	public void setOrig_value(String orig_value) {
		this.orig_value = orig_value;
	}
	public String getE_num() {
		return e_num;
	}
	public void setE_num(String e_num) {
		this.e_num = e_num;
	}
	public String getE_fac() {
		return e_fac;
	}
	public void setE_fac(String e_fac) {
		this.e_fac = e_fac;
	}
	public String getRead_type() {
		return read_type;
	}
	public void setRead_type(String read_type) {
		this.read_type = read_type;
	}
	public String getRead_value() {
		return read_value;
	}
	public void setRead_value(String read_value) {
		this.read_value = read_value;
	}
	public String getData_seq() {
		return data_seq;
	}
	public void setData_seq(String data_seq) {
		this.data_seq = data_seq;
	}
	
	
}
