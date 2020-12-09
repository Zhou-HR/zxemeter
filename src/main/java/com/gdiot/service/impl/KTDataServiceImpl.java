package com.gdiot.service.impl;

import com.gdiot.mapper.KTNBEMDataMapper;
import com.gdiot.model.KTREMReadPo;
import com.gdiot.model.ktEmDataPo;
import com.gdiot.service.KTDataService;
import com.gdiot.util.EmDataNBUtil;
import com.gdiot.util.Utilty;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author: shenzhengkai
 * @className: JbqDataServiceImpl
 * @description: JbqDataServiceImpl
 * @date: 日期: 2020/10/23 时间: 17:10
 **/
@Slf4j
@Service("KTDataService")
public class KTDataServiceImpl implements KTDataService {
    private Logger log = LoggerFactory.getLogger(KTDataServiceImpl.class);
    @Autowired
    private KTNBEMDataMapper ktnbemDataMapper;


    @Override
    public void insertKtData(ktEmDataPo ktEmDataPo) {
        log.info("save kt data");
        int num = ktnbemDataMapper.insertKTData(ktEmDataPo);
        if (num > 0) {
            log.info("保存成功");
        } else {
            log.info("保存失败");
        }

    }

    @Override
    public int insertCmdReadData(KTREMReadPo ktremReadPo) {
        int num = ktnbemDataMapper.insertCmdReadData(ktremReadPo);
        if (num > 0) {
            log.info("保存成功");
        } else {
            log.info("保存失败");
        }
        return num;
    }

}
