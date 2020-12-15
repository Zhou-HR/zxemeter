package com.gdiot.service;

import com.gdiot.model.QDEMDataPo;

/**
 * @author ZhouHR
 * @date 2020/12/14
 */
public interface IQDEMDataService {
    /**
     * @param qdemDataPo
     * @return
     */
    int addOne(QDEMDataPo qdemDataPo);
}
