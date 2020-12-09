package com.gdiot.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdiot.mapper.WMDataMapper;
import com.gdiot.model.WMCmdDataPo;
import com.gdiot.model.WMCmdSendLogPo;
import com.gdiot.model.WMDataPo;
import com.gdiot.model.WMReadDataPo;
import com.gdiot.service.IWMDataService;

/**
 * @author ZhouHR
 */
@Service("IWMDataService")
public class WMDataServiceImpl implements IWMDataService {

    private Logger log = LoggerFactory.getLogger(WMDataServiceImpl.class);

    @Autowired
    private WMDataMapper mWMDataMapper;

    @Override
    public int addOne(WMDataPo WMDataPo) {
        log.info("begin addOne");
        return mWMDataMapper.insertOne(WMDataPo);
    }

    @Override
    public List<WMDataPo> selectbyDevId(String dev_id, int pageNo, int pageSize) {
        int count = mWMDataMapper.countbyDevId(dev_id);
        log.info("selectbyDevId lora wm count=" + count);
        int limit = pageSize;
        int offset = (pageNo - 1) * pageSize;

        List<WMDataPo> list = mWMDataMapper.selectbyDevId(dev_id, limit, offset);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("count", count);
        map.put("list", list);
        return list;
    }

    @Override
    public List<WMDataPo> selectbyNum(String wm_num) {
        List<WMDataPo> list = mWMDataMapper.selectbyNum(wm_num);
        return list;
    }

    @Override
    public int insertCmdData(WMCmdDataPo mWMCmdDataPo) {
        log.info("begin insertCmdData");
        return mWMDataMapper.insertCmdSendData(mWMCmdDataPo);
    }

    @Override
    public int updateCmdStatus(WMCmdDataPo mWMCmdDataPo) {
        log.info("begin updateCmdStatus");
        return mWMDataMapper.updateCmdSendStatus(mWMCmdDataPo);
    }

    @Override
    public List<WMCmdDataPo> selectSendCmd() {
        List<WMCmdDataPo> list = mWMDataMapper.selectCmdSendData();
        return list;
    }

    @Override
    public int insertCmdSendLog(WMCmdSendLogPo mWMCmdSendLogPo) {
        log.info("begin insertCmdSendLog");
        return mWMDataMapper.insertCmdSendLog(mWMCmdSendLogPo);
    }

    @Override
    public List<WMReadDataPo> selectReadData(String wm_num, String type) {
        log.info("begin selectReadData");
        return mWMDataMapper.selectReadData(wm_num, type);
    }

    @Override
    public int insertReadData(WMReadDataPo mWMReadDataPo) {
        log.info("begin insertReadData");
        return mWMDataMapper.insertReadData(mWMReadDataPo);
    }

}
