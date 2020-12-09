package com.gdiot.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdiot.mapper.SmokeDataMapper;
import com.gdiot.model.SmokeDataPo;
import com.gdiot.model.SmokeDevicePo;
import com.gdiot.model.SmokeDownPo;
import com.gdiot.service.ISmokeDataService;


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
	public Map<String, Object> selectbyDevId(String dev_id,int pageNo,int pageSize) {
		Map<String, Object> result = new HashMap<String, Object>();
		int count =mSmokeDataMapper.countbyDevId(dev_id);
		log.info("selectbyDevId lora wm count="+ count);
		
		if (pageSize < 1 || pageSize > count) {
			pageSize = 20;
		}
		int totalPage = count / pageSize;
		if (pageNo < 1 || pageSize > totalPage) {
			pageNo = 1;
		}
		
		int limit = pageSize;
		int offset = (pageNo-1) * pageSize;
		
		List<SmokeDataPo> list = mSmokeDataMapper.selectbyDevId(dev_id, limit, offset);
		
		log.info(" totalPage="+ totalPage);
		log.info(" pageNo="+ pageNo);
		log.info(" pageSize="+ pageSize);
		result.put("totalPage", totalPage);
		result.put("pageNo", pageNo);
		result.put("pageSize", pageSize);
		result.put("list", list);
		return result;
	}

	@Override
	public  List<SmokeDevicePo> selectAlarmList(String deviceId, String deviceType) {
//		Map<String, Object> result = new HashMap<String, Object>();
//		int count =mSmokeDataMapper.countbyDevId(deviceId);
//		log.info("selectAlarmList lora count="+ count);
////		
//		if (pageSize < 1 || pageSize > count) {
//			pageSize = 20;
//		}
//		int totalPage = count / pageSize;
//		if (pageNo < 1 || pageSize > totalPage) {
//			pageNo = 1;
//		}
//		
//		int limit = pageSize;
//		int offset = (pageNo-1) * pageSize;
		
		List<SmokeDevicePo> list = mSmokeDataMapper.selectAlarmList(deviceId,deviceType);
		
//		log.info(" totalPage="+ totalPage);
//		log.info(" pageNo="+ pageNo);
//		log.info(" pageSize="+ pageSize);
//		result.put("totalPage", totalPage);
//		result.put("pageNo", pageNo);
//		result.put("pageSize", pageSize);
//		result.put("list", list);
//		return result;
		return list;
	}

	@Override
	public int insertAlarmDown(SmokeDownPo mSmokeDownPo) {
		log.info("insertAlarmDown insert start");
		return mSmokeDataMapper.insertAlarmDown(mSmokeDownPo);
	}


}
