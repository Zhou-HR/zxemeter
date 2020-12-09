package com.gdiot.ssm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gdiot.ssm.entity.SmokeDataPo;

public interface SmokeDataMapper {
	
	int insertOne(SmokeDataPo mSmokeDataPo);

	int countbyDevId(@Param("dev_id")String dev_id);
	List<SmokeDataPo> selectbyDevId(@Param("dev_id")String dev_id,@Param("limit")int limit,@Param("offset")int offset);
}
