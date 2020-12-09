package com.gdiot.ssm.entity;

import java.io.Serializable;

public class NBDXDataPo implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String dev_id;
	private String orig_value;
	private long time;
	/**
	 * 信息来源:01:NB;02:Lora;03:2G
	 */
	private String source;
	
	//解析出的数据
	private String e_readings;//度数
	private String e_voltage;//电压
	private String e_current;//电流
	private String e_signal;//信号
	private String e_num;//表号
	private String e_fac;//厂商
	private String e_time;//数据的时间
	
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
	public String getE_readings() {
		return e_readings;
	}
	public void setE_readings(String e_readings) {
		this.e_readings = e_readings;
	}
	public String getE_voltage() {
		return e_voltage;
	}
	public void setE_voltage(String e_voltage) {
		this.e_voltage = e_voltage;
	}
	public String getE_current() {
		return e_current;
	}
	public void setE_current(String e_current) {
		this.e_current = e_current;
	}
	public String getE_signal() {
		return e_signal;
	}
	public void setE_signal(String e_signal) {
		this.e_signal = e_signal;
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
	public String getE_time() {
		return e_time;
	}
	public void setE_time(String e_time) {
		this.e_time = e_time;
	}
	
	
}
