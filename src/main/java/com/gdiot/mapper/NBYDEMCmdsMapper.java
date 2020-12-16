package com.gdiot.mapper;

import com.gdiot.model.EMCmdsSEQPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ZhouHR
 */
@Mapper
@Component
public interface NBYDEMCmdsMapper {
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
    List<EMCmdsSEQPo> selectcmdseq(@Param("imei") String imei);
}
