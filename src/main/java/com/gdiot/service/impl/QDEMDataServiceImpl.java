package com.gdiot.service.impl;

import com.gdiot.mapper.QDEMDataMapper;
import com.gdiot.model.QDEMReadPo;
import com.gdiot.service.IQDEMDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZhouHR
 */
@Slf4j
@Service("QDEMDataService")
public class QDEMDataServiceImpl implements IQDEMDataService {
    @Autowired
    private QDEMDataMapper mQDEMDataMapper;

    @Override
    public int insertReadData(QDEMReadPo mQDEMReadPo) {
        log.info("begin akr em read data addOne");
        return mQDEMDataMapper.insertReadData(mQDEMReadPo);
    }
}
