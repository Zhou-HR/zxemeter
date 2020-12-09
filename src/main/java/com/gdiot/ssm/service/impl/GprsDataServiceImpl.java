package com.gdiot.ssm.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdiot.ssm.dao.GprsDataMapper;
import com.gdiot.ssm.entity.GprsDataPo;
import com.gdiot.ssm.service.IGprsDataService;

@Service("GprsDataService")
public class GprsDataServiceImpl implements IGprsDataService {
	
	private Logger log = LoggerFactory.getLogger(GprsDataServiceImpl.class);

	@Autowired
	private GprsDataMapper mGprsDataMapper;

	public int addOne(GprsDataPo gprsData) {
		log.info("begin gprs data addOne");
		return mGprsDataMapper.insertOne(gprsData);
	}

}
