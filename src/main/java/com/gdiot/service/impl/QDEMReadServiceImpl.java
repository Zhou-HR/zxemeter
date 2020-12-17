package com.gdiot.service.impl;

import com.gdiot.mapper.QDEMDataMapper;
import com.gdiot.mapper.QDEMReadMapper;
import com.gdiot.model.QDEMReadPo;
import com.gdiot.service.IQDEMReadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZhouHR
 */
@Slf4j
@Service("QDEMReadService")
public class QDEMReadServiceImpl implements IQDEMReadService {
    @Autowired
    private QDEMReadMapper mQDEMReadMapper;

    @Override
    public int insertReadData(QDEMReadPo mQDEMReadPo) {
        log.info("begin akr em read data addOne");
        return mQDEMReadMapper.insertReadData(mQDEMReadPo);
    }
}
