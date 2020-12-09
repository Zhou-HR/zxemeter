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

    int insertcmdseq(EMCmdsSEQPo mNBYDEMCmdsPo);

    int updatecmdseq(EMCmdsSEQPo mNBYDEMCmdsPo);

    List<EMCmdsSEQPo> selectcmdseq(@Param("imei") String imei);
}
