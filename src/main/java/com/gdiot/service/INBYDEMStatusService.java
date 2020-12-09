package com.gdiot.service;

import java.util.List;
import java.util.Map;

import com.gdiot.model.YDEMeterStatusPo;

/**
 * @author ZhouHR
 */
public interface INBYDEMStatusService {

    Map<String, Object> selectStatus(String dev_id, String imei, String status, int pageNo, int pageSize);

    int insertStatus(YDEMeterStatusPo mYDEMeterStatusPo);

    int updateEMStatus(YDEMeterStatusPo mYDEMeterStatusPo);

    List<YDEMeterStatusPo> selectDevid(String dev_id, String imei);
}
