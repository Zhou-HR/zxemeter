package com.gdiot.service;

import com.gdiot.model.KTREMReadPo;
import com.gdiot.model.ktEmDataPo;

/**
 * @author: shenzhengkai
 * @className: JbqDataService
 * @description: JbqDataService
 * @date: 日期: 2020/10/23 时间: 17:10
 **/
public interface KTDataService {

    void insertKtData(ktEmDataPo ktEmDataPo);

    int insertCmdReadData(KTREMReadPo ktremReadPo);
}
