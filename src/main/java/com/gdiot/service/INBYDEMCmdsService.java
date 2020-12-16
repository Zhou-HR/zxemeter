package com.gdiot.service;

import java.util.List;

import com.gdiot.model.EMCmdsSEQPo;

/**
 * @author ZhouHR
 */
public interface INBYDEMCmdsService {
    /**
     * @param mNBYDEMCmdsPo
     * @return
     */
    int insertcmdseq(EMCmdsSEQPo mNBYDEMCmdsPo);

    /**
     * @param mNBYDEMCmdsPo
     * @return
     */
    int updatecmdseq(EMCmdsSEQPo mNBYDEMCmdsPo);

    /**
     * @param imei
     * @return
     */
    List<EMCmdsSEQPo> selectcmdseq(String imei);
}
