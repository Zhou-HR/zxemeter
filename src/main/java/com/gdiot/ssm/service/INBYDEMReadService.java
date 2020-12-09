package com.gdiot.ssm.service;

import java.util.Map;

import com.gdiot.ssm.entity.YDEMNBReadPo;

/**
 * @author ZhouHR
 */
public interface INBYDEMReadService {

    Map<String, Object> listNBEMRead(String dev_id, String eNum, String imei, Long beginTime, Long endTime, String source, int pageNo, int pageSize);

    int addOne(YDEMNBReadPo mYDEMNBReadPo);

}
