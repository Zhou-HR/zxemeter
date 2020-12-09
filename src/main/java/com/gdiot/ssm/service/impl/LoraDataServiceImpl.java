package com.gdiot.ssm.service.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdiot.ssm.dao.LoraDataMapper;
import com.gdiot.ssm.entity.LoraDataPo;
import com.gdiot.ssm.service.ILoraDataService;

@Service("LoraDataService")
public class LoraDataServiceImpl implements ILoraDataService {
	
	private Logger log = LoggerFactory.getLogger(LoraDataServiceImpl.class);

	@Autowired
	private LoraDataMapper mLoraDataMapper;

	public Map<String, Object> listLoraData(String deviceNo, Long beginTime, Long endTime, String source,
			int pageNo, int pageSize) {
		Map<String, Object> result = new HashMap<String, Object>();
		int count = mLoraDataMapper.countByCondition(deviceNo, beginTime, endTime, source);
		log.info("LoraServiceImpl lora count="+ count);
		if (pageSize < 1 || pageSize > count) {
			pageSize = 20;
		}
		int totalPage = count / pageSize;
		if (pageNo < 1 || pageSize > totalPage) {
			pageNo = 1;
		}
		
		int limit = pageSize;
		int offset = (pageNo-1) * pageSize;
		List<LoraDataPo> list = mLoraDataMapper.selectList(deviceNo, beginTime, endTime, source, limit,
				offset);
//		if (!StringUtils.isBlank(deviceNo) && !list.isEmpty()) {
//			BigDecimal cnt = list.get(0).getValue().subtract(list.get(list.size() - 1).getValue(),
//					MathContext.UNLIMITED);
//			result.put("cnt", cnt);
//		}
		for (LoraDataPo item : list) {
			if ("01".equals(item.getSource())) {
				item.setSource("nb");
			} else if ("02".equals(item.getSource())) {
				item.setSource("lora");
			}
		}
		log.info("LoraServiceImpl lora totalPage="+ totalPage);
		log.info("LoraServiceImpl lora pageNo="+ pageNo);
		log.info("LoraServiceImpl lora pageSize="+ pageSize);
		
		result.put("totalPage", totalPage);
		result.put("pageNo", pageNo);
		result.put("pageSize", pageSize);
		result.put("list", list);
		return result;
	}

	public int addOne(LoraDataPo LoraData) {
		log.info("begin addOne");
		return mLoraDataMapper.insertOne(LoraData);
	}

}
