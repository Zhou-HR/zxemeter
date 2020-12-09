package com.gdiot.service;

import java.util.List;
import java.util.Map;

import com.gdiot.model.XBEMDataPo;

/**
 * @author ZhouHR
 */
public interface IXBEMDataService {

    int addOne(XBEMDataPo mXBEMDataPo);

    Map<String, Object> selectList(String dev_id, String eNum, Long beginTime, Long endTime, String source, int pageNo, int pageSize);

    List<XBEMDataPo> selectOne(String imei, long e_seq, String source_type);

    List<XBEMDataPo> selectbySeq(String imei, String type, int flag_reload, int value);
}
