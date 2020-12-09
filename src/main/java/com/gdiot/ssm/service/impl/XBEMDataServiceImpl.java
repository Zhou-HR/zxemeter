package com.gdiot.ssm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdiot.ssm.dao.GprsDataMapper;
import com.gdiot.ssm.dao.XBEMDataMapper;
import com.gdiot.ssm.entity.GprsDataPo;
import com.gdiot.ssm.entity.XBEMDataPo;
import com.gdiot.ssm.entity.YDEMeterDataPo;
import com.gdiot.ssm.service.IGprsDataService;
import com.gdiot.ssm.service.IXBEMDataService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("XBEMDataService")
public class XBEMDataServiceImpl implements IXBEMDataService {

	@Autowired
	private XBEMDataMapper mXBEMDataMapper;

	public int addOne(XBEMDataPo xbemDataPo) {
		log.info("begin xb em data addOne");
		return mXBEMDataMapper.insertOne(xbemDataPo);
	}

	@Override
	public Map<String, Object> selectList(String dev_id, String eNum, Long beginTime, Long endTime, String source,
			int pageNo, int pageSize) {
		log.info("begin xb em data selectList");
		Map<String, Object> result = new HashMap<String, Object>();
		int count = mXBEMDataMapper.countByCondition(dev_id,eNum, beginTime, endTime, source);
		log.info("selectList count="+ count);
		
		if (pageSize < 1 || pageSize > count) {
			pageSize = 20;
		}
		int totalPage = count / pageSize;
		if (pageNo < 1 || pageSize > totalPage) {
			pageNo = 1;
		}
		
		int limit = pageSize;
		int offset = (pageNo-1) * pageSize;
		
		List<XBEMDataPo> list = mXBEMDataMapper.selectList(dev_id,eNum, beginTime, endTime,source, limit,offset);
		
		log.info("mNBYDEMeterDataMapper totalPage="+ totalPage);
		log.info("mNBYDEMeterDataMapper pageNo="+ pageNo);
		log.info("mNBYDEMeterDataMapper pageSize="+ pageSize);
		result.put("totalPage", totalPage);
		result.put("pageNo", pageNo);
		result.put("pageSize", pageSize);
		result.put("list", list);
		return result;
	}

	@Override
	public List<XBEMDataPo> selectOne(String dev_id, long e_seq) {
		log.info("begin xb em data selectOne");
		List<XBEMDataPo> list = mXBEMDataMapper.selectOne(dev_id,e_seq);
		return list;
	}
}
