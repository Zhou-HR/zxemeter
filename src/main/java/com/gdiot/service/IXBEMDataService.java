package com.gdiot.service;

import java.util.List;
import java.util.Map;

import com.gdiot.model.XBEMDataPo;

/**
 * @author ZhouHR
 */
public interface IXBEMDataService {
    /**
     * @param mXBEMDataPo
     * @return
     */
    int addOne(XBEMDataPo mXBEMDataPo);

    /**
     * @param dev_id
     * @param eNum
     * @param beginTime
     * @param endTime
     * @param source
     * @param pageNo
     * @param pageSize
     * @return
     */
    Map<String, Object> selectList(String dev_id, String eNum, Long beginTime, Long endTime, String source, int pageNo, int pageSize);

    /**
     * @param imei
     * @param e_seq
     * @param source_type
     * @return
     */
    List<XBEMDataPo> selectOne(String imei, long e_seq, String source_type);

    /**
     * @param imei
     * @param type
     * @param flag_reload
     * @param value
     * @return
     */
    List<XBEMDataPo> selectbySeq(String imei, String type, int flag_reload, int value);
}
