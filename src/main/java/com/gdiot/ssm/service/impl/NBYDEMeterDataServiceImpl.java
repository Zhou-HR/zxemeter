package com.gdiot.ssm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdiot.ssm.dao.NBYDEMeterDataMapper;
import com.gdiot.ssm.entity.YDEMeterDataPo;
import com.gdiot.ssm.service.INBYDEMeterDataService;

@Service("nbYDMeterDataService")
public class NBYDEMeterDataServiceImpl implements INBYDEMeterDataService {

	static Logger log = LoggerFactory.getLogger(INBYDEMeterDataService.class);
	
	@Autowired
	private NBYDEMeterDataMapper mNBYDEMeterDataMapper;

	public Map<String, Object> listNBEMeterData(String dev_id,String eNum,String imei,  
			Long beginTime, Long endTime, String source,int pageNo, int pageSize) {
		Map<String, Object> result = new HashMap<String, Object>();
		int count = mNBYDEMeterDataMapper.countByCondition(dev_id,eNum,imei, beginTime, endTime, source);
		log.info("mNBYDEMeterDataMapper count="+ count);
		
		if (pageSize < 1 || pageSize > count) {
			pageSize = 20;
		}
		int totalPage = count / pageSize;
		if (pageNo < 1 || pageSize > totalPage) {
			pageNo = 1;
		}
		
		int limit = pageSize;
		int offset = (pageNo-1) * pageSize;
		
		List<YDEMeterDataPo> list = mNBYDEMeterDataMapper.selectList(dev_id,eNum,imei, beginTime, endTime,source, limit,offset);
		for(YDEMeterDataPo item:list) {
			if("01".equals(item.getSource())) {
				item.setSource("nb");
			}else if("02".equals(item.getSource())) {
				item.setSource("lora");
			}
		}
		log.info("mNBYDEMeterDataMapper totalPage="+ totalPage);
		log.info("mNBYDEMeterDataMapper pageNo="+ pageNo);
		log.info("mNBYDEMeterDataMapper pageSize="+ pageSize);
		result.put("totalPage", totalPage);
		result.put("pageNo", pageNo);
		result.put("pageSize", pageSize);
		result.put("list", list);
		return result;
	}
//	public Map<String, Object> selectbyimei(String imei){
//		Map<String, Object> result = new HashMap<String, Object>();
//		List<YDEMeterDataPo> list = mNBYDEMeterDataMapper.selectbyimei(imei);
//		log.info("mNBYDEMeterDataMapper list.size()="+ list.size());
//		result.put("list", list);
//		return result;
//	}
	public List<YDEMeterDataPo> selectbyimei(String imei){
//		Map<String, Object> result = new HashMap<String, Object>();
		List<YDEMeterDataPo> list = mNBYDEMeterDataMapper.selectbyimei(imei);
		log.info("mNBYDEMeterDataMapper list.size()="+ list.size());
//		result.put("list", list);
		return list;
	}
	
	public int addOne(YDEMeterDataPo mYDEMeterDataPo) {
		log.info("NBYDEMeterDataServiceImpl EMeter begin addOne");
		return mNBYDEMeterDataMapper.insertOne(mYDEMeterDataPo);
	}
	
	@Override
	public int updateData(YDEMeterDataPo mYDEMeterDataPo) {
		log.info("NBYDEMeterDataServiceImpl updateData--");
		return mNBYDEMeterDataMapper.updateData(mYDEMeterDataPo);
	}
	@Override
	public List<YDEMeterDataPo> selectbyseq(String imei, long e_seq) {
		List<YDEMeterDataPo> list = mNBYDEMeterDataMapper.selectbyseq(imei,e_seq);
		log.info("mNBYDEMeterDataMapper selectbyseq list.size()="+ list.size());
		return list;
	}
}
