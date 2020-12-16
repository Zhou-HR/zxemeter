package com.gdiot.service;

import java.util.List;
import java.util.Map;

import com.gdiot.model.YDEMeterEventPo;

/**
 * @author ZhouHR
 */
public interface INBYDEMEventService {
    /**
     * @param dev_id
     * @param eNum
     * @param imei
     * @param beginTime
     * @param endTime
     * @param source
     * @param pageNo
     * @param pageSize
     * @return
     */
    Map<String, Object> listNBEMeterData(String dev_id, String eNum, String imei, Long beginTime, Long endTime, String source, int pageNo, int pageSize);

    /**
     * @param mYDEMeterDataPo
     * @return
     */
    int addOne(YDEMeterEventPo mYDEMeterDataPo);

    /**
     * @param imei
     * @param type
     * @param flag_reload
     * @param value
     * @return
     */
    List<YDEMeterEventPo> selectEventbySeq(String imei, String type, int flag_reload, int value);
}
