package com.gdiot.ssm.service;

import java.util.List;

import com.gdiot.ssm.entity.SmokeDataPo;


/**
 * @author ZhouHR
 */
public interface ISmokeDataService {

    int insert(SmokeDataPo mSmokeDataPo);

    List<SmokeDataPo> selectbyDevId(String dev_id, int pageNo, int pageSize);
}
