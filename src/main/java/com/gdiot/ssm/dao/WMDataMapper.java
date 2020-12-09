package com.gdiot.ssm.dao;

import com.gdiot.ssm.entity.WMCmdDataPo;
import com.gdiot.ssm.entity.WMCmdSendLogPo;
import com.gdiot.ssm.entity.WMDataPo;
import com.gdiot.ssm.entity.WMReadDataPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ZhouHR
 */
@Mapper
@Component
public interface WMDataMapper {

    int insertOne(WMDataPo wMDataPo);

    int countbyDevId(@Param("dev_id") String dev_id);

    List<WMDataPo> selectbyDevId(@Param("dev_id") String dev_id, @Param("limit") int limit, @Param("offset") int offset);

    List<WMDataPo> selectbyNum(@Param("wm_num") String wm_num);

    int insertCmdSendData(WMCmdDataPo mWMCmdDataPo);

    int updateCmdSendStatus(WMCmdDataPo mWMCmdDataPo);

    List<WMCmdDataPo> selectCmdSendData();

    int insertCmdSendLog(WMCmdSendLogPo mWMCmdSendLogPo);

    int insertReadData(WMReadDataPo mWMReadDataPo);

    List<WMReadDataPo> selectReadData(@Param("wm_num") String wm_num, @Param("type") String type);
}
