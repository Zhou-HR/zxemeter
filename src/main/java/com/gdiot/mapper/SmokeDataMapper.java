package com.gdiot.mapper;

import com.gdiot.model.SmokeDataPo;
import com.gdiot.model.SmokeDevicePo;
import com.gdiot.model.SmokeDownPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ZhouHR
 */
@Mapper
@Component
public interface SmokeDataMapper {

    int insertOne(SmokeDataPo mSmokeDataPo);

    int countbyDevId(@Param("dev_id") String dev_id);

    List<SmokeDataPo> selectbyDevId(@Param("dev_id") String dev_id, @Param("limit") int limit, @Param("offset") int offset);

    List<SmokeDevicePo> selectAlarmList(@Param("deviceId") String deviceId, @Param("deviceType") String deviceType);

    int insertAlarmDown(SmokeDownPo mSmokeDownPo);
}
