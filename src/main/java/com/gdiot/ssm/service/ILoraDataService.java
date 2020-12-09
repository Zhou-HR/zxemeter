package com.gdiot.ssm.service;

import java.util.Map;

import com.gdiot.ssm.entity.LoraDataPo;

public interface ILoraDataService {

	Map<String, Object> listLoraData(String deviceNo, Long beginTime, Long endTime,String source,int pageNo,int pageSize);

	int addOne(LoraDataPo LoraData);
}
