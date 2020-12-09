package com.gdiot.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdiot.mapper.NBEMEventMapper;
import com.gdiot.model.YDEMeterEventPo;
import com.gdiot.service.INBYDEMEventService;

@Service("NBYDEMEventService")
public class NBYDEMEventServiceImpl implements INBYDEMEventService {

	static Logger log = LoggerFactory.getLogger(INBYDEMEventService.class);
	
	@Autowired
	private NBEMEventMapper mNBYDEMEventMapper;

	public Map<String, Object> listNBEMeterData(String dev_id,String eNum,String imei,  
			Long beginTime, Long endTime, String source,int pageNo, int pageSize) {
		Map<String, Object> result = new HashMap<String, Object>();
		int count = mNBYDEMEventMapper.countByCondition(dev_id,eNum,imei, beginTime, endTime, source);
		log.info("mNBYDEMEventMapper count="+ count);
		
		if (pageSize < 1 || pageSize > count) {
			pageSize = 20;
		}
		int totalPage = count / pageSize;
		if (pageNo < 1 || pageSize > totalPage) {
			pageNo = 1;
		}
		
		int limit = pageSize;
		int offset = (pageNo-1) * pageSize;
		
		List<YDEMeterEventPo> list = mNBYDEMEventMapper.selectList(dev_id,eNum,imei, beginTime, endTime,source, limit,offset);
		
		log.info("mNBYDEMEventMapper totalPage="+ totalPage);
		log.info("mNBYDEMEventMapper pageNo="+ pageNo);
		log.info("mNBYDEMEventMapper pageSize="+ pageSize);
		result.put("totalPage", totalPage);
		result.put("pageNo", pageNo);
		result.put("pageSize", pageSize);
		result.put("list", list);
		return result;
	}
	
	public int addOne(YDEMeterEventPo mYDEMeterEventPo) {
		log.info("YDEMeterEventPo EMeter begin addOne");
		return mNBYDEMEventMapper.insertOne(mYDEMeterEventPo);
	}
}
