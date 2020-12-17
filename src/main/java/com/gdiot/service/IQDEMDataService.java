package com.gdiot.service;

import com.gdiot.model.QDEMReadPo;

/**
 * @author ZhouHR
 */
public interface IQDEMDataService {
    /**
     * @param mQDEMReadPo
     * @return
     */
    int insertReadData(QDEMReadPo mQDEMReadPo);
}
