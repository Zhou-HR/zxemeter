package com.gdiot.ssm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdiot.ssm.dao.NBYDEMStatusMapper;
import com.gdiot.ssm.entity.YDEMeterDataPo;
import com.gdiot.ssm.entity.YDEMeterStatusPo;
import com.gdiot.ssm.service.INBYDEMStatusService;

@Service("nbYDEMStatusService")
public class NBYDEMStatusServiceImpl implements INBYDEMStatusService {

	static Logger log = LoggerFactory.getLogger(INBYDEMStatusService.class);
	
	@Autowired
	private NBYDEMStatusMapper mNBYDEMStatusMapper;

	public Map<String, Object> selectStatus(String dev_id,String imei,String status,int pageNo, int pageSize) {
		Map<String, Object> result = new HashMap<String, Object>();
		int count = mNBYDEMStatusMapper.countByCondition(dev_id,imei,status);
		log.info("NBYDEMStatusMapper count="+ count);
		
		if (pageSize < 1 || pageSize > count) {
			pageSize = 20;
		}
		int totalPage = count / pageSize;
		if (pageNo < 1 || pageSize > totalPage) {
			pageNo = 1;
		}
		
		int limit = pageSize;
		int offset = (pageNo-1) * pageSize;
		
		List<YDEMeterStatusPo> list = mNBYDEMStatusMapper.selectStatus(dev_id,imei,status, limit,offset);
		
		log.info("NBYDEMStatusMapper totalPage="+ totalPage);
		log.info("NBYDEMStatusMapper pageNo="+ pageNo);
		log.info("NBYDEMStatusMapper pageSize="+ pageSize);
		result.put("totalPage", totalPage);
		result.put("pageNo", pageNo);
		result.put("pageSize", pageSize);
		result.put("list", list);
		return result;
	}

	public int insertStatus(YDEMeterStatusPo mYDEMeterStatusPo) {
		log.info("mNBYDEMStatusMapper EMeter begin insertStatus");
		return mNBYDEMStatusMapper.insertStatus(mYDEMeterStatusPo);
	}
	public int updateEMStatus(YDEMeterStatusPo mYDEMeterStatusPo) {
		log.info("mNBYDEMStatusMapper EMeter begin updateEMStatus");
		return mNBYDEMStatusMapper.updateEMStatus(mYDEMeterStatusPo);
	}
	public List<YDEMeterStatusPo> selectDevid(String dev_id,String imei){
		List<YDEMeterStatusPo> list = mNBYDEMStatusMapper.selectDevid(dev_id,imei);
		log.info("mNBYDEMStatusMapper selectDevid list.size()="+ list.size());
		return list;
	}

}
