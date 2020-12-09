package com.gdiot.model;

import java.io.Serializable;

public class DeviceServiceDataPo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String serviceId;
	private String serviceType;
	private String eventTime;
	// private SmartSocketPo data;
	private DataPo data;

	public String getEventTime() {
		return eventTime;
	}

	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public DataPo getData() {
		return data;
	}

	public void setData(DataPo data) {
		this.data = data;
	}

}
