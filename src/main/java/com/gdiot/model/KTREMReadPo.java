package com.gdiot.model;
public class KTREMReadPo {
	private int id;
	private String devId ;
	private long time;
	private String eOrigValue ;
	private String eNum;
	private String eReadType;
	private String eReadValue;
	private String eDataSeq;
	private String eSource;
	private String eFac;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDevId() {
		return devId;
	}

	public void setDevId(String devId) {
		this.devId = devId;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}


	public String geteNum() {
		return eNum;
	}

	public void seteNum(String eNum) {
		this.eNum = eNum;
	}

	public String geteOrigValue() {
		return eOrigValue;
	}

	public void seteOrigValue(String eOrigValue) {
		this.eOrigValue = eOrigValue;
	}

	public String geteReadType() {
		return eReadType;
	}

	public void seteReadType(String eReadType) {
		this.eReadType = eReadType;
	}

	public String geteReadValue() {
		return eReadValue;
	}

	public void seteReadValue(String eReadValue) {
		this.eReadValue = eReadValue;
	}

	public String geteDataSeq() {
		return eDataSeq;
	}

	public void seteDataSeq(String eDataSeq) {
		this.eDataSeq = eDataSeq;
	}

	public String geteSource() {
		return eSource;
	}

	public void seteSource(String eSource) {
		this.eSource = eSource;
	}

	public String geteFac() {
		return eFac;
	}

	public void seteFac(String eFac) {
		this.eFac = eFac;
	}
}
