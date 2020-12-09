package com.gdiot.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdiot.mapper.AKREMDataMapper;
import com.gdiot.model.AKREMDataPo;
import com.gdiot.model.AKREMEventPo;
import com.gdiot.model.AKREMReadPo;
import com.gdiot.service.IAKREMDataService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhouHR
 */
@Slf4j
@Service("AKREMDataService")
public class AKREMDataServiceImpl implements IAKREMDataService {

    @Autowired
    private AKREMDataMapper mAKREMDataMapper;

    @Override
    public int addOne(AKREMDataPo mAKREMDataPo) {
        log.info("begin akr em data addOne");
        return mAKREMDataMapper.insertOne(mAKREMDataPo);
    }

    @Override
    public Map<String, Object> selectList(String dev_id, String eNum, Long beginTime, Long endTime, String source,
                                          int pageNo, int pageSize) {
        log.info("begin akr em data selectList");
        Map<String, Object> result = new HashMap<String, Object>();
        int count = mAKREMDataMapper.countByCondition(dev_id, eNum, beginTime, endTime, source);
        log.info("selectList count=" + count);

        if (pageSize < 1 || pageSize > count) {
            pageSize = 20;
        }
        int totalPage = count / pageSize;
        if (pageNo < 1 || pageSize > totalPage) {
            pageNo = 1;
        }

        int limit = pageSize;
        int offset = (pageNo - 1) * pageSize;

        List<AKREMDataPo> list = mAKREMDataMapper.selectList(dev_id, eNum, beginTime, endTime, source, limit, offset);

        log.info("mNBYDEMeterDataMapper totalPage=" + totalPage);
        log.info("mNBYDEMeterDataMapper pageNo=" + pageNo);
        log.info("mNBYDEMeterDataMapper pageSize=" + pageSize);
        result.put("totalPage", totalPage);
        result.put("pageNo", pageNo);
        result.put("pageSize", pageSize);
        result.put("list", list);
        return result;
    }

    @Override
    public List<AKREMDataPo> selectOne(String dev_id, long e_seq) {
        log.info("begin akr em data selectOne");
        List<AKREMDataPo> list = mAKREMDataMapper.selectOne(dev_id, e_seq);
        return list;
    }

    @Override
    public int insertReadData(AKREMReadPo mAKREMReadPo) {
        log.info("begin akr em read data addOne");
        return mAKREMDataMapper.insertReadData(mAKREMReadPo);
    }

    @Override
    public List<AKREMReadPo> selectRead(String imei, String e_num, String type) {
        log.info("begin akr em read data selectOne");
        List<AKREMReadPo> list = mAKREMDataMapper.selectRead(imei, e_num, type);
        return list;
    }

    @Override
    public int insertEventData(AKREMEventPo mAKREMEventPo) {
        log.info("begin akr em event data addOne");
        return mAKREMDataMapper.insertEventData(mAKREMEventPo);
    }

    @Override
    public List<AKREMEventPo> selectEvent(String imei, String e_num, String type) {
        log.info("begin akr em event data selectOne");
        List<AKREMEventPo> list = mAKREMDataMapper.selectEvent(imei, e_num, type);
        return list;
    }
}
