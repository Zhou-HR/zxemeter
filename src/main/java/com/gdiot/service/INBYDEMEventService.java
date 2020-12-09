package com.gdiot.service;

import java.util.List;
import java.util.Map;

import com.gdiot.model.YDEMeterEventPo;

/**
 * @author ZhouHR
 */
public interface INBYDEMEventService {

    Map<String, Object> listNBEMeterData(String dev_id, String eNum, String imei, Long beginTime, Long endTime, String source, int pageNo, int pageSize);

    int addOne(YDEMeterEventPo mYDEMeterDataPo);

    List<YDEMeterEventPo> selectEventbySeq(String imei, String type, int flag_reload, int value);
}
