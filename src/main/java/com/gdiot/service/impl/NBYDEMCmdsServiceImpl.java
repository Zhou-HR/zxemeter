package com.gdiot.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdiot.mapper.NBYDEMCmdsMapper;
import com.gdiot.model.EMCmdsSEQPo;
import com.gdiot.service.INBYDEMCmdsService;

/**
 * @author ZhouHR
 */
@Service("nbYDEMCmdsService")
public class NBYDEMCmdsServiceImpl implements INBYDEMCmdsService {

    static Logger log = LoggerFactory.getLogger(NBYDEMCmdsServiceImpl.class);

    @Autowired
    private NBYDEMCmdsMapper mNBYDEMCmdsMapper;

    @Override
    public int insertcmdseq(EMCmdsSEQPo mNBYDEMCmdsPo) {
        log.info("insertcmdseq------------");
        return mNBYDEMCmdsMapper.insertcmdseq(mNBYDEMCmdsPo);
    }

    @Override
    public int updatecmdseq(EMCmdsSEQPo mNBYDEMCmdsPo) {
        log.info("updatecmdseq-------------");
        return mNBYDEMCmdsMapper.updatecmdseq(mNBYDEMCmdsPo);
    }

    @Override
    public List<EMCmdsSEQPo> selectcmdseq(String imei) {
        return mNBYDEMCmdsMapper.selectcmdseq(imei);
    }

}
