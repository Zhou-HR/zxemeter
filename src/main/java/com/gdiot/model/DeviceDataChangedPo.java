package com.gdiot.model;

import java.io.Serializable;

public class DeviceDataChangedPo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String notifyType;
	private String requestId;
	private String deviceId;
	private String gatewayId;
	private DeviceServiceDataPo service;

	public String getNotifyType() {
		return notifyType;
	}

	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
	}

	public DeviceServiceDataPo getService() {
		return service;
	}

	public void setService(DeviceServiceDataPo service) {
		this.service = service;
	}

}
