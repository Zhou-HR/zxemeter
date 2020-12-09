package com.gdiot.service;

import java.util.List;

import com.gdiot.model.EMCmdsSEQPo;

/**
 * @author ZhouHR
 */
public interface INBYDEMCmdsService {

    int insertcmdseq(EMCmdsSEQPo mNBYDEMCmdsPo);

    int updatecmdseq(EMCmdsSEQPo mNBYDEMCmdsPo);

    List<EMCmdsSEQPo> selectcmdseq(String imei);
}
