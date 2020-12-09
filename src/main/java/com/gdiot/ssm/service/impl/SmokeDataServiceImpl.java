package com.gdiot.ssm.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdiot.ssm.dao.SmokeDataMapper;
import com.gdiot.ssm.entity.SmokeDataPo;
import com.gdiot.ssm.service.ISmokeDataService;


@Service("ISmokeDataService")
public class SmokeDataServiceImpl implements ISmokeDataService {
	
	private Logger log = LoggerFactory.getLogger(SmokeDataServiceImpl.class);

	@Autowired
	private SmokeDataMapper mSmokeDataMapper;

	@Override
	public int insert(SmokeDataPo mSmokeDataPo) {
		log.info("begin addOne");
		return mSmokeDataMapper.insertOne(mSmokeDataPo);
	}

	@Override
	public List<SmokeDataPo> selectbyDevId(String dev_id,int pageNo,int pageSize) {
		int count =mSmokeDataMapper.countbyDevId(dev_id);
		log.info("selectbyDevId lora wm count="+ count);
		int limit = pageSize;
		int offset = (pageNo-1) * pageSize;
		
		List<SmokeDataPo> list = mSmokeDataMapper.selectbyDevId(dev_id, limit, offset);
		return list;
	}


}
