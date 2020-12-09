package com.gdiot.service;

import java.util.Map;

import com.gdiot.model.YDEMeterEventPo;

/**
 * @author ZhouHR
 */
public interface INBYDEMEventService {

    Map<String, Object> listNBEMeterData(String dev_id, String eNum, String imei, Long beginTime, Long endTime, String source, int pageNo, int pageSize);

    int addOne(YDEMeterEventPo mYDEMeterDataPo);


}
