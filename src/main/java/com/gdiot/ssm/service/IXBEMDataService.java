package com.gdiot.ssm.service;

import java.util.List;
import java.util.Map;

import com.gdiot.ssm.entity.XBEMDataPo;

/**
 * @author ZhouHR
 */
public interface IXBEMDataService {

    int addOne(XBEMDataPo mXBEMDataPo);

    Map<String, Object> selectList(String dev_id, String eNum, Long beginTime, Long endTime, String source, int pageNo, int pageSize);

    List<XBEMDataPo> selectOne(String imei, long e_seq);
}
