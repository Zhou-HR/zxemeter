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
import com.gdiot.service.INBYDEMReadService;

/**
 * @author ZhouHR
 */
@Service("NBYDEMReadService")
public class NBYDEMReadServiceImpl implements INBYDEMReadService {

    static Logger log = LoggerFactory.getLogger(INBYDEMReadService.class);

    @Autowired
    private NBEMReadMapper mNBEMReadMapper;

    @Override
    public List<YDEMNBReadPo> listNBEMRead(String imei, String type, String data_seq) {
//		Map<String, Object> result = new HashMap<String, Object>();
        List<YDEMNBReadPo> list = mNBEMReadMapper.selectListByseq(imei, type, data_seq);
//		result.put("list", list);
        return list;
    }

    @Override
    public Map<String, Object> listGetAllRead(String imei) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<YDEMNBReadPo> list = mNBEMReadMapper.selectAllList(imei);
        result.put("list", list);
        return result;
    }

    @Override
    public int addOne(YDEMNBReadPo mYDEMNBReadPo) {
        log.info("YDEMNBReadPo EMeter begin addOne");
        return mNBEMReadMapper.insertOne(mYDEMNBReadPo);
    }
}
