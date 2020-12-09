package com.gdiot.ssm.service;

import java.util.Map;

import com.gdiot.ssm.entity.YDEMeterEventPo;

public interface INBYDEMEventService {
	
	Map<String, Object> listNBEMeterData(String dev_id,String eNum,String imei, Long beginTime, Long endTime,String source,int pageNo,int pageSize);
	int addOne(YDEMeterEventPo mYDEMeterDataPo);

	
}
