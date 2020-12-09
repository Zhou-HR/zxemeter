package com.gdiot.service;

import java.util.Map;

import com.gdiot.model.YDEMNBReadPo;

public interface INBYDEMReadService {
	
	Map<String, Object> listNBEMRead(String dev_id,String eNum,String imei, Long beginTime, Long endTime,String source,int pageNo,int pageSize);
	int addOne(YDEMNBReadPo mYDEMNBReadPo);

}
