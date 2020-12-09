package com.gdiot.ssm.entity;

import java.io.Serializable;

public class YDEMeterEventPo implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String dev_id;
	private String orig_value;
	private long time;
	private String imei;
	
	//解析出的数据
	private String event_type;
	private String e_num;//表号
	private String e_fac;//厂商
	//解析出的数据
	private long e_seq;//序列号
	private String e_start_time;//事件开始的时间
	private String e_end_time;//事件结束的时间
	private String e_start_kwh;//事件开始电量
	private String e_start_kwh2;//事件开始电量
	private String e_end_kwh;//事件结束电量
	private String e_end_kwh2;//事件结束电量
	private String e_start_voltage_a;//事件开始电压
	private String e_start_voltage_b;//事件开始电压
	private String e_start_voltage_c;//事件开始电压
	private String e_end_voltage_a;//事件结束电压
	private String e_end_voltage_b;//事件结束电压
	private String e_end_voltage_c;//事件结束电压
	private String e_start_current_a;//事件开始电流
	private String e_start_current_b;//事件开始电流
	private String e_start_current_c;//事件开始电流
	private String e_end_current_a;//事件结束电流
	private String e_end_current_b;//事件结束电流
	private String e_end_current_c;//事件结束电流
	private String e_start_switch;//停电事件 事件开始继电器状态
	private String e_end_switch;//停电事件 事件结束继电器状态
	private String e_start_status1;// 事件开始 状态字1
	private String e_end_status1;//事件结束 状态字1
	private String e_start_status2;// 事件开始 状态字2
	private String e_end_status2;// 事件结束 状态字2
	private String e_start_module;//模块状态监控字
	private String e_end_module;//模块状态监控字
	private int flag_reload;
	/**
	 * 信息来源:01:NB;02:Lora
	 */
	private String source;
	private int deal_flag;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getDev_id() {
		return dev_id;
	}
	public void setDev_id(String dev_id) {
		this.dev_id = dev_id;
	}
	
	public String getE_fac() {
		return e_fac;
	}
	public void setE_fac(String e_fac) {
		this.e_fac = e_fac;
	}
	
	public long getE_seq() {
		return e_seq;
	}
	public void setE_seq(long e_seq) {
		this.e_seq = e_seq;
	}
	public String getEvent_type() {
		return event_type;
	}
	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}
	public String getE_start_time() {
		return e_start_time;
	}
	public void setE_start_time(String e_start_time) {
		this.e_start_time = e_start_time;
	}
	public String getE_end_time() {
		return e_end_time;
	}
	public void setE_end_time(String e_end_time) {
		this.e_end_time = e_end_time;
	}
	public String getE_start_kwh() {
		return e_start_kwh;
	}
	public void setE_start_kwh(String e_start_kwh) {
		this.e_start_kwh = e_start_kwh;
	}
	public String getE_end_kwh() {
		return e_end_kwh;
	}
	public void setE_end_kwh(String e_end_kwh) {
		this.e_end_kwh = e_end_kwh;
	}
	
	public String getE_start_kwh2() {
		return e_start_kwh2;
	}
	public void setE_start_kwh2(String e_start_kwh2) {
		this.e_start_kwh2 = e_start_kwh2;
	}
	public String getE_end_kwh2() {
		return e_end_kwh2;
	}
	public void setE_end_kwh2(String e_end_kwh2) {
		this.e_end_kwh2 = e_end_kwh2;
	}
	public String getE_start_voltage_a() {
		return e_start_voltage_a;
	}
	public void setE_start_voltage_a(String e_start_voltage_a) {
		this.e_start_voltage_a = e_start_voltage_a;
	}
	public String getE_start_voltage_b() {
		return e_start_voltage_b;
	}
	public void setE_start_voltage_b(String e_start_voltage_b) {
		this.e_start_voltage_b = e_start_voltage_b;
	}
	public String getE_start_voltage_c() {
		return e_start_voltage_c;
	}
	public void setE_start_voltage_c(String e_start_voltage_c) {
		this.e_start_voltage_c = e_start_voltage_c;
	}
	public String getE_end_voltage_a() {
		return e_end_voltage_a;
	}
	public void setE_end_voltage_a(String e_end_voltage_a) {
		this.e_end_voltage_a = e_end_voltage_a;
	}
	public String getE_end_voltage_b() {
		return e_end_voltage_b;
	}
	public void setE_end_voltage_b(String e_end_voltage_b) {
		this.e_end_voltage_b = e_end_voltage_b;
	}
	public String getE_end_voltage_c() {
		return e_end_voltage_c;
	}
	public void setE_end_voltage_c(String e_end_voltage_c) {
		this.e_end_voltage_c = e_end_voltage_c;
	}
	public String getE_start_current_a() {
		return e_start_current_a;
	}
	public void setE_start_current_a(String e_start_current_a) {
		this.e_start_current_a = e_start_current_a;
	}
	public String getE_start_current_b() {
		return e_start_current_b;
	}
	public void setE_start_current_b(String e_start_current_b) {
		this.e_start_current_b = e_start_current_b;
	}
	public String getE_start_current_c() {
		return e_start_current_c;
	}
	public void setE_start_current_c(String e_start_current_c) {
		this.e_start_current_c = e_start_current_c;
	}
	public String getE_end_current_a() {
		return e_end_current_a;
	}
	public void setE_end_current_a(String e_end_current_a) {
		this.e_end_current_a = e_end_current_a;
	}
	public String getE_end_current_b() {
		return e_end_current_b;
	}
	public void setE_end_current_b(String e_end_current_b) {
		this.e_end_current_b = e_end_current_b;
	}
	public String getE_end_current_c() {
		return e_end_current_c;
	}
	public void setE_end_current_c(String e_end_current_c) {
		this.e_end_current_c = e_end_current_c;
	}
	public String getE_start_switch() {
		return e_start_switch;
	}
	public void setE_start_switch(String e_start_switch) {
		this.e_start_switch = e_start_switch;
	}
	public String getE_end_switch() {
		return e_end_switch;
	}
	public void setE_end_switch(String e_end_switch) {
		this.e_end_switch = e_end_switch;
	}
	public String getE_start_status1() {
		return e_start_status1;
	}
	public void setE_start_status1(String e_start_status1) {
		this.e_start_status1 = e_start_status1;
	}
	public String getE_end_status1() {
		return e_end_status1;
	}
	public void setE_end_status1(String e_end_status1) {
		this.e_end_status1 = e_end_status1;
	}
	public String getE_start_status2() {
		return e_start_status2;
	}
	public void setE_start_status2(String e_start_status2) {
		this.e_start_status2 = e_start_status2;
	}
	public String getE_end_status2() {
		return e_end_status2;
	}
	public void setE_end_status2(String e_end_status2) {
		this.e_end_status2 = e_end_status2;
	}
	public String getE_start_module() {
		return e_start_module;
	}
	public void setE_start_module(String e_start_module) {
		this.e_start_module = e_start_module;
	}
	public String getE_end_module() {
		return e_end_module;
	}
	public void setE_end_module(String e_end_module) {
		this.e_end_module = e_end_module;
	}
	public int getFlag_reload() {
		return flag_reload;
	}
	public void setFlag_reload(int flag_reload) {
		this.flag_reload = flag_reload;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public int getDeal_flag() {
		return deal_flag;
	}
	public void setDeal_flag(int deal_flag) {
		this.deal_flag = deal_flag;
	}
	
}
