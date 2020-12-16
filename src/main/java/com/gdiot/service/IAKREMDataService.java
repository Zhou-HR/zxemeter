package com.gdiot.service;

import java.util.List;
import java.util.Map;

import com.gdiot.model.AKREMDataPo;
import com.gdiot.model.AKREMReadPo;

/**
 * @author ZhouHR
 */
public interface IAKREMDataService {
    /**
     * @param mAKREMDataPo
     * @return
     */
    int addOne(AKREMDataPo mAKREMDataPo);

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
     * @return
     */
    List<AKREMDataPo> selectOne(String imei);

    /**
     * @param mAKREMReadPo
     * @return
     */
    int insertReadData(AKREMReadPo mAKREMReadPo);

    /**
     * @param imei
     * @param e_num
     * @param type
     * @return
     */
    List<AKREMReadPo> selectRead(String imei, String e_num, String type);
}
