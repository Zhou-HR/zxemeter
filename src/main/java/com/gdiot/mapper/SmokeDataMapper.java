package com.gdiot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gdiot.model.SmokeDataPo;
import com.gdiot.model.SmokeDevicePo;
import com.gdiot.model.SmokeDownPo;

public interface SmokeDataMapper {
	
	int insertOne(SmokeDataPo mSmokeDataPo);

	int countbyDevId(@Param("dev_id")String dev_id);
	List<SmokeDataPo> selectbyDevId(@Param("dev_id")String dev_id,@Param("limit")int limit,@Param("offset")int offset);
	
	List<SmokeDevicePo> selectAlarmList(@Param("deviceId")String deviceId,@Param("deviceType")String deviceType);
	
	int insertAlarmDown(SmokeDownPo mSmokeDownPo);
}
