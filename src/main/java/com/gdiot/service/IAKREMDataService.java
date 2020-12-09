package com.gdiot.service;

import java.util.List;
import java.util.Map;

import com.gdiot.model.AKREMDataPo;
import com.gdiot.model.AKREMEventPo;
import com.gdiot.model.AKREMReadPo;

/**
 * @author ZhouHR
 */
public interface IAKREMDataService {

    int addOne(AKREMDataPo mAKREMDataPo);

    Map<String, Object> selectList(String dev_id, String eNum, Long beginTime, Long endTime, String source, int pageNo, int pageSize);

    List<AKREMDataPo> selectOne(String imei, long e_seq);

    int insertReadData(AKREMReadPo mAKREMReadPo);

    List<AKREMReadPo> selectRead(String imei, String e_num, String type);

    int insertEventData(AKREMEventPo mAKREMEventPo);

    List<AKREMEventPo> selectEvent(String imei, String e_num, String type);
}
