package com.gdiot.ssm.service;

import java.util.List;
import java.util.Map;

import com.gdiot.ssm.entity.YDEMeterDataPo;

/**
 * @author ZhouHR
 */
public interface INBYDEMeterDataService {

    Map<String, Object> listNBEMeterData(String dev_id, String eNum, String imei, Long beginTime, Long endTime, String source, int pageNo, int pageSize);

    int addOne(YDEMeterDataPo mYDEMeterDataPo);

    //	Map<String, Object> selectbyimei(String imei);
    List<YDEMeterDataPo> selectbyimei(String imei);

    int updateData(YDEMeterDataPo mYDEMeterDataPo);

    List<YDEMeterDataPo> selectbyseq(String imei, long e_seq);
}
