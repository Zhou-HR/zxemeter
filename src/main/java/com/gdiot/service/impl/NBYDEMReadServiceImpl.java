package com.gdiot.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdiot.mapper.NBEMReadMapper;
import com.gdiot.model.YDEMNBReadPo;
import com.gdiot.service.INBYDEMEventService;
import com.gdiot.service.INBYDEMReadService;

@Service("NBYDEMReadService")
public class NBYDEMReadServiceImpl implements INBYDEMReadService {

	static Logger log = LoggerFactory.getLogger(INBYDEMEventService.class);
	
	@Autowired
	private NBEMReadMapper mNBEMReadMapper;

	@Override
	public Map<String, Object> listNBEMRead(String dev_id, String eNum, String imei, Long beginTime, Long endTime,
			String source, int pageNo, int pageSize) {
		Map<String, Object> result = new HashMap<String, Object>();
		int count = mNBEMReadMapper.countByCondition(dev_id,eNum,imei, beginTime, endTime, source);
		log.info("mNBEMReadMapper count="+ count);
		
		if (pageSize < 1 || pageSize > count) {
			pageSize = 20;
		}
		int totalPage = count / pageSize;
		if (pageNo < 1 || pageSize > totalPage) {
			pageNo = 1;
		}
		
		int limit = pageSize;
		int offset = (pageNo-1) * pageSize;
		
		List<YDEMNBReadPo> list = mNBEMReadMapper.selectList(dev_id,eNum,imei, beginTime, endTime,source, limit,offset);
		
		log.info("mNBEMReadMapper totalPage="+ totalPage);
		log.info("mNBEMReadMapper pageNo="+ pageNo);
		log.info("mNBEMReadMapper pageSize="+ pageSize);
		result.put("totalPage", totalPage);
		result.put("pageNo", pageNo);
		result.put("pageSize", pageSize);
		result.put("list", list);
		return result;
	}

	@Override
	public int addOne(YDEMNBReadPo mYDEMNBReadPo) {
		log.info("YDEMNBReadPo EMeter begin addOne");
		return mNBEMReadMapper.insertOne(mYDEMNBReadPo);
	}
}
