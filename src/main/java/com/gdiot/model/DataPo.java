package com.gdiot.model;

import java.io.Serializable;

public class DataPo implements Serializable {
	private static final long serialVersionUID = 1L;
	private int RSSI;
	private Integer length;
	private String payload;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getRSSI() {
		return RSSI;
	}

	public void setRSSI(int RSSI) {
		this.RSSI = RSSI;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}
}
