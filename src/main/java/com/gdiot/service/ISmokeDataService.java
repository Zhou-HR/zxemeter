package com.gdiot.service;

import java.util.List;
import java.util.Map;

import com.gdiot.model.SmokeDataPo;
import com.gdiot.model.SmokeDevicePo;
import com.gdiot.model.SmokeDownPo;


public interface ISmokeDataService {

	int insert(SmokeDataPo mSmokeDataPo);
	Map<String, Object> selectbyDevId(String dev_id,int pageNo,int pageSize);
	
	List<SmokeDevicePo> selectAlarmList(String deviceId,String deviceType);
	
	int insertAlarmDown(SmokeDownPo mSmokeDownPo);
}
