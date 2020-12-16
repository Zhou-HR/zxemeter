package com.gdiot.service;

import java.util.List;
import java.util.Map;

import com.gdiot.model.YDEMNBReadPo;

/**
 * @author ZhouHR
 */
public interface INBYDEMReadService {
    /**
     * @param imei
     * @param type
     * @param data_seq
     * @return
     */
    List<YDEMNBReadPo> listNBEMRead(String imei, String type, String data_seq);

    /**
     * @param imei
     * @return
     */
    Map<String, Object> listGetAllRead(String imei);

    /**
     * @param mYDEMNBReadPo
     * @return
     */
    int addOne(YDEMNBReadPo mYDEMNBReadPo);

}
