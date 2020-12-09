package com.gdiot.service;

import java.util.List;
import java.util.Map;

import com.gdiot.model.YDEMNBReadPo;

/**
 * @author ZhouHR
 */
public interface INBYDEMReadService {

    List<YDEMNBReadPo> listNBEMRead(String imei, String type, String data_seq);

    Map<String, Object> listGetAllRead(String imei);

    int addOne(YDEMNBReadPo mYDEMNBReadPo);

}
