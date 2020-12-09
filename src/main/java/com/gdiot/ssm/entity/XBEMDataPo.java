package com.gdiot.ssm.entity;

import java.io.Serializable;

public class XBEMDataPo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String dev_id;
	private String orig_value;
	private long time;
	/**
	 * 信息来源:nb;lora;mqtt_2g
	 */
	private String source;
	
	//解析出的数据
	private String e_num;//表号
	private String e_fac;//厂商
	private String e_time;//数据的时间
	private long e_seq;//序列号
	private int flag_reload;
	private String seq_type;
	private String e_signal;//信号
	private String data_type;//上报数据类型，日冻结，月冻结，整点冻结
	
	private String e_kwh1= "";//有功电能 同度数 e_readings
	private String e_kwh2= "";//无功电能
	private String e_kw1_all= "";//有功总功率
	private String e_kw2_all= "";//无功总功率
	private String e_voltage_a= "";//电压 同e_vlotage
	private String e_voltage_b= "";
	private String e_voltage_c= "";
	private String e_current_a= "";//A相电流 同e_current
	private String e_current_b= "";//电流
	private String e_current_c= "";//电流
	private String e_factor_all= "";//总功率因数
	private String e_statu1= "";//电表状态字1
	private String e_statu2= "";//电表状态字2
	private String e_switch= "";//继电器状态
	
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
	public long getE_seq() {
		return e_seq;
	}
	public void setE_seq(long e_seq) {
		this.e_seq = e_seq;
	}
	public int getFlag_reload() {
		return flag_reload;
	}
	public void setFlag_reload(int flag_reload) {
		this.flag_reload = flag_reload;
	}
	public String getSeq_type() {
		return seq_type;
	}
	public void setSeq_type(String seq_type) {
		this.seq_type = seq_type;
	}
	public String getData_type() {
		return data_type;
	}
	public void setData_type(String data_type) {
		this.data_type = data_type;
	}
	public String getE_kwh1() {
		return e_kwh1;
	}
	public void setE_kwh1(String e_kwh1) {
		this.e_kwh1 = e_kwh1;
	}
	public String getE_kwh2() {
		return e_kwh2;
	}
	public void setE_kwh2(String e_kwh2) {
		this.e_kwh2 = e_kwh2;
	}
	public String getE_kw1_all() {
		return e_kw1_all;
	}
	public void setE_kw1_all(String e_kw1_all) {
		this.e_kw1_all = e_kw1_all;
	}
	public String getE_kw2_all() {
		return e_kw2_all;
	}
	public void setE_kw2_all(String e_kw2_all) {
		this.e_kw2_all = e_kw2_all;
	}
	public String getE_voltage_a() {
		return e_voltage_a;
	}
	public void setE_voltage_a(String e_voltage_a) {
		this.e_voltage_a = e_voltage_a;
	}
	public String getE_voltage_b() {
		return e_voltage_b;
	}
	public void setE_voltage_b(String e_voltage_b) {
		this.e_voltage_b = e_voltage_b;
	}
	public String getE_voltage_c() {
		return e_voltage_c;
	}
	public void setE_voltage_c(String e_voltage_c) {
		this.e_voltage_c = e_voltage_c;
	}
	public String getE_current_a() {
		return e_current_a;
	}
	public void setE_current_a(String e_current_a) {
		this.e_current_a = e_current_a;
	}
	public String getE_current_b() {
		return e_current_b;
	}
	public void setE_current_b(String e_current_b) {
		this.e_current_b = e_current_b;
	}
	public String getE_current_c() {
		return e_current_c;
	}
	public void setE_current_c(String e_current_c) {
		this.e_current_c = e_current_c;
	}
	public String getE_factor_all() {
		return e_factor_all;
	}
	public void setE_factor_all(String e_factor_all) {
		this.e_factor_all = e_factor_all;
	}
	public String getE_statu1() {
		return e_statu1;
	}
	public void setE_statu1(String e_statu1) {
		this.e_statu1 = e_statu1;
	}
	public String getE_statu2() {
		return e_statu2;
	}
	public void setE_statu2(String e_statu2) {
		this.e_statu2 = e_statu2;
	}
	public String getE_switch() {
		return e_switch;
	}
	public void setE_switch(String e_switch) {
		this.e_switch = e_switch;
	}
	
	
}
